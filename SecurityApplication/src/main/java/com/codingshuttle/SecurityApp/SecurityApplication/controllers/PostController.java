package com.codingshuttle.SecurityApp.SecurityApplication.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.PostDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.services.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	
	@GetMapping
	public List<PostDTO> getAllPosts(){
		return postService.getAllPosts();
	}
	
	@GetMapping("/{postId:\\d+}")
	public PostDTO getPostById(@PathVariable Long postId) {
		return postService.getPostById(postId);
	}
	
	@PostMapping
	public PostDTO createNewPost(@RequestBody PostDTO input) {
		return postService.createNewPost(input);
	}
	
	@PutMapping("{postId}")
	public PostDTO updatePost(@RequestBody PostDTO inputPost, @PathVariable Long postId)
	{
		return postService.updatePost(inputPost, postId);
	}
	
	@GetMapping("/isAlive")
	public String isAlive() {
		return "isAlive";
	}
}
