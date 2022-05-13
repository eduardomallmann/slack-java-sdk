package com.eduardomallmann.slackintegration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureAlert {

    private String schemaId;
    private AlertData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AlertData {
        private AlertEssentials essentials;
        private AlertContext alertContext;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AlertEssentials {
        private String alertId;
        private String alertRule;
        private String severity;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AlertContext {
        private AlertCondition condition;


    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AlertCondition {
        private List<AllOfCondition> allOf;
        private LocalDateTime windowStartTime;
        private LocalDateTime windowEndTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class AllOfCondition {
        private String metricName;
        private String operator;
        private String threshold;
        private String timeAggregation;
        private Integer metricValue;
    }
}
