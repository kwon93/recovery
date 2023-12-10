package com.blog.recovery.controller;

import com.blog.recovery.config.RecoveryMockUser;
import com.blog.recovery.domain.Post;
import com.blog.recovery.domain.Users;
import com.blog.recovery.repository.PostRepository;
import com.blog.recovery.repository.UserRepository;
import com.blog.recovery.request.PostCreate;
import com.blog.recovery.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @WithMockUser(username = "kwon93@naver.com", roles = {"ADMIN"}, password = "k1234")
    @DisplayName("/post 요청시 content값이 없을경우 검증 후 에러 메시지를 응답해야한다.")
    void post1() throws Exception {
        //given
        PostCreate postDTO = PostCreate.builder()
                .title("제목입니다~")
                .build();

        //when, then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/post")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.errorMessage").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.content").value("내용을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @RecoveryMockUser()
    @DisplayName("/post 요청시 DB에 요청한 정보가 저장되어야한다.")
    void post2() throws Exception {
        //given
        PostCreate postDTO = PostCreate.builder()
                .title("제목입니다~")
                .content("내용입니다잉~~")
                .build();

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/post")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        assertThat(postRepository.count()).isEqualTo(1);
    }


    @Test
    @WithMockUser(username = "kwon93@naver.com", roles = {"ADMIN"}, password = "k1234")
    @DisplayName("사용자의 글 단건조회 요청에 성공해야한다.")
    void get() throws Exception {
        //given
        String title1 = "foo1";
        String content1 = "bar";

        Post request1 = Post.builder()
                .title(title1)
                .content(content1)
                .build();

        Post request = postRepository.save(request1);

        // when , then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/post/{postId}",request.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(request1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(title1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(content1))
                .andDo(MockMvcResultHandlers.print());

    }



    @Test
    @WithMockUser(username = "kwon93@naver.com", roles = {"ADMIN"}, password = "k1234")
    @DisplayName("사용자 요청에 의해 1페이지 조회 응답에 성공해야한다.")
    void test() throws Exception {
        //given
        List<Post> requestPost = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .toList();

        postRepository.saveAll(requestPost);

        // when , then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/post?page=1&size=10")
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("제목 30"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("사용자가 페이지 요청을 0으로해도 첫페이지를 가져온다.")
    void paging() throws Exception {
        //given
        List<Post> requestPost = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .toList();

        postRepository.saveAll(requestPost);

        // when , then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/post?page=0&size=10")
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("제목 30"))
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    @WithMockUser(username = "kwon93@naver.com", roles = {"ADMIN"}, password = "k1234")
    @DisplayName("사용자의 글 수정 요청에 응답해야한다.")
    void update() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEdit editedPost = PostEdit.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when , then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/post/{postId}",post.getId())
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editedPost))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @RecoveryMockUser
    @DisplayName("사용자의 글 삭제 요청에 HTTP 200 code로 응답해야한다.")
    void delete() throws Exception {
        //given
        Users user = Users.builder()
                .id(1L)
                .email("kwon93@naver.com")
                .name("kwon")
                .password("k1234")
                .build();

        Users savedUser = userRepository.save(user);

        Post post = Post.builder()
                .user(savedUser)
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);


        // when , then

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/post/{postId}",post.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    @WithMockUser(roles = {"USER"})
    @DisplayName("존재하지않는 게시글 조회")
    void exceptionTest() throws Exception {
        //given

        // when then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/post/{postId}",1L)
                                .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "kwon93@naver.com", roles = {"ADMIN"}, password = "k1234")
    @DisplayName("존재하지않는 게시글 수정")
    void exceptionTest2() throws Exception {
        //given
        PostEdit editedPost = PostEdit.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/post/{postId}",999L)
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(editedPost))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @RecoveryMockUser
    @DisplayName("존재하지않는 게시글 삭제")
    void exceptionTest3() throws Exception {
        //given

        // when then
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/post/{postId}",999L)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "kwon93@naver.com", roles = {"ADMIN"}, password = "k1234")
    @DisplayName("게시물 작성 요청시 내용에 '바보'는 포함 될 수 없다.")
    void post3() throws Exception {
        //given
        PostCreate postDTO = PostCreate.builder()
                .title("하고싶은 말이 있습니다.")
                .content("너는 바보입니다.")
                .build();

        //expected
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/post")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postDTO))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }



}