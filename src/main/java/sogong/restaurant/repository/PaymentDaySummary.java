package sogong.restaurant.repository;

public interface PaymentDaySummary {
    int getDateRange();
    Long getTotal();
    Long getTotalSale();
}
