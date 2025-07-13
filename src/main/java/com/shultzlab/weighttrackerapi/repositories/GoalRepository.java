package com.shultzlab.weighttrackerapi.repositories;


import com.shultzlab.weighttrackerapi.models.Goal;
import com.shultzlab.weighttrackerapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUser(User user);

    Goal findDistinctFirstByUserOrderByCreatedAtDesc(User user);
}
