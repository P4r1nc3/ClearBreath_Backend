package pl.clearbreath.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.clearbreath.TestUtils;
import pl.clearbreath.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = TestUtils.createUser();
    }

    @Test
    public void testFindByEmail() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    public void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("non.existent@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByEmail("non.existent@example.com");

        assertTrue(foundUser.isEmpty());
    }
}
