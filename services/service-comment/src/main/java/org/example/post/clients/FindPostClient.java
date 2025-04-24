package org.example.post.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-post")
public interface FindPostClient {
    @GetMapping("/post/search/check")
    int checkPostsById(@RequestParam("uuid") String uuid,@RequestParam("authorId") String authorId);
}
