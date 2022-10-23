package com.example.recipea.controller;

import com.example.recipea.IntegrationTest;
import com.example.recipea.security.JwtTokenUtil;
import com.example.recipea.service.dto.UserDto;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mahdi Sharifi
 */

@IntegrationTest
@DisplayName("User controller Integration test")
 class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

   @Test
    void shouldReturnTokenAndUser_whenLoginIsCalled() throws Exception {

       MvcResult result =mockMvc.perform( MockMvcRequestBuilders.post("/v1/users/login?username=mahdi")
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.user").value("mahdi"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists())
               .andReturn();

       UserDto userDto= new Gson().fromJson(result.getResponse().getContentAsString(),UserDto.class);
       Claims claims = JwtTokenUtil.validateToken(userDto.getToken().replace("Bearer ","").trim());
       assertNotNull(claims.get("authorities"));

   }

}
