package com.blog.recovery.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostResponse {

    private String title;
    private String content;

    @Builder
    public PostResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
