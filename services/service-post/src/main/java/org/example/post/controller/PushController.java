package org.example.post.controller;

import org.example.common.model.global.BaseResult;
import org.example.post.service.PushService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RequestMapping(value = "/post/push")
@RestController
public class PushController {

    private final PushService pushService;

    public PushController(PushService pushService) {
        this.pushService = pushService;
    }
    @RequestMapping(value = "/pushPosts")
    public BaseResult<Object> pushPosts(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer pageSize) throws ExecutionException, InterruptedException {
        return BaseResult.success(pushService.pushPosts(page, pageSize).get());
    }
    @RequestMapping(value = "/pushPostsByCommentCount")
    public BaseResult<Object> pushPostsByCommentCount(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer pageSize) throws ExecutionException, InterruptedException {
        return BaseResult.success(pushService.pushPostsByCommentCount(page, pageSize).get());
    }
}
