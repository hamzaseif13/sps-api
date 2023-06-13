package com.hope.sps.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hope.sps.admin.Admin;
import com.hope.sps.admin.AdminDTO;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerDTO;
import com.hope.sps.officer.Officer;
import com.hope.sps.officer.OfficerDTO;
import com.hope.sps.user_information.UserDetailsServiceImpl;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneDTO;
import com.hope.sps.zone.ZoneRegistrationRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper getModelMapper() {
        var mapper = new ModelMapper();
        configureModelMapper(mapper);

        return mapper;
    }

    private void configureModelMapper(final ModelMapper mapper) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // ADMIN TO ADMIN_DTO mapping config
        mapper.createTypeMap(Admin.class, AdminDTO.class)
                .addMapping(src -> src.getUserInformation().getFirstName(), AdminDTO::setFirstName)
                .addMapping(src -> src.getUserInformation().getLastName(), AdminDTO::setLastName)
                .addMapping(src -> src.getUserInformation().getEmail(), AdminDTO::setEmail);

        // CUSTOMER TO CUSTOMER_DTO mapping config
        mapper.createTypeMap(Customer.class, CustomerDTO.class)
                .addMapping(src -> src.getUserInformation().getFirstName(), CustomerDTO::setFirstName)
                .addMapping(src -> src.getUserInformation().getLastName(), CustomerDTO::setLastName)
                .addMapping(src -> src.getUserInformation().getEmail(), CustomerDTO::setEmail);

        // ZONE TO ZONE_DTO mapping config
        mapper.createTypeMap(Zone.class, ZoneDTO.class)
                .addMapping(src -> src.getLocation().getAddress(), ZoneDTO::setAddress)
                .addMapping(src -> src.getLocation().getLng(), ZoneDTO::setLng)
                .addMapping(src -> src.getLocation().getLat(), ZoneDTO::setLat);

        // ZoneRegistrationRequest TO ZONE mapping config
        mapper.createTypeMap(ZoneRegistrationRequest.class, Zone.class)
                .addMapping(ZoneRegistrationRequest::getAddress, Zone::setAddress)
                .addMapping(ZoneRegistrationRequest::getLng, Zone::setLng)
                .addMapping(ZoneRegistrationRequest::getLat, Zone::setLat);

        // OFFICER TO OFFICER_DTO mapping config
        mapper.createTypeMap(Officer.class, OfficerDTO.class)
                .addMapping(src -> src.getUserInformation().getFirstName(), OfficerDTO::setFirstName)
                .addMapping(src -> src.getUserInformation().getLastName(), OfficerDTO::setLastName)
                .addMapping(src -> src.getUserInformation().getEmail(), OfficerDTO::setEmail);
    }

    @Bean
    public ObjectMapper objectMapper() {
        // ObjectMapper config so a jackson library can serialize LocalDateTime and other modern java time classes

        final var objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}
