package com.diletant.diletantmediatest.entity;

import com.diletant.diletantmediatest.util.StringListConverter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Post {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String title;

    @Column(length = 5000)
    @NotNull
    @Lob
    private String text;

    @Column
    @NotNull
    private String author;

    @Column
    @Convert(converter = StringListConverter.class)
    Set<String> tags = new HashSet<>();

    public Post(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setPostTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
