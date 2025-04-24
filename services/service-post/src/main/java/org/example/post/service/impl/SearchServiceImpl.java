package org.example.post.service.impl;

import org.example.post.mapper.SearchPostMapper;
import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;
import org.example.post.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SearchServiceImpl implements SearchService {

    private final SearchPostMapper searchPostMapper;

    public SearchServiceImpl(SearchPostMapper searchPostMapper) {
        this.searchPostMapper = searchPostMapper;
    }
    Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    //根据关键词查询
    @Override
    public PageResult<PostVO> searchPosts(String keyword, int page, int pageSize) {
            logger.debug("正在进行搜索帖子服务");
        if (page <= 0 || pageSize <= 0) {
            // 如果页码或页面大小小于等于0，则抛出异常
            logger.error("帖子分页发生错误");
            throw new IllegalArgumentException("帖子分页发生错误");
        }
        try {
            logger.debug("正在从DAO层获取数据");
            List<PostVO> list = searchPostMapper.searchPosts(keyword);
            logger.debug("数据获取完成");
            int total = list.size();
            return new PageResult<>(total, list, page, pageSize);
        } catch (Exception e) {
            logger.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }
    }




    @Override
    public PageResult<PostVO> searchPostsByTag(String tagName, int page, int pageSize){
        if (page <= 0 || pageSize <= 0) {
            logger.error("帖子分页发送错误");
            // 如果页码或页面大小小于等于0，则抛出异常
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        try {
            logger.debug("正在从DAO层获取数据，利用tag");
            List<PostVO> list = searchPostMapper.searchPostsByTag(tagName);
            int total = list.size();
            return new PageResult<>(total, list, page, pageSize);

        } catch (Exception e) {
            logger.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }
        }

    @Override
    public PostVO searchPostsById(String uuid) {
        return searchPostMapper.searchPostsById(uuid);
    }

    @Override
    public int checkPostsById(String uuid, String authorId) {
        logger.debug("正在检查帖子");
        return searchPostMapper.checkPostsById(uuid,authorId);
    }
}



