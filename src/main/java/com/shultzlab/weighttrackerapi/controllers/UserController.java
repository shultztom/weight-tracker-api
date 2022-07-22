package com.shultzlab.weighttrackerapi.controllers;

import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String getAllUsers(){
        return "YO";
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return this.userRepository.save(user);
    }

}
