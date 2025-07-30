package kz.dulaty.queue.feature.auth.data.mapper;

import kz.dulaty.queue.feature.auth.data.dto.AddUserRequestDto;
import kz.dulaty.queue.feature.auth.data.dto.SignUpRequest;
import kz.dulaty.queue.feature.auth.data.dto.UserInfoDto;
import kz.dulaty.queue.feature.auth.data.entity.Role;
import kz.dulaty.queue.feature.auth.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {Boolean.class})
public interface UserInfoMapper {
    UserInfoMapper USER_INFO_MAPPER = Mappers.getMapper(UserInfoMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", expression = "java(Boolean.FALSE)")
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastLoginDateTime", ignore = true)
    @Mapping(target = "passCount", ignore = true)
    @Mapping(target = "passChangeDateTime", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(SignUpRequest signUpRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", expression = "java(Boolean.TRUE)")
    @Mapping(target = "locked", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastLoginDateTime", ignore = true)
    @Mapping(target = "passCount", ignore = true)
    @Mapping(target = "passChangeDateTime", ignore = true)
    @Mapping(target = "roles", source = "roleList")
    User toEntity(AddUserRequestDto signUpRequest, Set<Role> roleList);

    UserInfoDto toDto(User user);
}
