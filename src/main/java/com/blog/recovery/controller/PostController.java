package com.blog.recovery.controller;


import com.blog.recovery.domain.Post;
import com.blog.recovery.request.PostCreate;
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
    public PostResponse post(@RequestBody @Validated PostCreate requset){
        log.info("title = {} , content ={}",requset.getTitle(), requset.getContent());

        service.postSave(requset);

        return PostResponse.builder()
                .title(requset.getTitle())
                .content(requset.getContent())
                .build();
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return service.read(postId);
    }

    @GetMapping("/post")
    public List<PostResponse> getList(@PageableDefault(size = 10) Pageable page){
        return service.getList(page);
    }



}
