package ua.vitalii.bella.clearsolutionsapi.service;

import ua.vitalii.bella.clearsolutionsapi.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    void deleteById(Long id);

    List<User> getAll();

    void update(User user);

    Optional<User> getById(Long id);

    List<User> saveAll(List<User> userList);

    List<User> findByBirthDateBetween(LocalDate from, LocalDate to);
}
