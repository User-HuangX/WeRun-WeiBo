package org.example.post.mapper;

import org.apache.ibatis.annotations.*;
import org.example.post.pojo.po.PostPO;
import org.example.post.pojo.dto.UpdateDTO;

@Mapper
public interface PostMapper {
        // 插入帖子
        @Insert("INSERT INTO post (uuid, title, content,author_id) VALUES (#{uuid}, #{title},#{schedule},#{authorId})")
        void insertPost(PostPO postPO);
        //更新帖子
        @Update("UPDATE post SET title = #{title}, content = #{content} ,update_time=#{updateTime} WHERE uuid = #{uuid}")
        void updatePost(UpdateDTO updateDto);
        //删除帖子
        @Delete("DELETE FROM post WHERE uuid = #{uuid};")
        void deletePost(String uuid);

        @Insert("INSERT INTO post_tags (post_id, tagname) VALUES (#{postid}, #{tagname})")
        void insertPostTag(@Param("postid") String postid, @Param("tagname")String tagname);

        //删除 帖子关联标签
        @Delete("DELETE FROM post_tags WHERE post_id = #{postid} AND tagname = #{tagname}")
        void deletePostTags(@Param("postid") String postid, @Param("tagname")String tagname );

        @Delete("DELETE FROM post_tags WHERE post_id = #{uuid}")
        void deleteTags(String uuid);
}

