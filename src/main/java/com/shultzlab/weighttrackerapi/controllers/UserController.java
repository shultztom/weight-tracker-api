package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.exceptions.TokenForbiddenException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.enums.ActivityLevel;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import com.shultzlab.weighttrackerapi.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping()
    public List<User> searchAllUsers(@RequestParam(required = false) String username, @RequestParam(required = false) String age, @RequestParam(required = false) ActivityLevel activityLevel) {
        if(username == null &&
            age == null &&
            activityLevel == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing at least one param to search by");
        }

        // Parse age to int
        int ageValue = -1;
        if(age != null){
            ageValue = Integer.parseInt(age);
        }

        return this.userRepository.searchAllUsers(username, ageValue, activityLevel);
    }

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
