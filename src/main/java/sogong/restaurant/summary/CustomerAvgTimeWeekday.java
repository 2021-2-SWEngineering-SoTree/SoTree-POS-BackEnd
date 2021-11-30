package sogong.restaurant.summary;

import org.springframework.data.jpa.repository.Query;

public interface CustomerAvgTimeWeekday {
    double getTotalAvgWeekday();
    double getDinnerAvgWeekday();
    double getLunchAvgWeekday();
}