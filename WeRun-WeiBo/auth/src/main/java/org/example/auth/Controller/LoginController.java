package org.example.auth.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth.POJO.DTO.LoginDTO;
import org.example.auth.POJO.DTO.RegisterDTO;
import org.example.auth.POJO.VO.LoginVO;
import org.example.auth.Service.LoginService;
import org.example.common.model.global.BaseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public BaseResult login(@RequestBody @Valid LoginDTO loginDTO) {
        //校验登录
        LoginVO loginVO;
        try {
            loginVO = loginService.login(loginDTO);
        } catch (Exception e) {
            return BaseResult.error("用户名或者密码错误");
        }
        return BaseResult.success("登录成功", loginVO.getToken());
    }

    @PostMapping("/logout")
    public BaseResult logout(@RequestHeader(value = "id") int id) {
        loginService.logout(id);
        return BaseResult.success("退出成功", null);
    }

    @PostMapping("/register")
    public BaseResult register(@RequestBody @Valid RegisterDTO registerDTO) {
        LoginDTO loginDTO = new LoginDTO(registerDTO.getPhone(),registerDTO.getPassword());
        loginService.register(registerDTO);
        BaseResult result = login(loginDTO);
        return BaseResult.success("完成注册并登录",result.getData());
    }
    @PreAuthorize("denyAll()")
    @GetMapping("/Oss")
    public BaseResult Oss() {
        return BaseResult.error("未知访问",HttpStatus.FORBIDDEN);
    }
}
