package ua.vitalii.bella.clearsolutionsapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.vitalii.bella.clearsolutionsapi.model.ResponseMessage;
import ua.vitalii.bella.clearsolutionsapi.model.User;
import ua.vitalii.bella.clearsolutionsapi.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    private User userExample;
    private List<User> userListExample;

    @BeforeEach
    public void init() {
        userExample = User.builder().id(1L).email("test@gmail.com").firstName("John").lastName("Black").birthDate(LocalDate.of(1995, 10, 6)).address("Kyiv").phoneNumber("+380638574859").build();
        userListExample = List.of(userExample,
                User.builder().id(2L).email("test.user2@gmail.com").firstName("Philipp").lastName("Morris").birthDate(LocalDate.of(2000, 6, 13)).address("Lviv").phoneNumber("+380503746382").build(),
                User.builder().id(3L).email("test.user3@gmail.com").firstName("Andrew").lastName("Brown").birthDate(LocalDate.of(2001, 9, 22)).build(),
                User.builder().id(4L).email("test.user4@gmail.com").firstName("John").lastName("Wick").birthDate(LocalDate.of(2005, 2, 16)).address("Odessa").build());
    }

    @BeforeEach
    public void configureObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void UserController_CreateUser_ValidUser_ReturnCreated() throws Exception {
        User validUser = userExample;

        when(userService.save(validUser)).thenReturn(validUser);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        Assertions.assertEquals(createdUser, validUser);
    }

    @Test
    public void UserController_CreateUser_NullEmail_ReturnEmailValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setEmail(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("Email may not be null"));
    }

    @Test
    public void UserController_CreateUser_EmptyEmail_ReturnEmailValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setEmail("");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("Email may have at least 1 symbol"));
    }

    @Test
    public void UserController_CreateUser_InvalidEmail_ReturnEmailValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setEmail("shdyvdf");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("Email is not valid"));
    }

    @Test
    public void UserController_CreateUser_NullFirstName_ReturnFirstNameValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setFirstName(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("FirstName may not be null"));
    }

    @Test
    public void UserController_CreateUser_EmptyFirstName_ReturnFirstNameValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setFirstName("");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("FirstName may have at least 1 symbol"));
    }

    @Test
    public void UserController_CreateUser_NullLastName_ReturnLastNameValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setLastName(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("LastName may not be null"));
    }

    @Test
    public void UserController_CreateUser_EmptyLastName_ReturnLastNameValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setLastName("");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("LastName may have at least 1 symbol"));
    }

    @Test
    public void UserController_CreateUser_NullBirthDate_ReturnBirthDateValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setBirthDate(null);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("BirthDate may not be null"));
    }

    @Test
    public void UserController_CreateUser_FutureBirthDate_ReturnValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setBirthDate(LocalDate.now().plusYears(2));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("BirthDate may not be in future"));
    }

    @Test
    public void UserController_CreateUser_EmptyAddress_ReturnBirthDateValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setAddress("");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("Address may have at least 1 symbol"));
    }

    @Test
    public void UserController_CreateUser_EmptyPhoneNumber_ReturnBirthDateValidationError() throws Exception {
        User invalidUser = userExample;
        invalidUser.setPhoneNumber("");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("PhoneNumber may have at least 1 symbol"));
    }

    @Test
    public void UserController_CreateUser_UnderageUser_ReturnUserNotAdultException() throws Exception {
        User underageUser = userExample;
        underageUser.setBirthDate(LocalDate.now().minusYears(16));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(underageUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("User is not Adult"));
    }

    @Test
    public void UserController_GetAllUsers_ReturnAllUsers() throws Exception {
        List<User> userList = userListExample;
        when(userService.getAll()).thenReturn(userList);

        String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<User> responseUserList = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        Assertions.assertEquals(responseUserList, userList);
    }

    @Test
    public void getUserById_ReturnsUser() throws Exception {
        User user = userExample;
        given(userService.getById(anyLong()))
                .willReturn(Optional.of(user));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);


        Assertions.assertEquals(responseUser, user);
    }

    @Test
    public void getUserById_ReturnsNotFound() throws Exception {
        Long id = 1L;
        given(userService.getById(anyLong()))
                .willReturn(Optional.empty());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains(String.format("User with id %s not found", id)));
    }

    @Test
    public void updateUser_Success() throws Exception {
        User user = userExample;
        user.setEmail("updated@example.com");

        Long userId = 1L;

        given(userService.getById(userId))
                .willReturn(Optional.of(user));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        Assertions.assertEquals(responseUser, user);
    }

    @Test
    public void updateUser_UserNotFound() throws Exception {
        Long userId = 1L;

        given(userService.getById(userId))
                .willReturn(Optional.empty());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains(String.format("User with id %s not found", userId)));
    }

    @Test
    public void updateUserFields_ValidUser_ReturnsOk() throws Exception {
        User existingUser = userExample;
        given(userService.getById(anyLong()))
                .willReturn(Optional.of(existingUser));

        User requestUser = new User();
        requestUser.setEmail("updated@gmail.com");
        requestUser.setFirstName("UpdatedFirstName");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isOk())
                .andReturn();

        User responseUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        existingUser.setEmail(requestUser.getEmail());
        existingUser.setFirstName(requestUser.getFirstName());

        Assertions.assertEquals(responseUser, existingUser);
    }

    @Test
    public void updateUserFields_InvalidUser_ReturnsBadRequest() throws Exception {
        User existingUser = userExample;
        given(userService.getById(anyLong()))
                .willReturn(Optional.of(existingUser));

        User requestUser = new User();
        requestUser.setEmail("invalid_email");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("Email is not valid"));

    }

    @Test
    public void updateUserFields_UserNotAdult_ReturnsBadRequest() throws Exception {
        User existingUser = userExample;
        given(userService.getById(anyLong()))
                .willReturn(Optional.of(existingUser));

        User requestUser = new User();
        requestUser.setBirthDate(LocalDate.of(2010, 1, 1));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("User is not Adult"));
    }

    @Test
    public void updateUserFields_UserNotFound_ReturnsNotFound() throws Exception {
        Long userId = 1L;
        given(userService.getById(anyLong()))
                .willReturn(Optional.empty());

        User requestUser = new User();
        requestUser.setEmail("updated@gmail.com");
        requestUser.setFirstName("UpdatedFirstName");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestUser)))
                .andExpect(status().isNotFound())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains(String.format("User with id %s not found", userId)));
    }

    @Test
    public void deleteUser_ExistingUser_ReturnsOk() throws Exception {
        Long userId = 1L;
        User existingUser = userExample;
        given(userService.getById(anyLong()))
                .willReturn(Optional.of(existingUser));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isOk())
                .andReturn();

        String responseMessage = result.getResponse().getContentAsString();

        Assertions.assertTrue(responseMessage.contains(String.format("User with id %s successfully deleted", userId)));
    }

    @Test
    public void deleteUser_UserNotFound_ReturnsNotFound() throws Exception {
        Long userId = 1L;

        given(userService.getById(anyLong()))
                .willReturn(Optional.empty());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
                .andExpect(status().isNotFound())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains(String.format("User with id %s not found", userId)));
    }

    @Test
    public void searchUsersByBirthDateRange_ValidRange_ReturnsListOfUsers() throws Exception {
        List<User> expectedUsers = userListExample;

        LocalDate from = LocalDate.of(1995, 1, 1);
        LocalDate to = LocalDate.of(2010, 12, 31);

        given(userService.findByBirthDateBetween(from, to)).willReturn(expectedUsers);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<User> responseUserList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertEquals(responseUserList, expectedUsers);

    }

    @Test
    public void searchUsersByBirthDateRange_InvalidRange_ReturnsBadRequest() throws Exception {
        LocalDate from = LocalDate.of(2002, 1, 1);
        LocalDate to = LocalDate.of(2000, 12, 31);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        ResponseMessage responseMessage = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);

        Assertions.assertTrue(responseMessage.getMessage().contains("Parameter FROM cannot be higher then parameter TO"));
    }
}
