package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class IncomingUserDto {
    @NotBlank(groups = Marker.OnCreate.class)
    private String name;

    @NotBlank(groups = Marker.OnCreate.class, message = "адрес электронной почты не должен быть пустым")
    @Pattern(regexp = "^.+@.+\\..+$", message = "Некорректный адрес электронной почты")
    private String email;
<<<<<<< HEAD
}
=======


}
>>>>>>> 6cc5d081d5fc2f68fbe70910fb5eb6895ef10748
