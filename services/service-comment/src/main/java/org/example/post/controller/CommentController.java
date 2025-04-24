package org.example.post.controller;

import jakarta.validation.Valid;
import org.example.post.utils.ThreadContext;
import org.example.common.model.global.BaseResult;
import org.example.post.pojo.dto.CTCDTO;
import org.example.post.pojo.dto.CTPDTO;
import org.example.post.pojo.dto.ReplyDTO;
import org.example.post.service.CommentService;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RequestMapping("/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/toPost")
    public BaseResult<Object> toPost(@Valid CTPDTO ctpdto, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.toPost(ctpdto,token);
        return BaseResult.success();
    }
    @PostMapping("/toComment")
    public BaseResult<Object> toComment(@Valid CTCDTO ctcdto, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.toComment(ctcdto,token);
        return BaseResult.success();
    }
    @PostMapping("/reply")
    public BaseResult<Object> reply(@Valid ReplyDTO replydto, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.reply(replydto,token);
        return BaseResult.success();
    }
    @GetMapping("/getCommentsByTime")
    public BaseResult<Object> getCommentsByTime(String postId) throws ExecutionException, InterruptedException {
        return BaseResult.success(commentService.getCommentsByTime(postId).get());
    }
    @GetMapping("/getCommentsByLikes")
    public BaseResult<Object> getCommentsByLikes(String postId) throws ExecutionException, InterruptedException {
        return BaseResult.success(commentService.getCommentsByLikes(postId).get());
    }
    @DeleteMapping("/deleteMyCommentsCTC")
    public BaseResult<Object> deleteMyCommentsCTC(String commentId, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.deleteMyCommentsCTC(commentId,token);
        return BaseResult.success();
    }
    @DeleteMapping("/deleteMyCommentsCTP")
    public BaseResult<Object> deleteMyCommentsCTP(String commentId, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.deleteMyCommentsCTP(commentId,token);
        return BaseResult.success();
    }
    @DeleteMapping("/deleteUnderPost")
    public BaseResult<Object> deleteUnderPost(String postId,String commentId, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.deleteUnderPost(postId,commentId,token);
        return BaseResult.success();
    }
    @PostMapping("/like")
    public BaseResult<Object> like(String commentId, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.like(commentId,token);
        return BaseResult.success();
    }
    @PostMapping("/cancellike")
    public BaseResult<Object> cancellike(String commentId, @RequestHeader("Authorization") String token1,@RequestHeader("Authorization0") String token0){
        String token = ThreadContext.getThreadLocal();
        commentService.cancellike(commentId,token);
        return BaseResult.success();
    }
}
