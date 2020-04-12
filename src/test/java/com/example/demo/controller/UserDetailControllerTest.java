package com.example.demo.controller;

import com.example.demo.config.SecurityConfig;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.DBUser;
import com.example.demo.model.SystemUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {SecurityConfig.class, UserDetailController.class})
public class UserDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(value = "user1")
    public void homePathShouldReturnStaticContent() throws Exception {
        mockMvc.perform(get("/home").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h1>Welcome</h1>")));
    }

    @Test
    @WithMockUser(value = "user2")
    public void userPathShouldReturnUserName() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user"))
                .andExpect(status().isOk()).andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        then(containsString("\"username\":\"user2\"").matches(responseContent));
    }

    @Test
    @WithMockUser(value = "user3", roles = "ADMIN")
    public void adminPathShouldReturnUserName() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin"))
                .andExpect(status().isOk()).andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        then(containsString("\"username\":\"user3\"").matches(responseContent));
    }

    @Test
    public void authenticateWithValidUserShouldReturnSuccessWithToken() throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder().userName("user6").password("pwd").build();
        ObjectMapper mapper = new ObjectMapper();
        String requestBodyJson = mapper.writeValueAsString(request);
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new SystemUser(DBUser.builder()
                        .id(1)
                        .userName("user6")
                        .password("pwd")
                        .active(true)
                        .roles("USER")
                        .build()));
        mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk());
    }

    @Test
    public void authenticateWithInvalidUserShouldThrowUnauthorizedxception() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad Credentials"));
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().userName("user4").password("pass").build();
        mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(authenticationRequest)))
                .andExpect(status().isUnauthorized());

    }

    public static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
