package sogong.restaurant.repository;

public interface PaymentSummary {
    Long getPaymentId();
    String getMethod();
    String getPayTime();
    int getFinalPrice();

}