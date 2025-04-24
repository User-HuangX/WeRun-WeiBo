package org.example.post.service.impl;

import org.example.post.mapper.PostMapper;
import org.example.post.pojo.dto.PostDTO;
import org.example.post.pojo.po.PostPO;
import org.example.post.pojo.dto.UpdateDTO;
import org.example.post.service.PostService;
import org.example.post.utils.JwtUtils;
import org.example.post.utils.RedisIdWorker;
import org.example.post.utils.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class PostServiceImpl implements PostService {

    public PostServiceImpl(PostMapper postMapper, StringRedisTemplate stringRedisTemplate, RedisIdWorker redisIdWorker) {
        this.postMapper = postMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisIdWorker = redisIdWorker;
    }

    private final PostMapper postMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisIdWorker redisIdWorker;
    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    public void publishPost(PostPO postPO) {
        logger.debug("正在进行发布");
        if (postPO.getSchedule() == null) {
            logger.error("选择类别出现问题");
            return;
        }
        // 检查是否为定时发布
        if (postPO.getSchedule() == 0) {
            // 定时发布的逻辑
            logger.debug("现在进行定时发布");
            if (postPO.getScheduleTime() == null) {
                logger.error("帖子发送状态出现问题");
                return;
            }
            try {
                // 确保 getScheduleTime() 返回的是 LocalDateTime 类型
                Date scheduleTime = postPO.getScheduleTime();
                logger.info("定时发布时间：{}", scheduleTime);
                logger.debug("定时发布时间正常进行");
                logger.debug("定时发布时间转换");
                schedulePost(postPO);
                logger.debug("完成定时发布服务");
            } catch (Exception e) {
                logger.error("Error while processing scheduled time: {}", e.getMessage(), e);
            }
        } else if (postPO.getSchedule() == 1 || postPO.getSchedule() == 2) {
            logger.debug("开始发布帖子");
            postMapper.insertPost(postPO);
            logger.debug("帖子发布完成");// 查询帖子的 ID
            String postId = postPO.getUuid();
            logger.debug("将标签放到数据库进行管理");
            if (postPO.getTags() != null && !postPO.getTags().isEmpty()) {
                postPO.getTags().forEach(StoreTag(postId));
            }
        }
    }

    /**
     * 更新帖子  仅包含删除标签
     */
    @Override
    public void updatePost(UpdateDTO updateDto) {
        //检查用户id是否与帖子作者id匹配
        String token = ThreadContext.getThreadLocal();
        if (!updateDto.getUuid().equals(JwtUtils.parseJWT(token).get("uuid") )){
            logger.error("用户id不匹配");
            return;
        }

        logger.debug("现在进入更新帖子的服务");
        if(updateDto.getUpdateTime()!=null){
            logger.debug("更新时间异常");
            return;
        }
        updateDto.setUpdateTime(new Date());
        postMapper.updatePost(updateDto);
        logger.debug("将更新的内容存到数据库");
        String postId = updateDto.getUuid();
        if (updateDto.getDeleteTags() != null) {
            logger.debug("删除标签");
            updateDto.getDeleteTags().forEach(tagName -> {
                postMapper.deletePostTags(postId, tagName);
                logger.info("删除标签{}，{}", postId, tagName);
            });
            logger.debug("删除标签完成");
        }
        if (updateDto.getTags() != null && !updateDto.getTags().isEmpty()) {
            updateDto.getTags().forEach(StoreTag(postId));
        }

    }

    private Consumer<String> StoreTag(String postId) {
        return tagName -> {
            if (tagName != null) {
                logger.info("存入标签{}，{}", postId, tagName);
                postMapper.insertPostTag(postId, tagName);
            }
        };
    }

    /**
     * 删除帖子
     */
    @Override
    public void deletePost(String uuid) {
        //检查用户id是否与帖子作者id匹配
        String token = ThreadContext.getThreadLocal();
        if (!uuid.equals(JwtUtils.parseJWT(token).get("uuid") )){
            logger.error("用户id不匹配");
            return;
        }
        logger.debug("现在进行删除帖子的操作");
        postMapper.deletePost(uuid);
        postMapper.deleteTags(uuid);
        logger.debug("删除帖子完成");
        // 实现删除逻辑
    }

    @Override
    public PostPO assemblePO(PostDTO postDTO){
        logger.debug("开始进行帖子PO的创建");
        String token = ThreadContext.getThreadLocal();
        String jwt = token.substring(7);
        logger.debug("获取到token");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.debug("开始进行时间转换");
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(postDTO.getScheduleTime());
        } catch (Exception e) {
            logger.error("时间转换出现问题");
        }
        logger.debug("时间转换完成");
        try {
            return new PostPO(String.valueOf(redisIdWorker.nextId("post")), (String) JwtUtils.parseJWT(jwt).get("uuid"), postDTO.getTitle(), postDTO.getContent(), postDTO.getSchedule(), parsedDate, postDTO.getTags());
        } catch (Exception e) {
            logger.error("帖子PO创建出现问题");
            return null;

        }
    }

    private void schedulePost(PostPO postPO) {
        logger.debug("正在进行帖子定时发布工作，现在在用redis缓存数据");
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", postPO.getUuid());
        map.put("authorId", postPO.getAuthorId());
        map.put("title", postPO.getTitle());
        map.put("content", postPO.getContent());
        String exp = postPO.getScheduleTime().toString();
        map.put("scheduleTime", exp);
        map.put("tags", postPO.getTags());
        String jwt = JwtUtils.generateJwt(map);
        stringRedisTemplate.opsForValue().set("post:" + postPO.getUuid(), jwt);
    }
}