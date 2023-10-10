package ua.vitalii.bella.clearsolutionsapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.vitalii.bella.clearsolutionsapi.util.validation_groups.EmailValidation;
import ua.vitalii.bella.clearsolutionsapi.util.validation_groups.NotNullValidation;
import ua.vitalii.bella.clearsolutionsapi.util.validation_groups.PastValidation;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Email may not be null", groups = {NotNullValidation.class})
    @NotBlank(message = "Email may have at least 1 symbol", groups = {NotNullValidation.class})
    @Email(message = "Email is not valid", regexp = ".+[@].+[\\.].+", groups = {EmailValidation.class})
    private String email;

    @NotNull(message = "FirstName may not be null", groups = {NotNullValidation.class})
    @NotBlank(message = "FirstName may have at least 1 symbol", groups = {NotNullValidation.class})
    private String firstName;

    @NotNull(message = "LastName may not be null", groups = {NotNullValidation.class})
    @NotBlank(message = "LastName may have at least 1 symbol", groups = {NotNullValidation.class})
    private String lastName;

    @NotNull(message = "BirthDate may not be null", groups = {NotNullValidation.class})
    @Past(message = "BirthDate may not be in future", groups = {PastValidation.class})
    //example 2023-09-18
    private LocalDate birthDate;

    @NotBlank(message = "Address may have at least 1 symbol", groups = {NotNullValidation.class})
    private String address;

    @NotBlank(message = "PhoneNumber may have at least 1 symbol", groups = {NotNullValidation.class})
    private String phoneNumber;

    public User(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
