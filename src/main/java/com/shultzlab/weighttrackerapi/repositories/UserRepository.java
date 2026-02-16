package com.shultzlab.weighttrackerapi.repositories;

import com.shultzlab.weighttrackerapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findDistinctTopByUsername(String username);

    Optional<User> findByUsername(String username);
}
