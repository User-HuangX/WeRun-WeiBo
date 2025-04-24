package org.example.post.controller;

import org.example.common.model.global.BaseResult;
import org.example.post.service.UserPostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/userpost")
public class UserPostController {

    private final UserPostService userPostService;

    public UserPostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @RequestMapping("/getUserPosts")
    public BaseResult<Object> getUserPosts(String authid, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        return BaseResult.success(userPostService.getUserPosts(authid, page, pageSize));
    }

}
