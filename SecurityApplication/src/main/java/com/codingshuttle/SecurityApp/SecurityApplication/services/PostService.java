package com.codingshuttle.SecurityApp.SecurityApplication.services;

import java.util.List;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.PostDTO;


public interface PostService {

	List<PostDTO> getAllPosts();
	
	PostDTO createNewPost(PostDTO inputPost);

	PostDTO getPostById(Long postId);

	PostDTO updatePost(PostDTO inputPost, Long postId);
}
