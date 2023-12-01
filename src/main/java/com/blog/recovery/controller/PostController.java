package com.blog.recovery.controller;


import com.blog.recovery.request.PostCreate;
import com.blog.recovery.response.PostResponse;
import com.blog.recovery.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService service;


    @PostMapping("/post")
    public PostResponse post(@RequestBody @Validated PostCreate requset){
        log.info("title = {} , content ={}",requset.getTitle(), requset.getContent());

        service.postSave(requset);

        PostResponse response = PostResponse.builder()
                .title(requset.getTitle())
                .content(requset.getContent())
                .build();

        return response;
    }

}
