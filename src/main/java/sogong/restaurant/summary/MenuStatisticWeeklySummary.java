package sogong.restaurant.summary;

public interface MenuStatisticWeeklySummary {
    Long getQuantity();
    Long getTotalCount();
    String getMenuName();
    int getPrice();
}
