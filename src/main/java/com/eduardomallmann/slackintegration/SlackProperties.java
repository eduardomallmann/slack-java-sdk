package com.eduardomallmann.slackintegration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("slack")
public class SlackProperties {

    private String token;
    private String webhook;
}
