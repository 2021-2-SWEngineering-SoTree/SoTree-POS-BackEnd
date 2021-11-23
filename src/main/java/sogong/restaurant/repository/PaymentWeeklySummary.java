package sogong.restaurant.repository;

public interface PaymentWeeklySummary {
    String getDate();
    Long getTotalPrice();
    Long getTotalCount();
}
