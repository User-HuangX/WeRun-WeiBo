package org.example.post.service;

import jakarta.validation.Valid;
import org.example.post.pojo.dto.CTCDTO;
import org.example.post.pojo.dto.CTPDTO;
import org.example.post.pojo.dto.ReplyDTO;
import org.example.post.pojo.vo.CTPPlusVO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CommentService {
    void toPost(@Valid CTPDTO ctpdto,String token);

    void toComment(@Valid CTCDTO ctcdto, String token);

    void reply(@Valid ReplyDTO replydto, String token);

    CompletableFuture<List<CTPPlusVO>> getCommentsByTime(String postId);

    void deleteMyCommentsCTC(String commentId, String token);

    void deleteMyCommentsCTP(String commentId, String token);

    void deleteUnderPost(String postId, String commentId,String token);

    void like(String commentId, String token);

    void cancellike(String commentId, String token);

    CompletableFuture<List<CTPPlusVO>> getCommentsByLikes(String postId);
}
