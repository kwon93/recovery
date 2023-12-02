package com.blog.recovery.response;

import com.blog.recovery.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    @Builder
    public PostResponse(Long id,String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static PostResponse of(Post entity){
        return PostResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }
}
