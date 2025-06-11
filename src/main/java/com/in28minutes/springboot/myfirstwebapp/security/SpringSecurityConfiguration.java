package com.in28minutes.springboot.myfirstwebapp.security;

import static org.springframework.security.config.Customizer.withDefaults; // 로그인폼 관련 기본값 설정

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {
	// LDAP OR Database
	// In Memory
	
	//InMemoryUserDetailsManager
	//InMemoryUserDetailsManager(UserDetails... users)
	
	@Bean
	public InMemoryUserDetailsManager createUserDetailsManager() {

		
		UserDetails userDetails = createNewUser("in28minutes", "dummy");
		UserDetails userDetails1 = createNewUser("ranga", "test");
		
		return new InMemoryUserDetailsManager(userDetails, userDetails1);
	}

	private UserDetails createNewUser(String username, String password) {
		Function<String, String> passwordEncoder
		=input -> passwordEncoder().encode(input);
		
		UserDetails userDetails = User.builder()
									.passwordEncoder(passwordEncoder)
									.username(username)
									.password(password)
									.roles("USER","ADMIN")
									.build();
		return userDetails;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 아래는, 설정해야할 요소들. 
	// 모든 주소는, 보호됨. SecurityFilterChain 의 기능
	// 인증되지않은 요청들은, 로그인폼이 보여짐 SecurityFilterChain 의 기능
	// CSRF 비활성
	// 프레임즈
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				auth -> auth.anyRequest().authenticated()); // 일단 모든요청 승인
		http.formLogin(withDefaults()); // 로그인 양식표시 & 로그인폼관련 모든기본값 사용
		
		//http.csrf().disable(); // CSRF 비활성
		http.csrf(csrf -> csrf.disable()); // 최신 람다 방식
		//http.headers().frameOptions().disable(); // 프레임 비활성
		http.headers(headers ->
        headers.frameOptions(frameOptions -> frameOptions.disable())); // 최신 방식
		
		return http.build();
	}
}
