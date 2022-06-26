package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.Comment;
import com.tigergraph.tg.model.User;
import com.tigergraph.tg.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // crud for vertex

    // simple GET
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    // simple GET, to get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // simple POST, no UPDATE as tg treats them the same, as an 'UPSERT' query
    @PostMapping("/user")
    public User addUser(@RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    // simple DELETE
    @DeleteMapping("/user/{id}")
    public Map<String, String> deleteUser(@PathVariable("id") String id) {
        return Collections.singletonMap("response", userService.deleteUser(id));
    }

    /**
    // these were test queries as a starting point to build khop,
    // I'll just keep it for now if I need to refer to the syntax
    // gets 1 hop neighbours, which in our graph are only movies directly available
    @GetMapping("/onehop/{id}")
    public List<Movie> userOneHop(@PathVariable("id") String id) {
        return userService.getUserOneHop(id);
    }

    // 2 hop query, cross domain, building upon 1 hop
    @GetMapping("/neighbourusers/{id}")
    public List<User> neighbouringUsers(@PathVariable("id") String id) {
        return userService.getNeighbouringUsers(id);
    }
    */

    // khop query, returns any node that is k hops away from user
    @GetMapping("/khop/{id}/{hops}")
    public List<Object> kHops(@PathVariable("id") String id, @PathVariable("hops") Integer hops) {
        return userService.getKHops(id, hops);
    }

    // gets all comments made by the user, done with accumulator syntax
    @GetMapping("/comments/{id}")
    public List<Comment> commentsByUser(@PathVariable("id") String id) {
        return userService.getCommentsByUser(id);
    }

    // gets all users who posted n number of comments, returns the users and their comments
    // builds upon commentsByUser with accumulator
    @GetMapping("/matchcomment/{num}")
    public List<Object> matchUsersByNumberOfComments(@PathVariable("num") Integer num) {
        return userService.matchUsersByNumberOfComments(num);
    }
}
