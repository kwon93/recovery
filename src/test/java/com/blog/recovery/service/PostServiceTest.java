package com.blog.recovery.service;

import com.blog.recovery.domain.Post;
import com.blog.recovery.repository.PostRepository;
import com.blog.recovery.request.PostCreate;
import com.blog.recovery.request.PostSearch;
import com.blog.recovery.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {


    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAllInBatch();
    }

    @Test
    @DisplayName("블로그 글 작성이 정상적으로 되어야한다.")
    void postSave1() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();


        // when
        postService.postSave(request);


        //then
        assertThat(repository.count()).isEqualTo(1);
        Post post = repository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }



    @Test
    @DisplayName("글의 단건조회가 정상적으로 조회되어야 한다.")
    void read() throws Exception {
        //given
        String title1 = "foo";
        String content1 = "bar";

        Post request = Post.builder()
                .title(title1)
                .content(content1)
                .build();

        Post savedPost = repository.save(request);

        // when
        PostResponse post = postService.read(savedPost.getId());
        //then
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo(title1);
        assertThat(post.getContent()).isEqualTo(content1);

    }

    @Test
    @DisplayName("글 1페이지 조회에 성공해야한다.")
    void getList() throws Exception {
        //given
        List<Post> requestPost = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());


        repository.saveAll(requestPost);

        PostSearch search = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> posts = postService.getList(search);

        //then
        assertThat(posts.size()).isEqualTo(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목 30");
        assertThat(posts.get(4).getTitle()).isEqualTo("제목 26");

    }


}