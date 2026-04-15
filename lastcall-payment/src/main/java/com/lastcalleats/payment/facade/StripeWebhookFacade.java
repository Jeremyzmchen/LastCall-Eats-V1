package com.lastcalleats.payment.facade;

import com.lastcalleats.payment.config.StripeConfig;
import com.lastcalleats.payment.dto.WebhookRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Stripe Webhook 解析门面，封装签名验证、事件反序列化和 metadata 提取的全部细节。
 * Controller 只调用 {@link #parse} 拿到标准化的 {@link WebhookRequest}，
 * 不直接依赖 Stripe SDK 的任何具体类，便于测试和未来替换支付平台。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StripeWebhookFacade {

    private final StripeConfig stripeConfig;

    /**
     * 解析 Stripe Webhook 请求，返回标准化的业务对象。
     * 验签失败或无法解析时返回 empty，由 Controller 决定响应码。
     *
     * @param payload   HTTP 请求原始 body
     * @param sigHeader Stripe-Signature 请求头
     * @return 解析成功的 WebhookRequest，失败时返回 empty
     */
    public Optional<WebhookRequest> parse(String payload, String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeConfig.getWebhookSecret());
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe webhook signature");
            return Optional.empty();
        }

        Optional<StripeObject> stripeObject = event.getDataObjectDeserializer().getObject();
        if (stripeObject.isEmpty()) {
            log.warn("Failed to deserialize Stripe event: {}", event.getType());
            return Optional.empty();
        }

        if (!(stripeObject.get() instanceof PaymentIntent intent)) {
            return Optional.empty();
        }

        String orderIdStr = intent.getMetadata().get("orderId");
        if (orderIdStr == null) {
            log.warn("PaymentIntent missing orderId metadata: {}", intent.getId());
            return Optional.empty();
        }

        WebhookRequest request = new WebhookRequest();
        request.setEventId(event.getId());
        request.setEventType(event.getType());
        request.setOrderId(Long.parseLong(orderIdStr));
        return Optional.of(request);
    }
}
