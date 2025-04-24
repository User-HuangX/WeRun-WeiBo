package org.example.post.service;

import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;


public interface SearchService {
    //根据输入的关键词u查询帖子并分页展示
    PageResult<PostVO> searchPosts(String keyword, int page, int pageSize);

    //根据标签查询帖子并分页展示
    PageResult<PostVO> searchPostsByTag(String tagName, int page, int pageSize);

    PostVO searchPostsById(String uuid);

    int checkPostsById(String uuid, String authorId);
}
