package pl.clearbreath.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.request.ChangePasswordRequest;
import pl.clearbreath.exception.InvalidPasswordException;
import pl.clearbreath.exception.SamePasswordException;
import pl.clearbreath.exception.UserNotFoundException;
import pl.clearbreath.model.User;
import pl.clearbreath.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = TestUtils.createUser();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testUser);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetUserData() {
        User returnedUser = userController.getUserData();

        assertEquals(testUser, returnedUser);
        verify(securityContext, times(1)).getAuthentication();
    }

    @Test
    public void testDeleteUser() {
        ResponseEntity<?> response = userController.deleteUser();

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(testUser);
    }

    @Test
    public void testChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");

        ResponseEntity<?> response = userController.changePassword(changePasswordRequest);

        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).changePassword(testUser, changePasswordRequest);
    }

    @Test
    public void testHandleInvalidPasswordException() {
        InvalidPasswordException exception = new InvalidPasswordException("Invalid password");

        when(httpServletRequest.getRequestURI()).thenReturn("/users/change-password");

        ResponseEntity<Object> response = userController.handlePasswordChangeExceptions(exception, httpServletRequest);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid password"));
    }

    @Test
    public void testHandleSamePasswordException() {
        SamePasswordException exception = new SamePasswordException("Passwords are the same");

        when(httpServletRequest.getRequestURI()).thenReturn("/users/change-password");

        ResponseEntity<Object> response = userController.handlePasswordChangeExceptions(exception, httpServletRequest);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Passwords are the same"));
    }


    @Test
    public void testHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        when(httpServletRequest.getRequestURI()).thenReturn("/users/change-password");

        ResponseEntity<Object> response = userController.handleUserNotFoundException(exception, httpServletRequest);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User not found"));
    }
}
