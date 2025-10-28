package mdp.tirexchageservice.mappers;

import mdp.tirexchageservice.dto.Epd015DTO;
import mdp.tirexchageservice.entities.TirMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TirMessageMapper {
    @Mapping(target = "messageType", constant = "EPD015")
    @Mapping(target = "guaranteeNumber", source = "guaranteeNumber")
    @Mapping(target = "iruReference", source = "iruReference")
    @Mapping(target = "customsIndex", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "payload", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TirMessage toEntity(Epd015DTO dto);
}
