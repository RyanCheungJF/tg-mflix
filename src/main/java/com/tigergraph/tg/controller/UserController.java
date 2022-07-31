package com.tigergraph.tg.controller;

import com.tigergraph.tg.model.edge.CommentsOn;
import com.tigergraph.tg.model.vertex.User;
import com.tigergraph.tg.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Gets a user by their unqiue id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found user",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = User.class))}),
        @ApiResponse(responseCode = "404", description = "User does not exist",
            content = @Content)
    })
    @GetMapping(value = "/user/{id}")
    public User getUserById(@Parameter(description = "User id", required = true)
                                @PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Gets all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found all users",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = User.class))}),
        @ApiResponse(responseCode = "404", description = "Not a single user exists",
            content = @Content)
    })
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Adds a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully added",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = User.class))}),
        @ApiResponse(responseCode = "404", description = "Failed to add user",
            content = @Content)
    })
    @PostMapping("/user")
    public User addUser(@RequestBody User newUser) {
        return userService.addUser(newUser);
    }

    @Operation(summary = "Delete a pre-existing user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful delete",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = User.class))}),
        @ApiResponse(responseCode = "404", description = "Failed to delete with specified id",
            content = @Content)
    })
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<String> deleteUser(@Parameter(description = "User id", required = true)
                                                 @PathVariable("id") String id) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "Gets all type of nodes that are exactly k hops away from source")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully got all nodes k hops away",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Failed to get nodes",
            content = @Content)
    })
    @GetMapping(value = "/khop", params = {"id", "hops"})
    public List<Object> kHops(@Parameter(description = "User's id to query for", required = true)
                                  @RequestParam String id,
                              @Parameter(description = "Number of hops", required = true)
                                @RequestParam Integer hops) {
        return userService.getKHops(id, hops);
    }

    @Operation(summary = "Gets all comments made by a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully got all comments",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = CommentsOn.class))}),
        @ApiResponse(responseCode = "404", description = "Failed to get comments",
            content = @Content)
    })
    @GetMapping(value = "/comments", params = "id")
    public List<CommentsOn> commentsByUser(@Parameter(description = "User id", required = true)
                                               @RequestParam String id) {
        return userService.getCommentsByUser(id);
    }

    @Operation(summary = "Gets all users who posted k comments, and returns the users and their comments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully got all users and comments",
            content = {@Content(mediaType = "application/json")}),
        @ApiResponse(responseCode = "404", description = "Failed to get any users or comments",
            content = @Content)
    })
    @GetMapping(value = "/match-comment", params = "num")
    public List<Object> matchUsersByNumberOfComments(@Parameter(description = "Number of comments", required = true)
                                                         @RequestParam Integer num) {
        return userService.matchUsersByNumberOfComments(num);
    }
}
