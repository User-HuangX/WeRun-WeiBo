package org.example.post.service;


import org.example.post.pojo.dto.PostDTO;
import org.example.post.pojo.po.PostPO;
import org.example.post.pojo.dto.UpdateDTO;

import java.text.ParseException;

public interface PostService {

    void publishPost(PostPO postPO);
//    void insertTag(String tagname);
//
//    void selectTagId(String tagname);
//
//    void insertPostTag(Long postid, Long tagid);

    void updatePost(UpdateDTO updateDto);
    //    void insertTag(String tagname);
//    void selectTagId(String tagname);
//    void deletePostTag(Long postid, Long tagid);
    void deletePost(String uuid);


    PostPO assemblePO(PostDTO postDTO) throws ParseException;
}
