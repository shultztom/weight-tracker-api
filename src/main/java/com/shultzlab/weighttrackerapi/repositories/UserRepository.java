package com.shultzlab.weighttrackerapi.repositories;

import com.shultzlab.weighttrackerapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
