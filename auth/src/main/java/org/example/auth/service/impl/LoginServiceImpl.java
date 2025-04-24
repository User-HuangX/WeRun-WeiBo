package org.example.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import org.example.auth.mapper.LoginMapper;
import org.example.auth.pojo.bo.CheckEmailBO;
import org.example.auth.pojo.dto.*;
import org.example.common.model.user.UserBO;
import org.example.auth.pojo.po.RegisterPO;
import org.example.auth.pojo.po.ResetPO;
import org.example.auth.pojo.vo.LoginVO;
import org.example.auth.service.LoginService;
import org.example.auth.utils.JwtUtils;
import org.example.auth.utils.MD5Encryptor;
import org.example.auth.utils.RedisIdWorker;
import org.example.common.model.global.AppException;
import org.example.common.model.global.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final MailSender sender;

    public LoginServiceImpl(LoginMapper loginMapper, StringRedisTemplate stringRedisTemplate, MailSender sender) {
        this.loginMapper = loginMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.sender = sender;
    }
    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Override
    @Async
    public CompletableFuture<LoginVO> login(LoginDTO loginDTO) {
        return CompletableFuture.supplyAsync(()->getUserBO(loginDTO)).
                thenApplyAsync(this::jwtMaker).
                thenApplyAsync(this::returnToken).
                orTimeout(5, TimeUnit.SECONDS);
    }

    private List<UserBO> getUserBO(LoginDTO loginDTO) {
        logger.info("现在进行登录,现在的传入的参数为:{}", loginDTO);
        loginDTO.setPassword(MD5Encryptor.encryptToMD5(loginDTO.getPassword()));
        logger.debug("完成MD5算法加密");
        List<UserBO> userBO = loginMapper.login(loginDTO);
        logger.info("完成密码账号比对,获取的数据为:{}", userBO);
        return userBO;
    }

    private LoginVO returnToken(LoginVO loginVO) { //返回token和refreshToken
        logger.debug("返回token和refreshToken:{}",loginVO.getToken());
        logger.debug("返回token和refreshToken:{}",loginVO.getRefreshtoken());
        return new LoginVO(loginVO.getToken(), loginVO.getRefreshtoken());
    }


    private LoginVO jwtMaker(List<UserBO> userBO) {
        logger.debug("现在进入JWT令牌的加工环节");
        List<UserBO> newUserBO = userBO.stream().filter(aloginPO -> aloginPO.getStatus() > 0).toList();
        logger.info("进行假删除过的账号的过滤工作,过滤后为:{}", newUserBO);
        if (newUserBO.isEmpty()) {
            logger.error("生成JWT令牌时，过滤后的获得的用户为空");
            throw new AppException(HttpStatus.BAD_REQUEST,"登录错误",null);
        }
        UserBO oneloginBO;
        if(newUserBO.size()>1){
         logger.error("唯一账号发生重复问题，为了防止出现问题仍然负载均衡进行登录");
         oneloginBO = newUserBO.get(RandomUtil.randomInt(0, newUserBO.size()-1));
        }else {
            oneloginBO = newUserBO.get(0);
        }
        logger.debug("负载均衡获取到一个用户信息");
        Map<String,Object> map =new HashMap<>();
        logger.debug("创建Map集合");
        map.put("uuid", oneloginBO.getUuid());
        map.put("username", oneloginBO.getUsername());
        map.put("phone", oneloginBO.getPhone());
        map.put("email", oneloginBO.getEmail());
        map.put("signature", oneloginBO.getSignature());
        map.put("avatarURL", oneloginBO.getAvatarURL());
        map.put("address", oneloginBO.getAddress());
        map.put("gender", oneloginBO.getGender());
        map.put("birthday", oneloginBO.getBirthday());
        logger.debug("完成Map集合的添加：{}",map);
        String jwt = JwtUtils.generateJwt(map);
        logger.debug("完成JWT令牌加工:{}",jwt);
        String refreshjwt = JwtUtils.generateRefreshJwt(map);
        logger.debug("完成refreshJWT令牌加工:{}",jwt);
        return new LoginVO(jwt,refreshjwt);
    }

    @Override
    public void logout(String token) {
        logger.debug("登出操作，将JWT拉黑");
        stringRedisTemplate.opsForValue().set("blacklist:jwt"+token.substring(7), "BLACK", 3600,java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public void register(RegisterDTO registerDTO, MultipartFile file) {
        logger.debug("现在进行注册操作");
        if(loginMapper.checkUnique(registerDTO.getPhone(),registerDTO.getEmail())!=0){
            logger.error("注册发生重复的问题");
            throw new AppException(HttpStatus.BAD_REQUEST,"手机号或邮箱已被注册",null);
        }
        logger.debug("完成发生验证码进行下面的操作");
        sendCode(registerDTO.getEmail(),"register");
        logger.debug("完成校验码的邮箱发送");
    }

    @Override
    @Async
    public void mLogin(String Email) {
        logger.debug("现在进行邮箱登录操作");
        sendCode(Email, "code");
    }

    private void sendCode(String email, String type) {
        logger.debug("进行邮箱发送验证码");
        if (stringRedisTemplate.hasKey("email:code:"+email)) {
            logger.error("验证码发过了,如需重新接受等待600s");
            throw new AppException(HttpStatus.BAD_REQUEST,"验证码已发送",null);
        }
        SimpleMailMessage mail =new SimpleMailMessage();
        String code = RandomUtil.randomNumbers(6);
        logger.info("完成生成验证码:{}",code);
        stringRedisTemplate.opsForValue().set("email:"+ type +":"+email, code, 600, java.util.concurrent.TimeUnit.SECONDS);
        mail.setSubject("验证码");
        mail.setText("验证码为：\n"+ code);
        mail.setTo(email);
        mail.setFrom("A19861577050@126.com");
        sender.send(mail);
        logger.debug("完成验证码的发送");
    }

    @Override
    public LoginVO LoginByCode(CheckEmailBO l) {
        logger.debug("进行验证码检测");
        CheckCode(l,"code");
        List<UserBO> userBO = loginMapper.LoginByCode(l.getEmail());
        return jwtMaker(userBO);
    }

    private void CheckCode(CheckEmailBO l, String key) {
        if (stringRedisTemplate.hasKey("email:"+key+":"+ l.getEmail())) {
            String code = stringRedisTemplate.opsForValue().get("email:"+key+":"+ l.getEmail());
            if (code != null && !code.equals(l.getCode())) {
                throw new AppException(HttpStatus.BAD_REQUEST, "验证码错误", null);
            }
        }else {
            throw new AppException(HttpStatus.BAD_REQUEST,"验证码已过期",null);
        }
        logger.debug("登录验证码的校验成功");
    }

    @Override
    public void Reset(ResetDTO resetDTO) {
        sendCode(resetDTO.getEmail(), "reset");
    }

    @Override
    public void ResetPassword(ResetPasswordDTO resetPasswordDTO) {
        CheckCode(new CheckEmailBO(resetPasswordDTO.getEmail(), resetPasswordDTO.getCode()),"reset");
        resetPasswordDTO.setPassword(MD5Encryptor.encryptToMD5(resetPasswordDTO.getPassword()));
        ResetPO resetPO = new ResetPO(resetPasswordDTO.getEmail(), resetPasswordDTO.getPhone(),resetPasswordDTO.getPassword());
        loginMapper.ResetPassword(resetPO);
    }

    @Override
    public void registerOK(RegisterDTO registerDTO, MultipartFile file, String code) {
        registerDTO.setPassword(MD5Encryptor.encryptToMD5(registerDTO.getPassword()));
        CheckCode(new CheckEmailBO(registerDTO.getEmail(), code),"register");
        logger.debug("完成验证码的校验");
        RedisIdWorker redisIdWorker = new RedisIdWorker(stringRedisTemplate);
        String imagePath = null;
        try {
            imagePath = "/var/weiboImage" + redisIdWorker
                    .nextId("image") +
                    "." +
                    Objects.requireNonNull(file.getOriginalFilename()).
                            substring(file.
                                    getOriginalFilename().
                                    lastIndexOf(".") + 1);
            file.transferTo(new File(imagePath));
        } catch (Exception e) {
            logger.error("图片上传出现问题");
        }
        logger.debug("现在进行注册信息存入操作");
        loginMapper.register(new RegisterPO(
                registerDTO.getEmail(),
                registerDTO.getUserName(),
                registerDTO.getPhone(),
                registerDTO.getPassword(),
                registerDTO.getGender(),
                imagePath,
                String.valueOf(redisIdWorker.nextId("uuid"))
                )
        );
    }
}
