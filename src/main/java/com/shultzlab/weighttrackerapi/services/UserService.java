package com.shultzlab.weighttrackerapi.services;

import com.shultzlab.weighttrackerapi.exceptions.ResourceNotFoundException;
import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findDistinctTopByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findDistinctTopByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return user;
    }
}
