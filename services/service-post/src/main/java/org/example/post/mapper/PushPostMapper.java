package org.example.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.post.pojo.vo.PostVO;

import java.util.List;
@Mapper
public interface PushPostMapper {
    //查询所有帖子并按照时间顺序排序
    @Select("SELECT * FROM post ORDER BY create_time DESC")
    List<PostVO> selectAllPosts();
    //查询所有帖子并按照评论数排序
    @Select("SELECT * FROM post ORDER BY comment_count DESC")
    List<PostVO> selectAllPostsByCommentCount();

}
