package org.example.post.service.impl;

import org.example.post.mapper.UserPostMapper;
import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;
import org.example.post.service.UserPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPostServiceImpl implements UserPostService {

    private final UserPostMapper userPostMapper;

    public UserPostServiceImpl(UserPostMapper userPostMapper) {
        this.userPostMapper = userPostMapper;
    }
    Logger logger = LoggerFactory.getLogger(UserPostServiceImpl.class);
    @Override
    public PageResult<PostVO> getUserPosts(String authid, int page, int pageSize) {
        // 记录方法进入日志
        logger.info("进入按照用户名称查询帖子的操作,用户的ID: {}, page: {}, pageSize: {}", authid, page, pageSize);
        // 参数校验
        if (authid == null || authid.isEmpty()) {
            logger.error("该用户不存在");
            throw new IllegalArgumentException("Authid cannot be null or empty");
        }
        if (page <= 0 || pageSize <= 0) {
            logger.error("Invalid pagination parameters: page={}, pageSize={}", page, pageSize);
            throw new IllegalArgumentException("分页发生错误");
        }

        try {
            // 计算偏移量
            int offset = (page - 1) * pageSize;
            // 查询用户帖子及总数
            List<PostVO> list = userPostMapper.selectUserPosts(authid, offset, pageSize);
            logger.info("查询到帖子:{}",list);
            int total = userPostMapper.countUserPosts(authid);
            // 返回分页结果
            return new PageResult<>(total, list, page, pageSize);
        } catch (Exception e) {
            logger.error("获取帖子发生错误", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }
    }

}


