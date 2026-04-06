package com.shopeasy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@Slf4j
public class SnsService {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sns.order-topic-arn:}")
    private String orderTopicArn;

    public void sendOrderConfirmation(String email, Long orderId, String amount) {
        if (orderTopicArn.isEmpty()) {
            log.info("SNS not configured — skipping notification for order {}", orderId);
            return;
        }
        try {
            SnsClient sns = SnsClient.builder()
                    .region(Region.of(region))
                    .build();

            String message = String.format(
                "Order Confirmed!\nOrder ID: #%d\nAmount: Rs.%s\nEmail: %s",
                orderId, amount, email);

            sns.publish(PublishRequest.builder()
                    .topicArn(orderTopicArn)
                    .subject("ShopEasy Order #" + orderId + " Confirmed")
                    .message(message)
                    .build());

            log.info("SNS notification sent for order {}", orderId);
        } catch (Exception e) {
            log.error("SNS notification failed: {}", e.getMessage());
        }
    }
}