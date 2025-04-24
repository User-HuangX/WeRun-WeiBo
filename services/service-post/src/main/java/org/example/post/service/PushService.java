package org.example.post.service;

import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;

import java.util.concurrent.CompletableFuture;

public interface PushService {
    CompletableFuture<PageResult<PostVO>> pushPosts(int page, int pageSize);
    CompletableFuture<PageResult<PostVO>> pushPostsByCommentCount(int page, int pageSize);
}
