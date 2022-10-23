package com.example.recipea.dto;

import com.example.recipea.service.dto.ResponseDto;
import com.example.recipea.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mahdi Sharifi
 */
class ResponseDtoTest {

    @Test
    void testDto()  {
        ResponseDto<String> dto1= ResponseDto.<String>builder().payload(List.of("Payload")).message("success").errorCode(0).httpStatus(HttpStatus.CREATED).details("details").build();
        ResponseDto<String> dto2= ResponseDto.<String>builder().payload(List.of("Payload")).message("success").errorCode(0).httpStatus(HttpStatus.CREATED).details("details").build();

        assertEquals("success",dto1.getMessage());
        assertEquals("details",dto1.getDetails());
        assertEquals(HttpStatus.CREATED,dto1.getHttpStatus());
        assertEquals(0,dto1.getErrorCode());
        assertNotNull(dto1.getTimestamp());
        assertEquals(List.of("Payload"),dto1.getPayload());
        assertEquals(1,dto1.getPayload().size());

        assertEquals(dto1.getMessage(),dto2.getMessage());
        assertEquals(dto1.getDetails(),dto2.getDetails());
        assertEquals(dto1.getErrorCode(),dto2.getErrorCode());
        assertEquals(dto1.getHttpStatus(),dto2.getHttpStatus());
        assertEquals(dto1.getPayload(),dto2.getPayload());

    }

    @Test
    void testDto1()  {
        UserDto dto1=new UserDto("mahdi","my bearer token");
        UserDto dto2=new UserDto("mahdi","my bearer token");

        assertEquals("mahdi",dto1.getUser());
        assertEquals("my bearer token",dto1.getToken());

        assertEquals(dto1.getUser(),dto2.getUser());
        assertEquals(dto1.getToken(),dto2.getToken());

        assertThat(dto1).isEqualTo(dto2);
    }

}