package com.blog.recovery.service;

import com.blog.recovery.domain.Post;
import com.blog.recovery.repository.PostRepository;
import com.blog.recovery.request.PostCreate;
import com.blog.recovery.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public PostResponse read(Long id) {
        Post post = searchPostByPostId(id);
        return PostResponse.of(post);

    }

    private Post searchPostByPostId(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 글입니다.")
        );
        return post;
    }

    public List<PostResponse> getList(Pageable pageable) {

        return postRepository.findAll(pageable).stream()
                .map(PostResponse::of)
                .toList();
    }
}
