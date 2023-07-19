package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class IncomingUserDto {
    @NotBlank
    private String name;

    @NotBlank(message = "адрес электронной почты не должен быть пустым")
    @Pattern(regexp = "^.+@.+\\..+$", message = "Некорректный адрес электронной почты")
    private String email;


}
