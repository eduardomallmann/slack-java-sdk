package com.eduardomallmann.slackintegration;

import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.webhook.WebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppRestController {

    private final SlackService slackService;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("chat-message/plain/{channel}")
    public ChatPostMessageResponse postPlainChatMessage(@PathVariable("channel") final String channel, @RequestBody final PlainTextMessage message) throws Exception {
        log.info("M=postPlainChatMessage, message=Enter chat-message/plain/{channel}, channel={}, message={}", channel, message);
        return slackService.sendPlainPostMessage(channel, message.getText());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("chat-message/block/{channel}")
    public ChatPostMessageResponse postBlockChatMessage(@PathVariable("channel") final String channel, @RequestBody final PlainTextMessage message) throws Exception {
        log.info("M=postBlockChatMessage, message=Enter chat-message/block/{channel}, channel={}, message={}", channel, message);
        return slackService.sendBlockPostMessage(channel, message.getText());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("webhook/plain")
    public WebhookResponse postPlainWebhookMessage(@RequestBody final PlainTextMessage message) throws Exception {
        log.info("M=postPlainWebhookMessage, message=Enter webhook/plain, message={}", message);
        return slackService.sendWebhookPlainMessage(message.getText());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("webhook/block")
    public WebhookResponse postBlockWebhookMessage(@RequestBody final PlainTextMessage message) throws Exception {
        log.info("M=postBlockWebhookMessage, message=Enter webhook/plain, message={}", message);
        return slackService.sendWebhookBlockMessage(message.getText());
    }
}
