package kz.dulaty.queue.feature.manager.data.mapper;

import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.manager.data.dto.ManagerDto;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface ManagerMapper {
    ManagerMapper MANAGER_MAPPER = Mappers.getMapper(ManagerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "windowNumber", source = "windowNumber")
    Manager toEntity(String windowNumber, Department department, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "windowNumber", source = "windowNumber")
    void updateEntity(@MappingTarget Manager manager,
                      String windowNumber,
                      Department department,
                      User user);
    @Mapping(target = "name", source = "manager.user.name")
    @Mapping(target = "surname", source = "manager.user.surname")
    @Mapping(target = "patronymic", source = "manager.user.patronymic")
    @Mapping(target = "email", source = "manager.user.email")
    @Mapping(target = "phoneNumber", source = "manager.user.phoneNumber")
    @Mapping(target = "department", source = "manager.department")
    @Mapping(target = "windowNumber", source = "manager.windowNumber")
    @Mapping(target = "isActive", source = "manager.isActive")
    ManagerDto toDto(Manager manager);
}
