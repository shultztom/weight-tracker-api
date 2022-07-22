package com.shultzlab.weighttrackerapi.repositories;

import com.shultzlab.weighttrackerapi.models.WeightEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightEntryRepository extends JpaRepository<WeightEntry, Long> {
}
