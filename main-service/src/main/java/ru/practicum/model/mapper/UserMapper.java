package ru.practicum.model.mapper;

import ru.practicum.dto.UserDto;
import ru.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User makeUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto makeUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static List<User> makeUserList(List<UserDto> userDtoList) {
        List<User> users = new ArrayList<>();
        for (UserDto userDto : userDtoList)
            users.add(makeUser(userDto));
        return users;
    }

    public static List<UserDto> makeUserDtoList(List<User> userList) {
        List<UserDto> users = new ArrayList<>();
        for (User user : userList)
            users.add(makeUserDto(user));
        return users;
    }
}
