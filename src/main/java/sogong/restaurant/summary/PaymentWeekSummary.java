package sogong.restaurant.summary;

public interface PaymentWeekSummary {
    String getWeeks();
    Long getTotalCount();
    Long getTotalSale();
    Long getCashTotal();
    Long getCashTotalSale();
    Long getCardTotal();
    Long getCardTotalSale();
}
