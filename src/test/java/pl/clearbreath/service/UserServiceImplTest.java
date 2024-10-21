package pl.clearbreath.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.clearbreath.TestUtils;
import pl.clearbreath.dao.request.ChangePasswordRequest;
import pl.clearbreath.exception.InvalidPasswordException;
import pl.clearbreath.exception.SamePasswordException;
import pl.clearbreath.exception.UserNotFoundException;
import pl.clearbreath.model.User;
import pl.clearbreath.repository.UserRepository;
import pl.clearbreath.repository.MarkerRepository;
import pl.clearbreath.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MarkerRepository markerRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private ChangePasswordRequest passwordRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = TestUtils.createUser();
        passwordRequest = TestUtils.createChangePasswordRequest();

        when(applicationContext.getBean(PasswordEncoder.class)).thenReturn(passwordEncoder);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertNotNull(userService.userDetailsService().loadUserByUsername(testUser.getEmail()));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.userDetailsService().loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    public void testDeleteUser_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        userService.deleteUser(testUser);

        verify(markerRepository, times(1)).deleteByUser(testUser);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(testUser));
        verify(markerRepository, never()).deleteByUser(testUser);
        verify(userRepository, never()).delete(testUser);
    }

    @Test
    public void testChangePassword_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword123", testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");

        userService.changePassword(testUser, passwordRequest);

        verify(userRepository, times(1)).save(testUser);
        assertEquals("encodedNewPassword", testUser.getPassword());
    }

    @Test
    public void testChangePassword_SamePasswordException() {
        passwordRequest = new ChangePasswordRequest("oldPassword123", "oldPassword123");

        assertThrows(SamePasswordException.class, () -> userService.changePassword(testUser, passwordRequest));
        verify(userRepository, never()).save(testUser);
    }

    @Test
    public void testChangePassword_InvalidPasswordException() {
        when(passwordEncoder.matches("oldPassword123", testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.changePassword(testUser, passwordRequest));
        verify(userRepository, never()).save(testUser);
    }

    @Test
    public void testChangePassword_UserNotFound() {
        when(passwordEncoder.matches("oldPassword123", testUser.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(testUser, passwordRequest));
        verify(userRepository, never()).save(testUser);
    }
}
