package com.eduardomallmann.slackintegration;

import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties
public class AppConfig {

    @Bean
    public Slack slack() {
        SlackConfig config = new SlackConfig();
        config.setPrettyResponseLoggingEnabled(true);
        config.setTokenExistenceVerificationEnabled(true);
        config.setFailOnUnknownProperties(true);
        config.setStatsEnabled(false);
        return Slack.getInstance(config);
    }

    @Bean
    CorsWebFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
