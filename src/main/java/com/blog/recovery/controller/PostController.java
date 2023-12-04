package com.blog.recovery.controller;


import com.blog.recovery.domain.Post;
import com.blog.recovery.exception.InvalidRequest;
import com.blog.recovery.request.PostCreate;
import com.blog.recovery.request.PostEdit;
import com.blog.recovery.request.PostSearch;
import com.blog.recovery.response.PostResponse;
import com.blog.recovery.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService service;


    @PostMapping("/post")
    public void post(@RequestBody @Validated PostCreate requset){
        requset.validate();
        service.postSave(requset);
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return service.read(postId);
    }

    @GetMapping("/post")
    public List<PostResponse> getList(PostSearch search){
        return service.getList(search);
    }

    @PatchMapping("/post/{postId}")
    public PostResponse update(@PathVariable Long postId, @RequestBody @Validated PostEdit postEdit){
        return service.update(postId, postEdit);
    }

    @DeleteMapping("/post/{postId}")
    public void delete(@PathVariable Long postId){
        service.delete(postId);
    }







}
