package ua.vitalii.bella.clearsolutionsapi.config;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.vitalii.bella.clearsolutionsapi.exception.IncorrectDateRangeException;
import ua.vitalii.bella.clearsolutionsapi.exception.UserNotAdultException;
import ua.vitalii.bella.clearsolutionsapi.exception.UserNotFoundException;
import ua.vitalii.bella.clearsolutionsapi.model.ResponseMessage;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleUserNotFoundException(UserNotFoundException e) {
        ResponseMessage response = new ResponseMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAdultException.class)
    public ResponseEntity<ResponseMessage> handleUserNotAdultException(UserNotAdultException e) {
        ResponseMessage response = new ResponseMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseMessage> handleUserNotAdultException(ValidationException e) {
        ResponseMessage response = new ResponseMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectDateRangeException.class)
    public ResponseEntity<ResponseMessage> handleIncorrectDateRangeException(IncorrectDateRangeException e) {
        ResponseMessage response = new ResponseMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
