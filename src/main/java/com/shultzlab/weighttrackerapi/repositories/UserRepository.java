package com.shultzlab.weighttrackerapi.repositories;

import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.enums.ActivityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findDistinctTopByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE (:username IS NULL OR u.username = :username) AND (:age = -1 OR u.age = :age) AND (:activityLevel IS NULL OR u.activityLevel = :activityLevel)")
    List<User> searchAllUsers(@Param("username") String username,@Param("age") int age, @Param("activityLevel") ActivityLevel activityLevel);
}
