package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "forum_replies")
public class ForumReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    
    @Column(length = 2000)
    private String reply;

    private LocalDateTime timestamp;
    
    // Αρχική τιμή likeCount = 0
    private int likeCount = 0;

    @ManyToOne
    @JoinColumn(name = "forum_post_id")
    @JsonBackReference
    private ForumPost forumPost;

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
  
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
  
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
  
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
  
    public ForumPost getForumPost() {
        return forumPost;
    }
    public void setForumPost(ForumPost forumPost) {
        this.forumPost = forumPost;
    }
}
