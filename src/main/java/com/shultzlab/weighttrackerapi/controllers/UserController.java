package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // Commented out as client won't use these routes
//    @GetMapping()
//    public List<User> getAllUsers(){
//        return this.userRepository.findAll();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
//        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//        // TODO ensure username matches token
//        return ResponseEntity.ok().body(user);
//    }

    @PostMapping
    public User createUser(@RequestBody User user, @RequestHeader("x-auth-token") String token) throws TokenForbiddenException {
        String tokenUser = TokenService.getUsernameFromToken(token);
        if(!user.getUsername().equals(tokenUser)) {
            throw new TokenForbiddenException();
        }
        return this.userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userRequest, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String tokenUser = TokenService.getUsernameFromToken(token);
        if(!user.getUsername().equals(tokenUser)) {
            throw new TokenForbiddenException();
        }

        user.setUsername(userRequest.getUsername());
        user.setHeight(userRequest.getHeight());
        user.setBirthday(userRequest.getBirthday());
        user.setActivityLevel(userRequest.getActivityLevel());
        user.setGender(userRequest.getGender());
        return this.userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId, @RequestHeader("x-auth-token") String token) throws ResourceNotFoundException, TokenForbiddenException {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String tokenUser = TokenService.getUsernameFromToken(token);
        if(!user.getUsername().equals(tokenUser)) {
            throw new TokenForbiddenException();
        }

        this.userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
