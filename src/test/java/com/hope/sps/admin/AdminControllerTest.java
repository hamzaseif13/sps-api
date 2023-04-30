package com.hope.sps.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hope.sps.dto.RegisterRequest;
import com.hope.sps.jwt.JWTAuthFilter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AdminController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTAuthFilter.class)
)
class AdminControllerTest {

    private static final String BASE_URL = "/api/v1/admin";

    @MockBean
    private AdminService adminService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private AdminDTO testAdminDTO1;

    private AdminDTO testAdminDTO2;

    @BeforeEach
    void prepareTestData() {
        testAdminDTO1 = new AdminDTO(1L, "John", "Doe", "JohnDoe@gmail.com");
        testAdminDTO2 = new AdminDTO(2L, "Mike", "James", "MikeJames@gmail.com");
    }

    @Test
    @DisplayName("GET v1/api/admin getAllAdmins no data")
    @WithMockUser(roles = "ADMIN")
    @SneakyThrows
    void testRegister_noAdminsFound_shouldReturn204NoContent() {
        //given
        //when
        Mockito.when(adminService.getAllAdmins()).thenReturn(Collections.emptyList());

        //then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isNoContent())
                .andDo(print());

        Mockito.verify(adminService, times(1)).getAllAdmins();
    }

    @Test
    @DisplayName("GET v1/api/admin getAllAdmins valid data")
    @WithMockUser(roles = "ADMIN")
    @SneakyThrows
    public void testGetAllAdmins_adminsFound_shouldReturn200OkAndAdminDTOs() {
        //given
        final List<AdminDTO> adminDTOS = getTestAdminDTOList();

        //when
        Mockito.when(adminService.getAllAdmins()).thenReturn(adminDTOS);

        //then
        mockMvc.perform(get(BASE_URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(adminDTOS.size()))
                .andDo(print());

        Mockito.verify(adminService, times(1)).getAllAdmins();
    }

    @Test
    @DisplayName("POST v1/api/admin register valid RegistrationRequest")
    @WithMockUser(roles = "ADMIN")
    @SneakyThrows
    void testRegister_validRegistrationRequest_ShouldReturn200OkAndGeneratedId() {
        //given
        final RegisterRequest validRegistrationRequest = new RegisterRequest(
                "Ahmad",
                "AliAli",
                "Ahmad@gmail.com",
                "Ahmad1234"
        );

        final String validRegistrationRequestAsJson = mapper.writeValueAsString(validRegistrationRequest);

        final Long generatedId = 1L;

        //when
        Mockito.when(adminService.registerAdmin(validRegistrationRequest))
                .thenReturn(generatedId);

        //then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRegistrationRequestAsJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("POST v1/api/admin register valid RegistrationRequest")
    @WithMockUser(roles = "ADMIN")
    @SneakyThrows
    void testDeleteAdminById() {
        final Long id = 1L;

        Mockito.doNothing().when(adminService).deleteAdminById(id);

        mockMvc.perform(delete(BASE_URL + "/%s".formatted(id)))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    private List<AdminDTO> getTestAdminDTOList() {
        return List.of(testAdminDTO1, testAdminDTO2);
    }
}