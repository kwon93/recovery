package com.blog.recovery.service;

import com.blog.recovery.domain.Post;
import com.blog.recovery.repository.PostRepository;
import com.blog.recovery.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void postSave(PostCreate request){
        Post postEntity = Post.of(request);
        postRepository.save(postEntity);
    }
}
