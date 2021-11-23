package sogong.restaurant.repository;

public interface PaymentWeeklySummary {
    String getDate();
    Long getTotalSale();
    Long getTotalCount();
}
