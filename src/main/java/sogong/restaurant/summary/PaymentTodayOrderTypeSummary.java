package sogong.restaurant.summary;

public interface PaymentTodayOrderTypeSummary {
    String getDate();
    Long getTableTotalCount();
    Long getTableTotalSale();
    Long getTakeOutTotalCount();
    Long getTakeOutTotalSale();
}
