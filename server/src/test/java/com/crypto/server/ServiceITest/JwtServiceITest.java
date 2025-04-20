package com.crypto.server.ServiceITest;

import com.crypto.server.config.exceptions.UnauthorizedException;
import com.crypto.server.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
public class JwtServiceITest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "yJhY2zA6WxVr8PqWNxQtbk5U4v3iSz1A7ghz6j9kPZJXy9U2w");
    }

    @Test
    void testGenerateAndExtractToken() {
        String token = jwtService.generateToken(1, "testuser");

        assertNotNull(token);
        assertEquals("testuser", jwtService.extractClaim(token, "username"));
        assertEquals(1, jwtService.extractClaim(token, "_id"));
    }

    @Test
    void testExtractClaim_invalidToken_throwsException() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(UnauthorizedException.class, () -> {
            jwtService.extractClaim(invalidToken, "username");
        });
    }
}
