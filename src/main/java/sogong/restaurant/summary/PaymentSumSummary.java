package sogong.restaurant.summary;

public interface PaymentSumSummary {
    Long getTotalCount();
    Long getTotalSale();
    Long getCashTotal();
    Long getCashTotalSale();
    Long getCardTotal();
    Long getCardTotalSale();
}