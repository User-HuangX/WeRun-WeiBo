package org.example.post.service;

import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;

public interface UserPostService {

    PageResult<PostVO> getUserPosts(String authid, int page, int pageSize) ;

}
