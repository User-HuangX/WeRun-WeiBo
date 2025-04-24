package org.example.post.controller;

import org.example.common.model.global.BaseResult;
import org.example.post.pojo.vo.PostVO;
import org.example.post.service.SearchService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/post/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    @GetMapping("/keywords")
    public BaseResult<Object> searchPosts(String keyword, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        return BaseResult.success(searchService.searchPosts(keyword, page, pageSize));
    }
    @GetMapping("/tag")
    public BaseResult<Object> searchPostsByTag(String tagName, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        return BaseResult.success(searchService.searchPostsByTag(tagName, page, pageSize));
    }
    @GetMapping("/id")
    public PostVO searchPostsById(String uuid) {
        return searchService.searchPostsById(uuid);
    }
    @GetMapping("/check")
    public int checkPostsById(String uuid,String authorId) {
        return searchService.checkPostsById(uuid,authorId);
    }
}
