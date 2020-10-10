package com.diletant.diletantmediatest.service;

import com.diletant.diletantmediatest.dto.PostRequest;
import com.diletant.diletantmediatest.entity.Post;
import com.diletant.diletantmediatest.exception.DiletantMediaException;
import com.diletant.diletantmediatest.exception.ResourceNotFoundException;
import com.diletant.diletantmediatest.repository.PostRepository;
import com.diletant.diletantmediatest.util.StringListConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public List<Post> findAllPosts(){
        return postRepository.findAll();
    }

    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The post has not been found"));
    }

    public Set<Post> getRecommendedPosts(Long  postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        //extract tags from a post
        Set<String> tags = post.getTags();

        //store retrieved posts in a hashSet to avoid repetition
        Set<Post> recommendedPosts = new HashSet<>();

        for(var tag: tags){
            List<Post> postsByTags = postRepository.findPostsByTagsAndAuthor(tag, post.getAuthor()).orElseThrow(() -> new ResourceNotFoundException("No posts to recommend"));
            recommendedPosts.addAll(postsByTags);
        }

        //remove the original post to avoid recommending the same post which user is reading
        recommendedPosts.remove(post);

        return recommendedPosts;
    }

    public Post createPost(PostRequest postRequest){
        return postRepository.save(mapDtoToEntity(postRequest));
    }


    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public Post updatePost(PostRequest postRequest) {
        Post existingPost = mapDtoToEntity(postRequest);
        return postRepository.save(existingPost);
    }


    private Post mapDtoToEntity(PostRequest postRequest){
        //i decided not to use Mapstruct to save some time and because i find the structure too simple to use mapstruct
        Post post;

        if(postRequest.getId() == null) {
            post = new Post();
        } else {
            post = postRepository.findById(postRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        }

        post.setAuthor(postRequest.getAuthor());
        post.setPostTags(postRequest.getTags().stream().map(String::toLowerCase).collect(Collectors.toSet()));
        post.setText(postRequest.getText());
        post.setTitle(postRequest.getTitle());

        return post;
    }

    public Set<Post> searchPosts(String keyword) {
        if(keyword == null){
            throw new DiletantMediaException("Empty keyword");
        }
        List<Post> retrievedPosts = postRepository.searchPosts(keyword).orElseThrow(() -> new ResourceNotFoundException("No posts in the database"));
        return new HashSet<>(retrievedPosts);
    }

    public Set<Post> searchPostsByAuthor(String keyword) {
        List<Post> retrievedPosts = postRepository.searchPostsByAuthor(keyword).orElseThrow(() -> new ResourceNotFoundException("No posts in the database"));
        return new HashSet<>(retrievedPosts);
    }

    public Set<Post> searchPostsByTag(String keyword) {
        List<Post> retrievedPosts = postRepository.searchPostsByTag(keyword).orElseThrow(() -> new ResourceNotFoundException("No posts in the database"));
        return new HashSet<>(retrievedPosts);
    }

    public Set<String> findtags() {
        StringListConverter converter = new StringListConverter();
        List<String> retrievedTags = postRepository.findTags();
        String joined = String.join(",", retrievedTags);
        return converter.convertToEntityAttribute(joined);
    }

}
