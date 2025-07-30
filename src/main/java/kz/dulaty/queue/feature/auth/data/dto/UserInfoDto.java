package kz.dulaty.queue.feature.auth.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserInfoDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phoneNumber;
    private Set<RoleDto> roles;
}
