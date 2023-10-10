package ua.vitalii.bella.clearsolutionsapi.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.vitalii.bella.clearsolutionsapi.exception.IncorrectDateRangeException;
import ua.vitalii.bella.clearsolutionsapi.exception.UserNotAdultException;
import ua.vitalii.bella.clearsolutionsapi.exception.UserNotFoundException;
import ua.vitalii.bella.clearsolutionsapi.model.ResponseMessage;
import ua.vitalii.bella.clearsolutionsapi.model.User;
import ua.vitalii.bella.clearsolutionsapi.service.UserService;
import ua.vitalii.bella.clearsolutionsapi.util.validation_groups.EmailValidation;
import ua.vitalii.bella.clearsolutionsapi.util.validation_groups.NotNullValidation;
import ua.vitalii.bella.clearsolutionsapi.util.validation_groups.PastValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    @Value("${user.min-age}")
    private Integer minAge;

    /**
     * Creates a new user. Validates the input according to specified constraints and checks if the user is an adult.
     *
     * @param user   The user to be created, validated, and saved.
     * @param result The result of the validation.
     * @return A response entity with the created user or validation errors, and an HTTP status code.
     * @throws UserNotAdultException if the user is not an adult.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Validated({EmailValidation.class, PastValidation.class, NotNullValidation.class}) @RequestBody User user, BindingResult result) throws UserNotAdultException {
        if (result.hasErrors())
            throw new ValidationException(result.getAllErrors().toString());
        if (user.getBirthDate().plusYears(minAge).isAfter(LocalDate.now()))
            throw new UserNotAdultException("User is not Adult");
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A response entity with a list of all users and an HTTP status code.
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A response entity with the requested user and an HTTP status code.
     * @throws UserNotFoundException if the user with the specified ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) throws UserNotFoundException {
        Optional<User> user = userService.getById(id);
        if (user.isEmpty())
            throw new UserNotFoundException(String.format("User with id %s not found", id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates an existing user by their ID. Validates the input, checks if the user is an adult, and saves the changes.
     *
     * @param id     The ID of the user to update.
     * @param user   The user with updated information, validated before saving.
     * @param result The result of the validation.
     * @return A response entity with the updated user and an HTTP status code.
     * @throws UserNotFoundException if the user with the specified ID is not found.
     * @throws UserNotAdultException if the user is not an adult.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
                                        @Validated({EmailValidation.class, PastValidation.class, NotNullValidation.class}) @RequestBody User user,
                                        BindingResult result) throws UserNotFoundException, UserNotAdultException {
        Optional<User> optionalUser = userService.getById(id);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id %s not found", id));
        if (result.hasErrors())
            throw new ValidationException(result.getAllErrors().toString());
        User existingUser = optionalUser.get();

        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());

        if (existingUser.getBirthDate().plusYears(minAge).isAfter(LocalDate.now()))
            throw new UserNotAdultException("User is not Adult");

        userService.save(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    /**
     * Updates specific fields of an existing user by their ID. Validates the input, checks if the user is an adult, and saves the changes.
     *
     * @param id     The ID of the user to update.
     * @param user   The user with updated information, validated before saving.
     * @param result The result of the validation.
     * @return A response entity with the updated user and an HTTP status code.
     * @throws UserNotFoundException if the user with the specified ID is not found.
     * @throws UserNotAdultException if the user is not an adult.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserFields(@PathVariable("id") Long id,
                                              @Validated({EmailValidation.class, PastValidation.class}) @RequestBody User user,
                                              BindingResult result) throws UserNotFoundException, UserNotAdultException {
        User existingUser = userService.getById(id).orElse(null);
        if (existingUser == null)
            throw new UserNotFoundException(String.format("User with id %s not found", id));

        String[] ignoreProperties = getNullPropertyNames(user);
        BeanUtils.copyProperties(user, existingUser, ignoreProperties);

        if (result.hasErrors())
            throw new ValidationException(result.getAllErrors().toString());
        if (existingUser.getBirthDate().plusYears(minAge).isAfter(LocalDate.now()))
            throw new UserNotAdultException("User is not Adult");

        userService.save(existingUser);
        return new ResponseEntity<>(existingUser, HttpStatus.OK);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @return A response entity with a success message and an HTTP status code.
     * @throws UserNotFoundException if the user with the specified ID is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        if (userService.getById(id).isEmpty())
            throw new UserNotFoundException(String.format("User with id %s not found", id));
        userService.deleteById(id);
        return new ResponseEntity<>(new ResponseMessage(String.format("User with id %s successfully deleted", id)), HttpStatus.OK);
    }

    /**
     * Searches for users by birthDate range. Validates the date range.
     *
     * @param from The start date of the range.
     * @param to   The end date of the range.
     * @return A response entity with a list of users within the specified date range and an HTTP status code.
     * @throws IncorrectDateRangeException if the 'from' date is after the 'to' date.
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByBirthDateRange(@RequestParam(name = "from") LocalDate from,
                                                         @RequestParam(name = "to") LocalDate to) throws IncorrectDateRangeException {
        if (from.isAfter(to))
            throw new IncorrectDateRangeException("Parameter FROM cannot be higher then parameter TO");
        List<User> users = userService.findByBirthDateBetween(from, to);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Gets the names of object properties that are null.
     *
     * @param source The object whose properties to check.
     * @return An array of strings containing null property names.
     */
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> propertyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor propertyDescriptor : src.getPropertyDescriptors()) {
            Object srcValue = src.getPropertyValue(propertyDescriptor.getName());
            if (srcValue == null) {
                propertyNames.add(propertyDescriptor.getName());
            }
        }
        return propertyNames.toArray(new String[0]);
    }

}
