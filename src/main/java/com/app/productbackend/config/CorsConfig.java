// package com.app.productbackend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;

// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;

// import java.util.List;

// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsFilter corsFilter() {

//         CorsConfiguration config = new CorsConfiguration();

//         config.setAllowCredentials(true);

//         // 🔥 FRONTEND URL (VERY IMPORTANT)
//         config.setAllowedOriginPatterns(List.of("*")); // or your frontend URL

//         config.setAllowedHeaders(List.of("*"));
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);

//         return new CorsFilter(source);
//     }
// }