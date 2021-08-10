package com.escanor.acadperfapi;

import com.escanor.acadperfapi.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AcademicPerformanceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademicPerformanceApiApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean() {
	    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
	    AuthFilter authFilter = new AuthFilter();
	    registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/users/create");
		registrationBean.addUrlPatterns("/api/users/info/*");
		registrationBean.addUrlPatterns("/api/reports/*");
		registrationBean.addUrlPatterns("/api/comments/*");
		registrationBean.addUrlPatterns("/api/subjects/*");
		return registrationBean;
	}
}
