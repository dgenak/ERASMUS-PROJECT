package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "post_likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "post_id"})})
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "post_id")
    private Long postId;

    public PostLike() {
    }

    public PostLike(String username, Long postId) {
        this.username = username;
        this.postId = postId;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
