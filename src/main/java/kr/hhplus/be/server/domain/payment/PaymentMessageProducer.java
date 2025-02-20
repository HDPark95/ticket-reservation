package kr.hhplus.be.server.domain.payment;

public interface PaymentMessageProducer {
    void publishPaymentComplete(Long paymentId);
}
