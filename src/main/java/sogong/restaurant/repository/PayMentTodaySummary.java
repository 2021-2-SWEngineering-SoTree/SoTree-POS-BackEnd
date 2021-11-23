package sogong.restaurant.repository;

public interface PayMentTodaySummary {
    Long getYearPrice();
    Long getYearCount();
    Long getWeekCount();
    Long getWeekPrice();
    Long getMonthPrice();
    Long getMonthCount();
    Long getTodayPrice();
    Long getTodayCount();
}
