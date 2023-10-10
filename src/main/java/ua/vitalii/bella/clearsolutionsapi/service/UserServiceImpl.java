package ua.vitalii.bella.clearsolutionsapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.vitalii.bella.clearsolutionsapi.model.User;
import ua.vitalii.bella.clearsolutionsapi.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> saveAll(List<User> userList) {
        return userRepository.saveAll(userList);
    }

    @Override
    public List<User> findByBirthDateBetween(LocalDate from, LocalDate to) {
        return userRepository.findByBirthDateBetween(from, to);
    }
}
