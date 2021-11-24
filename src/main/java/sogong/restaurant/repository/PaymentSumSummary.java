package sogong.restaurant.repository;

public interface PaymentSumSummary {
    Long getTotalCount();
    Long getTotalSale();
    Long getCashTotal();
    Long getCashTotalSale();
    Long getCardTotal();
    Long getCardTotalSale();
}