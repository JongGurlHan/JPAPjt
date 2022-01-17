package com.JGHan.JPAPjt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

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
                .usersByUsernameQuery("select email,password,enabled "
                        + "from bael_users "
                        + "where email = ?")
                .authoritiesByUsernameQuery("select email,authority "
                        + "from authorities "
                        + "where email = ?");
    }

}