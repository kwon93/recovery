package com.blog.recovery.request;

import com.blog.recovery.exception.InvalidRequest;
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

    public void validate(){
        if (this.content.contains("바보")){
            throw new InvalidRequest("content","내용에 누군가를 바보라고 하지마십시다.");
        }
    }
}
