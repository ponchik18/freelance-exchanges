package com.bsuir.repository;

import com.bsuir.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findAllByToUser(String toUser, Pageable pageable);
    Boolean existsByFromUserAndToUser(String fromUser, String toUser);
}