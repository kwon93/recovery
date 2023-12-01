package com.blog.recovery.request;

import jakarta.validation.constraints.NotBlank;
import jdk.jfr.StackTrace;
import lombok.*;

@Getter
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
