package sogong.restaurant.summary;

public interface PaymentSummary {
    Long getPaymentId();
    String getMethod();
    String getPayTime();
    int getFinalPrice();

}