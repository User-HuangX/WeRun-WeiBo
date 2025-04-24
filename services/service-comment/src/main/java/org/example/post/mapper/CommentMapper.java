package org.example.post.mapper;

import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.post.pojo.po.CTCPO;
import org.example.post.pojo.po.CTPPO;
import org.example.post.pojo.po.ReplyPO;
import org.example.post.pojo.vo.CTCVO;
import org.example.post.pojo.vo.CTPVO;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into CTP(comment_id,post_id,user_id,content) values(#{commentId},#{postId},#{userId},#{content})")
    void toPost(CTPPO ctppo);
    @Update("update post set comment_count =comment_count + 1 where uuid = #{postId}")
    void addCommentCount(CTPPO ctppo);
    @Insert("insert into CTC(comment_id,posted_comment_id,user_id,content) values(#{commentId},#{postedCommentId},#{userId},#{content})")
    void toComment(CTCPO ctcpo);
    @Insert("insert into CTC(comment_id,posted_comment_id,user_id,content,reply_id) values(#{commentId},#{postedCommentId},#{userId},#{content},#{replyId})")
    void reply(ReplyPO replyPO);
    @Select("select comment_id, post_id, user_id, content, reply_num,is_liked, create_time from CTP where post_id=#{postId}")
    List<CTPVO> getCTP(String postId);
    @Select("select comment_id, posted_comment_id, user_id, content, is_liked, create_time, reply_id from CTC where posted_comment_id = #{commentId} and status = 1")
    List<CTCVO> getCTC(String commentId);
    @Update("update CTP set reply_num = reply_num + 1 where comment_id = #{commentId}")
    void addCommentNum(String commentId);
    @Update("update CTC set status = 0 where comment_id = #{commentId} and user_id = #{userId}")
    void deleteCTC(String commentId, String userId);
    @Update("update CTP set status = 0 where comment_id = #{commentId} and user_id = #{userId}")
    void deleteCTP(String commentId, String userId);
    @Update("update CTP set status = 0 where comment_id = #{commentId} and post_id = #{postId}")
    void deleteUnderPost(String postId, String commentId);
    @Insert("insert into is_like(comment_id,user_id) values(#{commentId},#{userId})")
    void like(String commentId, String userId);
    @Update("update CTP set is_liked = is_liked + 1 where comment_id = #{commentId}")
    void cancellike(String commentId, String userId);
    @Update("update CTP set is_liked = is_liked - 1 where comment_id = #{commentId}")
    void addLikeNum(String commentId);
    @Select("select comment_id, post_id, user_id, content, reply_num,is_liked, create_time  from CTP where post_id = #{postId} order by is_liked")
    List<CTPVO> getCTPByLikes(String postId);
    @Select("select post_id from CTP where comment_id = #{postedCommentId}")
    String getPostId(@NotNull(message = "正确指定所发的评论") String postedCommentId);
    @Select("select count(*) from is_like where comment_id = #{commentId} and user_id = #{userId}")
    int checkLike(String commentId, String userId);
}
