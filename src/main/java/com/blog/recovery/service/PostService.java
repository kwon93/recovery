package com.blog.recovery.service;

import com.blog.recovery.config.UserPrincipal;
import com.blog.recovery.domain.Post;
import com.blog.recovery.domain.PostEditor;
import com.blog.recovery.domain.Users;
import com.blog.recovery.exception.PostNotFound;
import com.blog.recovery.exception.UserNotFound;
import com.blog.recovery.repository.PostRepository;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.PostCreate;
import com.blog.recovery.request.PostEdit;
import com.blog.recovery.request.PostSearch;
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
    private final UserRepository userRepository;

    public void postSave(PostCreate request, Long userId){
        Users users = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        Post postEntity = Post.of(request, users);
        postRepository.save(postEntity);
    }

    public PostResponse read(Long id) {
        Post post = searchPostByPostId(id);
        return PostResponse.of(post);

    }

    public List<PostResponse> getList(PostSearch search) {

        return postRepository.getList(search).stream()
                .map(PostResponse::of)
                .toList();
    }

    @Transactional
    public PostResponse update(Long id, PostEdit postEdit){
        Post post = searchPostByPostId(id);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor edit = editorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(edit);

        return PostResponse.of(post);
    }
    @Transactional
    public void delete(Long id) {
        Post post = searchPostByPostId(id);
        postRepository.delete(post);
    }

    private Post searchPostByPostId(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
    }

}
