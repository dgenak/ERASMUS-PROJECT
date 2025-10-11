package com.example.demo;

import com.example.demo.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUsernameAndPostId(String username, Long postId);
    PostLike findByUsernameAndPostId(String username, Long postId);
}
