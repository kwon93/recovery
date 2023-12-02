package com.blog.recovery.repository;

import com.blog.recovery.domain.Post;
import com.blog.recovery.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch search);
}
