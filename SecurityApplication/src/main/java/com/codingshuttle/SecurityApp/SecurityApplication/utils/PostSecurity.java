package com.codingshuttle.SecurityApp.SecurityApplication.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.PostDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;

    public boolean isOwnerOfPost(Long postId) {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        PostDTO post = postService.getPostById(postId);

        return post.getAuthor()!= null
                && post.getAuthor().getId().equals(user.getId());
    }
}