package com.blog.recovery.controller;


import com.blog.recovery.config.UserPrincipal;
import com.blog.recovery.config.data.UserSession;
import com.blog.recovery.domain.Post;
import com.blog.recovery.exception.InvalidRequest;
import com.blog.recovery.request.PostCreate;
import com.blog.recovery.request.PostEdit;
import com.blog.recovery.request.PostSearch;
import com.blog.recovery.response.PostResponse;
import com.blog.recovery.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService service;

    @GetMapping("/foo")
    public Long foo(UserSession userSession){
        log.info("userSession id = {}", userSession.getId());

        return userSession.getId();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public void post(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Validated PostCreate requset){
        requset.validate();
        service.postSave(requset, userPrincipal.getUserId());
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return service.read(postId);
    }

    @GetMapping("/post")
    public List<PostResponse> getList(PostSearch search){
        return service.getList(search);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/post/{postId}")
    public PostResponse update(@PathVariable Long postId, @RequestBody @Validated PostEdit postEdit){
        return service.update(postId, postEdit);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId,'POST', 'DELETE')")
    @DeleteMapping("/post/{postId}")
    public void delete(@PathVariable Long postId){
        service.delete(postId);
    }







}
