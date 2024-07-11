package com.example.mobileApp;

import com.example.mobileApp.Profile;
import com.example.mobileApp.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    @Test
    public void testGetAllProfiles() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("TestFirstName");

        List<Profile> profiles = Arrays.asList(profile);

        when(profileService.getAllProfiles()).thenReturn(profiles);

        mockMvc.perform(get("/api/profiles/getAllProfiles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':1,'firstName':'TestFirstName'}]"));
    }

    @Test
    public void testGetProfileById() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("TestFirstName");

        when(profileService.getProfileById(1L)).thenReturn(profile);

        mockMvc.perform(get("/api/profiles/getProfile/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'firstName':'TestFirstName'}"));
    }

    @Test
    public void testGetProfileByIdNotFound() throws Exception {
        when(profileService.getProfileById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/profiles/getProfile/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProfile() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setFirstName("TestFirstName");

        when(profileService.createProfile(any(Profile.class))).thenReturn(profile);

        ObjectMapper objectMapper = new ObjectMapper();
        String profileJson = objectMapper.writeValueAsString(profile);

        mockMvc.perform(post("/api/profiles/createProfile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileJson))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1,'firstName':'TestFirstName'}"));
    }

    @Test
    public void testDeleteProfile() throws Exception {
        mockMvc.perform(delete("/api/profiles/deleteProfile/1"))
                .andExpect(status().isNoContent());
    }
}
