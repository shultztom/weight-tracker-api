package com.shultzlab.weighttrackerapi.repositories;

import com.shultzlab.weighttrackerapi.models.User;
import com.shultzlab.weighttrackerapi.models.WeightEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightEntryRepository extends JpaRepository<WeightEntry, Long> {
    @Query(value = "FROM WeightEntry WHERE user.id = ?1 ORDER BY createdAt DESC")
    List<WeightEntry> findAllByUserId(Long userId);

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.limit-query-result
    WeightEntry findFirstByUser(User user);
}
