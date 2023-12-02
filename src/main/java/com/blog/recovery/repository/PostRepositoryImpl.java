package com.blog.recovery.repository;

import com.blog.recovery.domain.Post;
import com.blog.recovery.domain.QPost;
import com.blog.recovery.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch search) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(search.getSize())
                .offset(search.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
