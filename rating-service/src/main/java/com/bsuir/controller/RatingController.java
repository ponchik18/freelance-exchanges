package com.bsuir.controller;

import com.bsuir.dto.RatingRequest;
import com.bsuir.entity.Rating;
import com.bsuir.service.RatingService;
import com.bsuir.util.PageSetting;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("{userId}")
    public Page<Rating> getAllRatingByUserId(@PathVariable String userId, PageSetting pageSetting) {
        return ratingService.getAllRatingByUserId(userId, pageSetting);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating saveRating(@RequestBody @Valid RatingRequest ratingRequest) {
        return ratingService.save(ratingRequest);
    }

    @DeleteMapping("{ratingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable long ratingId) {
        ratingService.delete(ratingId);
    }
}