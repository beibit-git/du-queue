package kz.dulaty.queue.feature.department.data;

import kz.dulaty.queue.feature.department.data.dto.DepartmentRequestDto;
import kz.dulaty.queue.feature.department.data.entity.Department;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {
    DepartmentMapper DEPARTMENT_MAPPER = Mappers.getMapper(DepartmentMapper.class);

    Department toEntity(DepartmentRequestDto request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Department department, DepartmentRequestDto request);
}
