package ru.practicum.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.NoFoundObjectException;
import ru.practicum.model.User;
import ru.practicum.repository.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static ru.practicum.model.mapper.UserMapper.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto addUsers(UserDto request) {
        User user = makeUser(request);
        User saveUser = userRepository.save(user);

        return makeUserDto(saveUser);
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            List<User> users = userRepository.findAll(PageRequest.of(from, size)).getContent();
            return makeUserDtoList(users);
        }

        List<User> users = userRepository.findAllByIdIn(ids, PageRequest.of(from, size));
        return makeUserDtoList(users);
    }

    @Transactional
    public void deleteUsers(Long userId) {
        checkExistUserById(userId);
        userRepository.deleteById(userId);
    }

    public User getUserByIdIfExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoFoundObjectException(String.format("Пользователь с id='%s' не найден", userId)));
    }

    public void checkExistUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoFoundObjectException(String.format("Пользователь с id='%s' не найден", userId));
        }
    }
}
