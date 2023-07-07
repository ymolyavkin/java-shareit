package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
//@NoArgsConstructor
//@RequiredArgsConstructor
public class UserDto {
    private final Long id;
    @NotBlank
    private final String name;
    @NotBlank(message = "адрес электронной почты не должен быть пустым")
    @Pattern(regexp = "^.+@.+\\..+$", message = "Некорректный адрес электронной почты")
    private final String email;

 /*   public UserDto(Long id, String name, String email) {
    }*/
}
