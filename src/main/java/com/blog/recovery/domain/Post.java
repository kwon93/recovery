package com.blog.recovery.domain;

import com.blog.recovery.request.PostCreate;
import com.blog.recovery.request.PostEdit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;


    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public static Post of(PostCreate request){
        return Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    public void edit(PostEditor edit) {
        title = edit.getTitle();
        content = edit.getContent();
    }
}
