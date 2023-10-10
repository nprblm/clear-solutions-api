package ua.vitalii.bella.clearsolutionsapi.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.vitalii.bella.clearsolutionsapi.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
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
     * It checks whether the saved user is not null and has a valid ID.
     */
    @Test
    public void UserRepository_SaveUser_ReturnSavedUser() {
        User savedUser = userRepository.save(userExample);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    /**
     * Test for deleting a user by ID in the UserRepository.
     * It verifies that the user is successfully deleted, and the repository size is reduced by one.
     */
    @Test
    public void UserRepository_DeleteUserById_ReturnDeletedUserId() {
        List<User> savedUserList = userRepository.saveAll(userListExample);

        Long deletedUserId = savedUserList.get(0).getId();
        userRepository.deleteById(deletedUserId);
        savedUserList.remove(savedUserList.get(0));

        Assertions.assertThat(userRepository.findById(deletedUserId)).isEqualTo(Optional.empty());
        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(savedUserList.size());
        Assertions.assertThat(userRepository.findAll()).isEqualTo(savedUserList);

    }

    /**
     * Test for retrieving all users from the UserRepository.
     * It checks if the list of users is not null, and the size and content match the expected values.
     */
    @Test
    public void UserRepository_GetAllUsers_ReturnAllUsers() {
        List<User> savedUserList = userRepository.saveAll(userListExample);
        List<User> responseUserList = userRepository.findAll();

        Assertions.assertThat(responseUserList).isNotNull();
        Assertions.assertThat(savedUserList.size()).isEqualTo(responseUserList.size());
        Assertions.assertThat(savedUserList).isEqualTo(responseUserList);
    }

    /**
     * Test for finding a user by ID in the UserRepository.
     * It verifies that the user is found and matches the expected user.
     */
    @Test
    public void UserRepository_FindUserById_ReturnUser() {
        User savedUser = userRepository.save(userExample);

        User userById = userRepository.findById(savedUser.getId()).orElse(null);

        Assertions.assertThat(userById).isNotNull();
        Assertions.assertThat(userById).isEqualTo(savedUser);
    }

    /**
     * Test for saving a list of users in the UserRepository.
     * It ensures that all users are saved and match the expected values.
     */
    @Test
    public void UserRepository_SaveAllUsers_ReturnAllSavedUsers() {
        List<User> savedUserList = userRepository.saveAll(userListExample);
        List<User> responceUserList = userRepository.findAll();


        Assertions.assertThat(responceUserList).isNotNull();
        Assertions.assertThat(responceUserList.size()).isEqualTo(savedUserList.size());
        Assertions.assertThat(responceUserList).isEqualTo(savedUserList);
    }

    /**
     * Test for finding users by birthDate range in the UserRepository.
     * It checks if the retrieved list matches the expected list of users within the specified date range.
     */
    @Test
    public void UserRepository_FindUserByBirthDateBetween_ReturnUsersList() {
        LocalDate dateFrom = LocalDate.of(2001, 1, 1);
        LocalDate dateTo = LocalDate.now();

        List<User> assertUserList = userRepository.saveAll(userListExample);
        assertUserList = assertUserList.stream().filter(user -> user.getBirthDate().isAfter(dateFrom) && user.getBirthDate().isBefore(dateTo)).toList();

        List<User> responseUserList = userRepository.findByBirthDateBetween(dateFrom, dateTo);

        Assertions.assertThat(responseUserList).isNotNull();
        Assertions.assertThat(responseUserList.size()).isEqualTo(assertUserList.size());
        Assertions.assertThat(responseUserList).isEqualTo(assertUserList);
    }

}
