package com.example.demo;

import com.example.demo.dto.ForumPostDto;
import com.example.demo.model.ForumPost;
import com.example.demo.PostLike;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/forum")
public class ForumPostsController {

    private final ForumPostRepository forumPostRepository;
    private final PostLikeRepository postLikeRepository; // Repository για τα Likes
    private static final AtomicLong idGenerator = new AtomicLong();

    public ForumPostsController(ForumPostRepository forumPostRepository, PostLikeRepository postLikeRepository) {
        this.forumPostRepository = forumPostRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @GetMapping
    public String showForum(Model model) {
        List<ForumPost> posts = forumPostRepository.findAll();
        model.addAttribute("posts", posts);
        // Δέσμευση μιας νέας φόρμας για δημιουργία νέου post
        model.addAttribute("forumPostDto", new ForumPostDto());
        return "forum";
    }

    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("forumPostDto") ForumPostDto forumPostDto,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {
        if (bindingResult.hasErrors()) {
            List<ForumPost> posts = forumPostRepository.findAll();
            model.addAttribute("posts", posts);
            return "forum";
        }

        String username = (String) session.getAttribute("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Anonymous";
        }

        ForumPost post = new ForumPost();
        post.setId(idGenerator.incrementAndGet());
        post.setUsername(username);
        post.setType(forumPostDto.getType());
        if ("question".equalsIgnoreCase(forumPostDto.getType())) {
            post.setTitle(forumPostDto.getTitle());
            post.setBody(forumPostDto.getBody());
        } else if ("experience".equalsIgnoreCase(forumPostDto.getType())) {
            post.setDepartment(forumPostDto.getDepartment());
            post.setUniversity(forumPostDto.getUniversity());
            post.setExperience(forumPostDto.getExperience());
        }
        post.setTimestamp(LocalDateTime.now());
        post.setLikeCount(0);

        forumPostRepository.save(post);
        return "redirect:/forum";
    }

    @PostMapping("/like")
    public String likePost(@RequestParam("postId") Long postId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Anonymous";
        }

        // Έλεγχος αν ο χρήστης έχει ήδη κάνει like στο συγκεκριμένο post
        boolean alreadyLiked = postLikeRepository.existsByUsernameAndPostId(username, postId);
        if (alreadyLiked) {
            // Επιστρέφουμε χωρίς αλλαγή ή μπορούμε να προσθέσουμε παράμετρο για error
            return "redirect:/forum?error=already-liked";
        }

        ForumPost post = forumPostRepository.findById(postId).orElse(null);
        if (post != null) {
            // Δημιουργούμε εγγραφή Like για το συγκεκριμένο (username, postId)
            PostLike newLike = new PostLike(username, postId);
            postLikeRepository.save(newLike);

            // Αυξάνουμε το likeCount και αποθηκεύουμε το post
            post.setLikeCount(post.getLikeCount() + 1);
            forumPostRepository.save(post);
        }
        return "redirect:/forum";
    }
    @PostMapping("/unlike")
    public String unlikePost(@RequestParam("postId") Long postId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null || username.trim().isEmpty()) {
            username = "Anonymous";
        }

        // Βρίσκουμε το ForumPost
        ForumPost post = forumPostRepository.findById(postId).orElse(null);
        if (post != null) {
            // Ελέγχουμε αν υπάρχει ήδη εγγραφή στο postLikeRepository για αυτό το (username, postId)
            boolean alreadyLiked = postLikeRepository.existsByUsernameAndPostId(username, postId);
            if (alreadyLiked) {
                // Βρίσκουμε το PostLike object
                PostLike existingLike = postLikeRepository.findByUsernameAndPostId(username, postId);
                // Το διαγράφουμε
                postLikeRepository.delete(existingLike);

                // Μειώνουμε το likeCount στο ForumPost
                post.setLikeCount(post.getLikeCount() - 1);
                forumPostRepository.save(post);
            }
        }

        // Επιστρέφουμε σε κάποια σελίδα ή κάνουμε redirect
        return "redirect:/forum";
    }

}
