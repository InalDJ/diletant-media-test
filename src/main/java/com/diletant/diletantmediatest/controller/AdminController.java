package com.diletant.diletantmediatest.controller;

import com.diletant.diletantmediatest.dto.PostRequest;
import com.diletant.diletantmediatest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/admin")
public class AdminController {

    private PostService postService;

    @Autowired
    public AdminController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity createPost(@RequestBody PostRequest postRequest){
        postService.createPost(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity updatePost(@RequestBody PostRequest postRequest){
        postService.updatePost(postRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId){
        System.out.println(postId);
        postService.deletePost(postId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
