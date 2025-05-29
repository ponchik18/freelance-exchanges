package com.bsuir.mapper;

import com.bsuir.dto.message.ChatResponse;
import com.bsuir.entity.Chat;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChatMapper {

    ChatResponse toDto(Chat chat);

}