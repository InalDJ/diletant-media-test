package com.diletant.diletantmediatest.service;

import com.diletant.diletantmediatest.entity.Post;
import com.diletant.diletantmediatest.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostService postService;

    @Mock
    private PostRepository postRepository;

    private Post post1;
    private Post post2;
    private Post post3;
    private List<Post> listPosts;
    private Set<String> postTags;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        postService = new PostService(postRepository);

        postTags = Set.of("tag1", "tag2", "tag3");

        post1 = new Post();
        post1.setId(1L);
        post1.setTitle("title");
        post1.setText("text");
        post1.setAuthor("author");
        post1.setId(1L);
        post1.setPostTags(postTags);

        post2 = new Post();
        post2.setId(2L);
        post2.setTitle("title2");
        post2.setText("text2");
        post2.setAuthor("author2");
        post2.setId(2L);
        post2.setPostTags(postTags);

        post3 = new Post();
        post3.setId(3L);
        post3.setTitle("title3");
        post3.setText("text3");
        post3.setAuthor("author3");
        post3.setId(2L);
        post3.setPostTags(postTags);

        listPosts = List.of(post1, post2, post3);
    }

    @Test
    void findAllPosts() {
        when(postRepository.findAll()).thenReturn(listPosts);
        List<Post> allPosts = postService.findAllPosts();
        assertThat(allPosts).isNotEmpty();
    }

    @Test
    void getPost() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post1));

        Post foundPost = postService.getPost(post1.getId());

        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getAuthor()).isEqualTo(post1.getAuthor());
        assertThat(foundPost.getText()).isEqualTo(post1.getText());
        assertThat(foundPost.getTitle()).isEqualTo(post1.getTitle());
        assertThat(foundPost.getTags()).isEqualTo(post1.getTags());
    }

    @Test
    void getRecommendedPosts() {
        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post1));
        when(postRepository.findPostsByTagsAndAuthor(any(), any())).thenReturn(Optional.ofNullable(listPosts));
        Set<Post> recommendedPosts = postService.getRecommendedPosts(post1.getId());
        assertThat(recommendedPosts).isNotEmpty();
    }

    @Test
    void createPost() {
        when(postRepository.save(any())).thenReturn(post1);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        postRepository.save(post1);

        verify(postRepository).save(captor.capture());

        Post capturedPost = captor.getValue();

        assertThat(capturedPost.getAuthor()).isEqualTo(post1.getAuthor());
        assertThat(capturedPost.getText()).isEqualTo(post1.getText());
        assertThat(capturedPost.getTags()).isEqualTo(post1.getTags());
        assertThat(capturedPost.getTitle()).isEqualTo(post1.getTitle());
    }

    @Test
    void deletePost() {
        when(postRepository.findById(any())).thenReturn(null);
        doNothing().when(postRepository).deleteById(any());
        postService.deletePost(post1.getId());
        assertThat(postRepository.findById(post1.getId())).isNull();
    }

    @Test
    void updatePost() {
        when(postRepository.save(any())).thenReturn(post2);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        postRepository.save(post2);

        verify(postRepository).save(captor.capture());

        Post capturedPost = captor.getValue();

        assertThat(capturedPost.getAuthor()).isEqualTo(post2.getAuthor());
        assertThat(capturedPost.getText()).isEqualTo(post2.getText());
        assertThat(capturedPost.getTags()).isEqualTo(post2.getTags());
        assertThat(capturedPost.getTitle()).isEqualTo(post2.getTitle());
    }

    @Test
    void searchPosts() {
        when(postRepository.searchPosts(any())).thenReturn(Optional.of(listPosts));
        Set<Post> foundPosts = postService.searchPosts("title");
        assertThat(foundPosts).isNotEmpty();
    }
}