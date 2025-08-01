package kz.dulaty.queue.feature.manager.data.dto;

import kz.dulaty.queue.feature.department.data.entity.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDto {
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private Department department;
    private String windowNumber;
    private Boolean isActive;
}
