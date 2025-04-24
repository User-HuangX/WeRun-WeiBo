package org.example.post.service.impl;

import org.example.post.mapper.PushPostMapper;
import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;
import org.example.post.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class PushServiceImpl implements PushService {

    private final PushPostMapper pushPostMapper;

    public PushServiceImpl(PushPostMapper pushPostMapper) {
        this.pushPostMapper = pushPostMapper;
    }
    Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);
    //查询所有帖子并分页展示
    @Override
    @Async
    public CompletableFuture<PageResult<PostVO>> pushPosts(int page, int pageSize) {
        return CompletableFuture.supplyAsync(this::getPostVOS).thenApplyAsync(e-> getPageResult(page, pageSize,e)).orTimeout(5, TimeUnit.SECONDS);
    }

    private PageResult<PostVO> getPageResult(int page, int pageSize, List<PostVO> allPosts) {
        int total = allPosts.size();
        int offset = (page - 1) * pageSize;
        int end = Math.min(offset + pageSize, total);
        List<PostVO> pagePosts = allPosts.subList(offset, end);
        logger.debug("返回分页后的数据");
        return new PageResult<>(total, pagePosts, page, pageSize);
    }

    private List<PostVO> getPostVOS() {
        logger.debug("正在进行推送服务");
        List<PostVO> allPosts = pushPostMapper.selectAllPosts();
        logger.debug("推送数据获取完成");
        logger.debug("开始分页");
        return allPosts;
    }

    @Override
    @Async
    public CompletableFuture<PageResult<PostVO>> pushPostsByCommentCount(int page, int pageSize) {
        return CompletableFuture.supplyAsync(this::getVos).thenApplyAsync(e-> getPageResult(page, pageSize,e)).orTimeout(5, TimeUnit.SECONDS);
    }

    private List<PostVO> getVos() {
        logger.debug("正在进行推送服务");
        List<PostVO> allPosts = pushPostMapper.selectAllPostsByCommentCount();
        logger.debug("推送数据获取完成");
        logger.debug("开始分页");
        return allPosts;
    }

}
