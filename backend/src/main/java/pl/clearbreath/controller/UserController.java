package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.clearbreath.ControllerUtils;
import pl.clearbreath.exception.InvalidPasswordException;
import pl.clearbreath.exception.SamePasswordException;
import pl.clearbreath.exception.UserNotFoundException;
import pl.clearbreath.model.User;
import pl.clearbreath.service.UserService;
import pl.clearbreath.dao.request.ChangePasswordRequest;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public User getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        userService.changePassword(user, changePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({InvalidPasswordException.class, SamePasswordException.class})
    public ResponseEntity<Object> handlePasswordChangeExceptions(RuntimeException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ControllerUtils.BAD_REQUEST_ACTION,
                request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return ControllerUtils.createErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                ControllerUtils.NOT_FOUND_ACTION,
                request
        );
    }
}
