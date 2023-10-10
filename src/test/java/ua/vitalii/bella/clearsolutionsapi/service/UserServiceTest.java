package ua.vitalii.bella.clearsolutionsapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.vitalii.bella.clearsolutionsapi.model.User;
import ua.vitalii.bella.clearsolutionsapi.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User userExample;
    private List<User> userListExample;

    @BeforeEach
    public void init() {
        userExample = User.builder().email("test@gmail.com").firstName("John").lastName("Black").birthDate(LocalDate.of(1995, 10, 6)).address("Kyiv").phoneNumber("+380638574859").build();
        userListExample = List.of(userExample,
                User.builder().email("test.user2@gmail.com").firstName("Philipp").lastName("Morris").birthDate(LocalDate.of(2000, 6, 13)).address("Lviv").phoneNumber("+380503746382").build(),
                User.builder().email("test.user3@gmail.com").firstName("Andrew").lastName("Brown").birthDate(LocalDate.of(2001, 9, 22)).build(),
                User.builder().email("test.user4@gmail.com").firstName("John").lastName("Wick").birthDate(LocalDate.of(2005, 2, 16)).address("Odessa").build());
    }

    /**
     * Test for saving a user in the UserRepository.
     * It verifies that the user is successfully saved and returned.
     */
    @Test
    public void UserService_SaveUser() {
        User user = userExample;
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    /**
     * Test for deleting a user by ID in the UserRepository.
     * It verifies that the user is successfully deleted, and the repository size is reduced by one.
     */
    @Test
    public void UserService_DeleteUserById() {
        Long userId = 1L;
        userService.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    /**
     * Test for getting all users from the UserRepository.
     * It verifies that all users are successfully retrieved.
     */
    @Test
    public void UserService_GetAllUsers() {
        List<User> users = userListExample;
        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getAll();

        assertSame(users, retrievedUsers);
    }

    /**
     * Test for updating a user in the UserRepository.
     * It verifies that the user is successfully updated.
     */
    @Test
    public void UserService_UpdateUser() {
        User user = userExample;
        when(userRepository.save(user)).thenReturn(user);

        userService.update(user);

        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test for getting a user by ID from the UserRepository.
     * It verifies that the correct user is successfully retrieved.
     */
    @Test
    public void UserService_GetUserById() {
        Long userId = 1L;
        User user = userExample;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getById(userId);

        assertTrue(retrievedUser.isPresent());
        assertEquals(user, retrievedUser.get());
    }

    /**
     * Test for saving a list of users in the UserRepository.
     * It verifies that the list of users is successfully saved and returned.
     */
    @Test
    public void UserService_SaveAllUsers() {
        List<User> users = userListExample;
        when(userRepository.saveAll(users)).thenReturn(users);

        List<User> savedUsers = userService.saveAll(users);

        assertSame(users, savedUsers);
    }

    /**
     * Test for finding users by birthDate range in the UserRepository.
     * It verifies that the list of users within the specified birthDate range matches the expected list.
     */
    @Test
    public void UserService_FindUsersByBirthDateRange() {
        LocalDate from = LocalDate.of(1995, 1, 1);
        LocalDate to = LocalDate.of(2020, 12, 31);
        List<User> users = userListExample;
        when(userRepository.findByBirthDateBetween(from, to)).thenReturn(users);

        List<User> retrievedUsers = userService.findByBirthDateBetween(from, to);

        assertSame(users, retrievedUsers);
    }
}
