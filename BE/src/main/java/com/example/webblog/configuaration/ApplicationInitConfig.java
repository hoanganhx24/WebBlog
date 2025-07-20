package com.example.webblog.configuaration;

import com.example.webblog.enums.Role;
import com.example.webblog.entity.User;
import com.example.webblog.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    PasswordEncoder  passwordEncoder;

    @Value("${admin.username}")
    String ADMIN_USERNAME;

    @Value("${admin.password}")
    String ADMIN_PASSWORD;

    @Bean
//    @ConditionalOnProperty(
//            prefix = "spring",
//            value = "datasource.driverClassName",
//            havingValue = "com.mysql.cj.jdbc.Driver"
//    )
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User user = User.builder()
                        .role(Role.ADMIN)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .email("admin@gmail.com")
                        .firstName("Admin")
                        .lastName("")
                        .isActive(true)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
