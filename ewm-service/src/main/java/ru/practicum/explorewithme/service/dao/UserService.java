package ru.practicum.explorewithme.service.dao;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUser);

    void deleteUser(int userId);

    List<UserDto> getUsersByParam(List<Integer> ids, Integer from, Integer size);
}
