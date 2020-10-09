package com.diletant.diletantmediatest.repository;

import com.diletant.diletantmediatest.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT * FROM post WHERE `tags` LIKE %:tag% OR `author` LIKE %:author%" , nativeQuery = true)
    Optional<List<Post>> findPostsByTagsAndAuthor(String tag, String author);

    @Query(value = "SELECT * FROM post WHERE `tags` LIKE %:keyword% OR `author` LIKE %:keyword% OR `title` LIKE %:keyword% OR `text` LIKE %:keyword%" , nativeQuery = true)
    Optional<List<Post>> searchPosts (String keyword);

    @Query("select p.tags from Post p")
    List<String> findTags();

    @Query(value = "SELECT * FROM post WHERE `tags` LIKE %:keyword%" , nativeQuery = true)
    Optional<List<Post>> searchPostsByTag (String keyword);

    @Query(value = "SELECT * FROM post WHERE `author` LIKE %:keyword%" , nativeQuery = true)
    Optional<List<Post>> searchPostsByAuthor (String keyword);

}
