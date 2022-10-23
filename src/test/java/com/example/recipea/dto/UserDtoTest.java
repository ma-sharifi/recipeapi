package com.example.recipea.dto;

import com.example.recipea.service.dto.IngredientDto;
import com.example.recipea.service.dto.UserDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mahdi Sharifi
 */
class UserDtoTest {

    @Test
    void testDto()  {
        UserDto dto1=new UserDto("mahdi","my bearer token");
        UserDto dto2=new UserDto("mahdi","my bearer token");

        assertEquals("mahdi",dto1.getUser());
        assertEquals("my bearer token",dto1.getToken());

        assertEquals(dto1.getUser(),dto2.getUser());
        assertEquals(dto1.getToken(),dto2.getToken());

        assertThat(dto1).isEqualTo(dto2);
    }
}