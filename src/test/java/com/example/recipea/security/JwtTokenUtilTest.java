package com.example.recipea.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mahdi Sharifi
 */
class JwtTokenUtilTest {

    @Test
    void shouldValidateToken_whenValidateTokenIsCalled() {
        String JWT="Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJtb3ZpZWFwaSIsInN1YiI6Im1haGRpIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY2NTM2MjMyMSwiZXhwIjoxNjY3OTU3OTIxfQ.6ZUM8FxUzzxX500ZF9498lMkowaWEjnyGgMFfTzJyJos7lhexru0zgvVzGXy88aS0BJXso_46oGI9flhuXiflA";
        Claims claims = JwtTokenUtil.validateToken(JWT.replace("Bearer ","").trim());
        assertNotNull(claims.get("authorities"));
    }

    @Test
    void shouldIssueJwtToken_whenIssueJwtToken() {
        String jwtToken = JwtTokenUtil.issueJwtToken("mahdi");
        assertNotNull(jwtToken);

        Claims claims = JwtTokenUtil.validateToken(jwtToken.replace("Bearer ","").trim());
        assertNotNull(claims.get("authorities"));
    }
}