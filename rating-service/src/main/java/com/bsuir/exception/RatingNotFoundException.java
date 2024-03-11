package com.bsuir.exception;


import static com.bsuir.constant.RatingServiceConstant.Error.RATING_NOT_FOUND;

public class RatingNotFoundException extends RuntimeException{
    public RatingNotFoundException(long id) {
        super(String.format(RATING_NOT_FOUND, id));
    }
}