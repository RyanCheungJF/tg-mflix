package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.model.User;
import com.tigergraph.tg.repository.CommentRepository;
import com.tigergraph.tg.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public UserService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public User getUserById(String id) {
        return userRepository.getUserById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers().orElse(null);
    }

    public User addUser(User newUser) {
        return userRepository.addUser(newUser).orElse(null);
    }

    public String deleteUser(String id) {
        return userRepository.deleteUser(id).orElse("Failed to delete any vertex");
    }

    /**
    public List<Movie> getUserOneHop(String id) {
        return userRepository.getUserOneHop(id).orElse(null);
    }

    public List<User> getNeighbouringUsers(String id) {
        return userRepository.getNeighbouringUsers(id).orElse(null);
    }
    */

    public List<Object> getKHops(String id, Integer hops) {
        return userRepository.getKHops(id, hops).orElse(null);
    }

    public List<Comment> getCommentsByUser(String id) {
        return commentRepository.getCommentsByUser(id).orElse(null);
    }

    public List<Object> matchUsersByNumberOfComments(Integer num) {
        return userRepository.matchUsersByNumberOfComments(num).orElse(null);
    }
}
