package ru.practicum.shareitgateway.user.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import ru.practicum.shareitgateway.validator.Marker;

@Data
@AllArgsConstructor
public class IncomingUserDto {
    @Size(max = 255, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreate.class)
    private String name;
    @Size(max = 512, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @NotBlank(groups = Marker.OnCreate.class, message = "адрес электронной почты не должен быть пустым")
    @Pattern(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, regexp = "^.+@.+\\..+$", message = "Некорректный адрес электронной почты")
    private String email;
}