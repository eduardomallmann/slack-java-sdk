package com.eduardomallmann.slackintegration;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.context;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.slack.api.webhook.Payload;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SlackMessageConverter {

    private static final String DATE_FORMAT = "MMM dd, YYYY";

    public static Payload createPayload(final AzureAlert.AlertData alertData) {
        log.info("M=createPayload, message=Start creating payload for {}", alertData.getEssentials().getAlertId());
        final var formattedDate = formattedTime(alertData.getAlertContext().getCondition().getWindowStartTime());
        final var formattedAlertId = formattedId(alertData.getEssentials().getAlertId());
        final var essentials = alertData.getEssentials();
        final var condition = alertData.getAlertContext().getCondition().getAllOf().get(0);
        Payload slackPayload = Payload.builder()
                                       .text(":alert::alert: Alert received")
                                       .blocks(asBlocks(
                                                       header(header -> header.text(
                                                               plainText(pt ->
                                                                                 pt.emoji(true).text(":alert: Alert Received :alert:")
                                                               )
                                                       )),
                                                       context(List.of(
                                                               markdownText(
                                                                       "*" + formattedDate + "*  |  *Alert ID*: _" + formattedAlertId + "_")
                                                       )),
                                                       divider(),
                                                       section(section -> section.text(markdownText("*Description*: _" + essentials.getDescription() + "_"))),
                                                       divider(),
                                                       section(section -> section.text(markdownText(":info: *ALERT DETAILS* :info:"))),
                                                       section(section -> section.fields(List.of(
                                                               markdownText("*Alert Rule*: \n\t" + essentials.getAlertRule()),
                                                               markdownText("*Severity*: \n\t\t\t\t\t_" + essentials.getSeverity() + "_"),
                                                               markdownText("*Status*: \n\t" + condition.getMetricName() + " " + condition.getOperator() + " " + condition.getThreshold()),
                                                               markdownText("*Current Value*: \n\t\t\t\t\t\t\t_" + condition.getMetricValue() + "_")
                                                       )))
                                               )
                                       )
                                       .build();
        log.info("M=createPayload, message=Exit creating payload for {}, payload: {}", alertData.getEssentials().getAlertId(), slackPayload);
        return slackPayload;
    }

    private static String formattedId(final String alertId) {
        return alertId.substring(alertId.lastIndexOf("/") + 1);
    }

    private static String formattedTime(final LocalDateTime windowStartTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return windowStartTime.format(formatter);
    }
}
