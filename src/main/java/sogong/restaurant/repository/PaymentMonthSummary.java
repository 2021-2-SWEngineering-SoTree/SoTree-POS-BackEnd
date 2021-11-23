package sogong.restaurant.repository;

public interface PaymentMonthSummary {
    String getMonths();
    Long getTotalCount();
    Long getTotalSale();
    Long getCardTotalSale();
    Long getCardTotal();
    Long getCashTotalSale();
    Long getCashTotal();
}
