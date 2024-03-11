package com.bsuir.service;

import com.bsuir.dto.RatingRequest;
import com.bsuir.entity.Rating;
import com.bsuir.exception.DuplicateRatingException;
import com.bsuir.exception.RatingNotFoundException;
import com.bsuir.mapper.RatingMapper;
import com.bsuir.repository.RatingRepository;
import com.bsuir.util.PageSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    public Page<Rating> getAllRatingByUserId(String userId, PageSetting pageSetting) {
        Pageable pageable = PageRequest.of(pageSetting.getPageNumber(), pageSetting.getElementsPerPage());
        return ratingRepository.findAllByToUser(userId, pageable);
    }

    public Rating save(RatingRequest ratingRequest) {
        Rating rating = ratingMapper.toEntity(ratingRequest);
        if(ratingRepository.existsByFromUserAndToUser(rating.getFromUser(), ratingRequest.getToUser())) {
            throw new DuplicateRatingException(rating.getFromUser(), ratingRequest.getToUser());
        }

        return ratingRepository.save(rating);
    }

    public void delete(long ratingId) {
        if(!ratingRepository.existsById(ratingId)) {
            throw new RatingNotFoundException(ratingId);
        }

        ratingRepository.deleteById(ratingId);
    }
}