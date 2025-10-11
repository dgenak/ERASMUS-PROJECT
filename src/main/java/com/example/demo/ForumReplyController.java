package com.example.demo;

import com.example.demo.dto.ForumReplyDto;
import com.example.demo.model.ForumPost;
import com.example.demo.model.ForumReply;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;            // <-- πρόσθεσε αυτό
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // ή @RestController
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller // ή @RestController
@RequestMapping("/forum/reply")
public class ForumReplyController {

    private final ForumReplyRepository forumReplyRepository;
    private final ForumPostRepository forumPostRepository;

    public ForumReplyController(ForumReplyRepository forumReplyRepository,
                                ForumPostRepository forumPostRepository) {
        this.forumReplyRepository = forumReplyRepository;
        this.forumPostRepository = forumPostRepository;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<ForumReply> addReply(@Valid @RequestBody ForumReplyDto dto, // <-- πρόσθεσε @Valid
                                               HttpSession session) {
        // 1) Εύρεση του ForumPost με βάση το postId από το DTO
        ForumPost post = forumPostRepository.findById(dto.getPostId()).orElse(null);
        if (post == null) {
            return ResponseEntity.badRequest().build();
        }

        // 2) Απόκτηση του username από το session ή χρήση "Anonymous"
        String username = (String) session.getAttribute("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Anonymous";
        }

        // 3) Δημιουργία και αποθήκευση του ForumReply
        ForumReply reply = new ForumReply();
        reply.setUsername(username);
        reply.setReply(dto.getReply());
        reply.setTimestamp(LocalDateTime.now());
        reply.setForumPost(post);

        ForumReply savedReply = forumReplyRepository.save(reply);

        // 4) Επιστροφή του αποθηκευμένου reply σε JSON μορφή
        return ResponseEntity.ok(savedReply);
    }

    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<ForumReply> likeReply(@RequestParam("replyId") Long replyId) {
        ForumReply reply = forumReplyRepository.findById(replyId).orElse(null);
        if (reply == null) {
            return ResponseEntity.notFound().build();
        }
        reply.setLikeCount(reply.getLikeCount() + 1);
        ForumReply updatedReply = forumReplyRepository.save(reply);
        return ResponseEntity.ok(updatedReply);
    }
}
