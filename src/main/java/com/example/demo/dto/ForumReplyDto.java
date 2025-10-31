package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ForumReplyDto {

    @NotNull(message = "Το postId είναι υποχρεωτικό.")
    private Long postId;

    @NotBlank(message = "Το κείμενο της απάντησης είναι υποχρεωτικό.")
    private String reply;

    // Getters & Setters
    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
    public String getReply() {
        return reply;
    }
    public void setReply(String reply) {
        this.reply = reply;
    }
}
