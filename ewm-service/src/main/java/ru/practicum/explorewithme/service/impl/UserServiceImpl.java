package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.dao.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 250;
    private static final Integer EMAIL_MIN_LENGTH = 6;
    private static final Integer EMAIL_MAX_LENGTH = 254;
    private static final Integer EMAIL_LOCAL_PART_LENGTH = 64;
    private static final Integer EMAIL_DOMAIN_PART_LENGTH = 63;
    private static final String USER_NOT_FOUND_MSG = "User with id=%d was not found";
    private static final String USER_NAME_LENGTH_MSG = "User name length should be between 2 and 250";
    private static final String USER_EMAIL_LENGTH_MSG = "Email length should be between 6 and 254";
    private static final String USER_EMAIL_LOCAL_PART_LENGTH_MSG = "Email local part length should be less than 64 symbols";
    private static final String USER_EMAIL_DOMAIN_PART_LENGTH_MSG = "Email domain part length should be less than 63 symbols";
    private static final String USER_NAME_EMPTY_MSG = "User name should not be empty";
    private static final String USER_EMAIL_EMPTY_MSG = "User email should not be empty";

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUser) {
        validateUser(newUser);
        User user = UserMapper.modelFromNewUserRequest(newUser);
        return UserMapper.modelToUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));

        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsersByParam(List<Integer> ids, Integer from, Integer size) {
        if (ids == null) {
            return userRepository.findAll().stream().map(UserMapper::modelToUserDto).limit(size).toList();
        } else {
            return userRepository.getUsersByIds(ids).stream().map(UserMapper::modelToUserDto).limit(size).toList();
        }
    }

    public void validateUser(NewUserRequest newUser) {
        if (newUser.getName() == null) {
            throw new ValidationException(USER_NAME_EMPTY_MSG);
        } else {
            if (newUser.getName().isBlank()) {
                throw new ValidationException(USER_NAME_EMPTY_MSG);
            }
            if (newUser.getName().length() < NAME_MIN_LENGTH || newUser.getName().length() > NAME_MAX_LENGTH) {
                throw new ValidationException(USER_NAME_LENGTH_MSG);
            }
        }

        if (newUser.getEmail() == null) {
            throw new ValidationException(USER_EMAIL_EMPTY_MSG);
        } else {
            if (newUser.getEmail().isBlank()) {
                throw new ValidationException(USER_EMAIL_EMPTY_MSG);
            }
            if (newUser.getEmail().length() < EMAIL_MIN_LENGTH || newUser.getEmail().length() > EMAIL_MAX_LENGTH) {
                throw new ValidationException(USER_EMAIL_LENGTH_MSG);
            }

            String email = newUser.getEmail();
            String[] partsOfEmail = email.split("@");
            String[] partsOfDomain = partsOfEmail[partsOfEmail.length - 1].split("\\.");
            if (partsOfEmail[0].length() > EMAIL_LOCAL_PART_LENGTH) {
                throw new ValidationException(USER_EMAIL_LOCAL_PART_LENGTH_MSG);
            }
            if (partsOfDomain[0].length() > EMAIL_DOMAIN_PART_LENGTH) {
                throw new ValidationException(USER_EMAIL_DOMAIN_PART_LENGTH_MSG);
            }
        }
    }
}
