package com.diletant.diletantmediatest.controller;


import com.diletant.diletantmediatest.entity.Post;
import com.diletant.diletantmediatest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/posts")
public class Controller {


    private PostService postService;

    @Autowired
    public Controller(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPost (@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }


    @GetMapping("/recommendedPosts/{postId}")
    public ResponseEntity<Set<Post>> getRecommendedPosts (@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getRecommendedPosts(postId));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Set<Post>> searchPosts (@PathVariable String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.searchPosts(keyword));
    }

    @GetMapping("/tags")
    public ResponseEntity<Set<String>> findTags () {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findtags());
    }

    @GetMapping("/searchByTag/{keyword}")
    public ResponseEntity<Set<Post>> searchPostsByTag (@PathVariable String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.searchPostsByTag(keyword));
    }

    @GetMapping("/searchByAuthor/{keyword}")
    public ResponseEntity<Set<Post>> searchPostsByAuthor (@PathVariable String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.searchPostsByAuthor(keyword));
    }



}
