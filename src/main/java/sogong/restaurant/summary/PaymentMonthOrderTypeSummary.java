package sogong.restaurant.summary;

public interface PaymentMonthOrderTypeSummary {
    String getMonth();
    Long getTableTotalCount();
    Long getTableTotalSale();
    Long getTakeOutTotalCount();
    Long getTakeOutTotalSale();
}