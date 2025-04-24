package org.example.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.*;
import org.example.post.pojo.vo.PostVO;

import java.util.List;

@Mapper
public interface SearchPostMapper {
    //根据输入的关键词查询帖子
    @Select("SELECT * FROM post WHERE title LIKE CONCAT('%', #{keyword}, '%') OR content LIKE CONCAT('%', #{keyword}, '%')")
    List<PostVO> searchPosts(String keyword);

    //根据输入的标签查询帖子
    @Select("SELECT p.* FROM post p JOIN post_tags pt ON p.uuid = pt.post_id WHERE pt.tagname LIKE CONCAT('%', #{tagname}, '%')")
    List<PostVO> searchPostsByTag(@Param("tagname") String tagname);
    @Select("SELECT * FROM post WHERE uuid = #{uuid}")
    PostVO searchPostsById(String uuid);
    @Select("SELECT count(*) FROM post WHERE uuid = #{uuid} AND author_id = #{authorId}")
    int checkPostsById(String uuid, String authorId);
}
