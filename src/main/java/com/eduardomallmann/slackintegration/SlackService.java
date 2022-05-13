package com.eduardomallmann.slackintegration;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.webhook.WebhookPayloads.payload;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.webhook.WebhookResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackService {

    private final Slack slack;
    private final SlackProperties slackProperties;

    public ChatPostMessageResponse sendPlainPostMessage(final String channel, final String text) throws Exception {
        log.info("M=sendPlainPostMessage, message=Start sending message: {}", text);
        try {
            ChatPostMessageResponse response = slack.methods(slackProperties.getToken()).chatPostMessage(req -> req.channel(channel).text(text));
            if (response.isOk()) {
                log.info("M=sendPlainPostMessage, message=Successful operation: {}", response.getMessage());
            } else {
                log.error("M=sendPlainPostMessage, message=Error on operation: {}", response.getError());
            }
            return response;
        } catch (SlackApiException requestFailure) {
            log.error("M=sendPlainPostMessage, message=SlackApiException thrown: {}", requestFailure.getResponseBody());
            throw new Exception(requestFailure.getMessage());
        } catch (IOException connectivityIssue) {
            log.error("M=sendPlainPostMessage, message=Exception by connectivity issue thrown: {}", connectivityIssue.getMessage());
            throw new Exception(connectivityIssue.getMessage());
        }
    }

    public ChatPostMessageResponse sendBlockPostMessage(final String channel, final String text) throws Exception {
        log.info("M=sendBlockPostMessage, message=Start sending message: {}", text);
        try {
            ChatPostMessageResponse response = slack.methods(slackProperties.getToken())
                                                       .chatPostMessage(req -> req
                                                                                       .channel(channel)
                                                                                       .text("Service: " + text + " in critical problem")
                                                                                       .blocks(asBlocks(
                                                                                               header(header -> header.text(
                                                                                                       plainText(
                                                                                                               pt -> pt.emoji(true)
                                                                                                                             .text(":rotating_light::rotating_light: Critical Service :rotating_light::rotating_light:")
                                                                                                       )
                                                                                               )),
                                                                                               divider(),
                                                                                               section(section -> section.text(markdownText("*Service:* " + text)))
                                                                                       ))
                                                       );
            if (response.isOk()) {
                log.info("M=sendBlockPostMessage, message=Successful operation: {}", response.getMessage());
            } else {
                log.error("M=sendBlockPostMessage, message=Error on operation: {}", response.getError());
            }
            return response;
        } catch (SlackApiException requestFailure) {
            log.error("M=sendBlockPostMessage, message=SlackApiException thrown: {}", requestFailure.getResponseBody());
            throw new Exception(requestFailure.getMessage());
        } catch (IOException connectivityIssue) {
            log.error("M=sendBlockPostMessage, message=Exception by connectivity issue thrown: {}", connectivityIssue.getMessage());
            throw new Exception(connectivityIssue.getMessage());
        }
    }

    public WebhookResponse sendWebhookPlainMessage(final String text) throws Exception {
        log.info("M=sendWebhookPlainMessage, message=Start sending message: {}", text);
        try {
            return slack.send(slackProperties.getWebhook(), payload(p -> p.text(":rotating_light::rotating_light:" + text)));
        } catch (Exception e) {
            log.error("M=sendPlainPostMessage, message=Exception thrown: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public WebhookResponse sendWebhookBlockMessage(final String text) throws Exception {
        log.info("M=sendWebhookBlockMessage, message=Start sending message: {}", text);
        try {
            return slack.send(slackProperties.getWebhook(),
                    payload(p -> p.text("Service: " + text + " in critical problem")
                                         .blocks(asBlocks(
                                                 header(header -> header.text(
                                                         plainText(
                                                                 pt -> pt.emoji(true)
                                                                               .text(":rotating_light::rotating_light: Critical Service :rotating_light::rotating_light:")
                                                         )
                                                 )),
                                                 divider(),
                                                 section(section -> section.text(markdownText("*Service:* " + text)))
                                         ))
                    ));
        } catch (Exception e) {
            log.error("M=sendWebhookBlockMessage, message=Exception thrown: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public WebhookResponse sendWebhookBlockAlert(final AzureAlert azureAlert) throws Exception {
        log.info("M=sendWebhookBlockAlert, message=Start sending alert: {}", azureAlert);
        try {
            return slack.send(slackProperties.getWebhook(), SlackMessageConverter.createPayload(azureAlert.getData()));
        } catch (Exception e) {
            log.error("M=sendWebhookBlockMessage, message=Exception thrown: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

}
