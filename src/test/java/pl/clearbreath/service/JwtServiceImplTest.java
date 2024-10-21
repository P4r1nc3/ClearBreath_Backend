package pl.clearbreath.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import pl.clearbreath.TestUtils;
import pl.clearbreath.service.impl.JwtServiceImpl;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private UserDetails userDetails;
    private String mockToken;

    @BeforeEach
    public void setUp() {
        jwtService = new JwtServiceImpl();
        String secretKey = "mysecretkeyformyappjwtshouldbeatleast32characters";
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", secretKey);
        userDetails = TestUtils.createUser();
        mockToken = jwtService.generateToken(userDetails);
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(mockToken, "Generated token should not be null.");
    }

    @Test
    public void testExtractUserName() {
        String extractedUserName = jwtService.extractUserName(mockToken);
        assertEquals(userDetails.getUsername(), extractedUserName, "Extracted username should match the user.");
    }

    @Test
    public void testIsTokenValid() {
        boolean isValid = jwtService.isTokenValid(mockToken, userDetails);
        assertTrue(isValid, "Token should be valid for the user.");
    }

    @Test
    public void testIsTokenInvalidForDifferentUser() {
        UserDetails differentUser = new User("differentUser", "password", Collections.emptyList());
        boolean isValid = jwtService.isTokenValid(mockToken, differentUser);
        assertFalse(isValid, "Token should not be valid for a different user.");
    }

    @Test
    public void testIsTokenExpired() {
        boolean isValid = jwtService.isTokenValid(mockToken, userDetails);
        assertTrue(isValid, "Token should be valid and not expired.");
    }
}
