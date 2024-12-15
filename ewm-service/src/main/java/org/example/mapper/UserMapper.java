package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.user.NewUserRequest;
import org.example.dto.user.UserDto;
import org.example.dto.user.UserShortDto;
import org.example.model.User;

@UtilityClass
public class UserMapper {
    public static User modelFromNewUserRequest(NewUserRequest newUser) {
        return User.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .build();
    }

    public static UserDto modelToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto modelToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}
