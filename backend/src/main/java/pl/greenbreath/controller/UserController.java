package pl.greenbreath.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.greenbreath.model.User;
import pl.greenbreath.service.UserService;

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

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        try {
            userService.deleteUser(user);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("An error occurred while deleting the user.");
        }
    }

}
