package com.bsuir.mapper;

import com.bsuir.dto.RatingRequest;
import com.bsuir.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RatingMapper {
    Rating toEntity(RatingRequest ratingRequest);
}