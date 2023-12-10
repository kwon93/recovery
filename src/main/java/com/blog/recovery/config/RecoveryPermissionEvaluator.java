package com.blog.recovery.config;

import com.blog.recovery.domain.Post;
import com.blog.recovery.exception.PostNotFound;
import com.blog.recovery.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
@Slf4j
public class RecoveryPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Post post = postRepository.findById((Long) targetId).orElseThrow(PostNotFound::new);

        if (!post.getUserId().equals(principal.getUserId())){
            log.error("[인가 실패] 해당 사용자가 작성한 글이 아닙니다.");
            return false;
        }

        return true;
    }
}
