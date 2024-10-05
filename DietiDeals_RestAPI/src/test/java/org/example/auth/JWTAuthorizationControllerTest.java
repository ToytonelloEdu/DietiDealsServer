package org.example.auth;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JWTAuthorizationControllerTest {

    static JwtAuthorizationController ac;

    @BeforeAll
    public static void init(){
        ac = JwtAuthorizationController.getInstance();
    }

    @Test
    public void TestCreateToken_1000ttlMillis_validToken(){
        String token = ac.createToken("ciroanastasio", 1000L);
        assertTrue(ac.validateToken(token));
    }

    @Test
    public void TestCreateToken_24httlMillis_validToken(){
        String token = ac.createToken("ciroanastasio", 86400000L);
        assertTrue(ac.validateToken(token));
    }

    @Test
    public void TestCreateToken_MaxValuettlMillis_validToken(){
        long millis = 9223372036854775807L - System.currentTimeMillis() - 1000;
        String token = ac.createToken("ciroanastasio", millis);
        assertTrue(ac.validateToken(token));
    }

    @Test
    public void TestCreateToken_UsernameNULL_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> ac.createToken(null, 86400000L));
    }

    @Test
    public void TestCreateToken_UsernameEmpty_throwsException(){
        assertThrows(IllegalArgumentException.class, () -> ac.createToken("", 86400000L));
    }

    @Test
    public void TestCreateToken_NegativettlMillis_invalidToken(){
        String token = ac.createToken("ciroanastasio", -86400000L);
        assertFalse(ac.validateToken(token));
    }

    @Test
    public void TestCreateToken_0ttlMillis_invalidToken(){
        String token = ac.createToken("ciroanastasio", 0L);
        assertFalse(ac.validateToken(token));
    }

    @Test
    public void TestCreateToken_PlusMaxValuettlMillis_invalidToken(){
        String token = ac.createToken("ciroanastasio", 9223372036854775807L);
        assertFalse(ac.validateToken(token));
    }

}
