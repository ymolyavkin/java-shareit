package ru.practicum.shareitserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareitserver.validator.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
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