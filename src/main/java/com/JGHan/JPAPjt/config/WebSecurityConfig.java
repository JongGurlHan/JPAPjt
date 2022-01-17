package com.JGHan.JPAPjt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource; //application.properties에있는 dataSource를 넘겨줘서 인증처리

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/home").permitAll() //로그인 안해도 접근가능한 페이지
                    .anyRequest().authenticated() //로그인해야 접근 가능 페이지
                    .and()
                .formLogin()
                    .loginPage("/login") //로그인 페이지, 만약 로그인 안하고 다른페이지 접근시도하면 여기로 redirect
                    .permitAll() // 누구나 로그인할 수 있도록
                    .and()
                .logout()
                    .permitAll(); //누구나 로그아웃할 수 있도록
    }

    //스프링 내부에서 인증처리
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder((passwordEncoder())) //스프링에서 인증처리할때 인코더를 이용해서 비밀번호 암호화
                .usersByUsernameQuery("select username,password,enabled " // 순서대로 해야한다.
                        + "from user "  //뒤에 공백주의!!
                        + "where username = ?") //알아서 ?값이 들어간다.
                .authoritiesByUsernameQuery("select username, name "
                        + "from authorities "
                        + "where email = ?");
    }
    //Authentication: 로그인
    //Authroization: 권한


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}