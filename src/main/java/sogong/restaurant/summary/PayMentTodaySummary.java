package sogong.restaurant.summary;

public interface PayMentTodaySummary {
    Long getYearSale();
    Long getYearCount();
    Long getWeekCount();
    Long getWeekSale();
    Long getMonthSale();
    Long getMonthCount();
    Long getTodaySale();
    Long getTodayCount();
    Long getTodayCashTotalSale();
    Long getTodayCashTotal();
    Long getTodayCardTotalSale();
    Long getTodayCardTotal();
}
