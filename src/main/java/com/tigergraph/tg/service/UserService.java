package com.tigergraph.tg.service;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.model.User;
import com.tigergraph.tg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
        return userRepository.getCommentsByUser(id).orElse(null);
    }

    public List<Object> matchUsersByNumberOfComments(Integer num) {
        return userRepository.matchUsersByNumberOfComments(num).orElse(null);
    }

    // returns in order of:
    // vertex id, password, name, id, email
    public static User reconstructUser(java.sql.ResultSet rs) throws SQLException {
        return new User(rs.getString(4), rs.getString(2), rs.getString(3), rs.getString(5));
    }
}
