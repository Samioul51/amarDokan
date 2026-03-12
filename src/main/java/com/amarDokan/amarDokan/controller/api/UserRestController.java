package com.amarDokan.amarDokan.controller.api;

import com.amarDokan.amarDokan.models.User;
import com.amarDokan.amarDokan.service.UserService;
import com.amarDokan.amarDokan.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String role) {
        List<User> users = userService.getUsers(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) 
            throw new ResourceNotFoundException("User not found with email: " + email);
        
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.existsEmail(user.getEmail())) 
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateAccountStatus(@PathVariable Long id, @RequestParam Boolean status) {
        Boolean updated = userService.updateAccountStatus(id, status);
        if (!updated) 
            throw new ResourceNotFoundException("User not found with id: " + id);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
