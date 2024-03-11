package com.bsuir.exception;

import static com.bsuir.constant.RatingServiceConstant.Error.DUPLICATE_RATING;

public class DuplicateRatingException extends RuntimeException {

    public DuplicateRatingException(String fromUser, String toUser) {
        super(String.format(DUPLICATE_RATING, fromUser, toUser));
    }
}