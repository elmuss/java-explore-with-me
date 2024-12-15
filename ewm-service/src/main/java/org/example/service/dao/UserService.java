package org.example.service.dao;

import org.example.dto.user.NewUserRequest;
import org.example.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUser);

    void deleteUser(int userId);

    List<UserDto> getUsersByParam(List<Integer> ids, Integer from, Integer size);
}
