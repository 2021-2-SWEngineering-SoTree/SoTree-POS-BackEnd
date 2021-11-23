package sogong.restaurant.repository;

public interface PaymentMonthSummary {
    String getMonths();
    Long getTotalCount();
    Long getTotalPrice();
    Long getCardTotalPrice();
    Long getCardTotal();
    Long getCashTotalPrice();
    Long getCashTotal();
}
