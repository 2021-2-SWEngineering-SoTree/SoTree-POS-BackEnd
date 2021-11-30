package sogong.restaurant.summary;

import org.springframework.data.jpa.repository.Query;

public interface CustomerAvgTimeWeekend {
    double getTotalAvgWeekend();
    double getDinnerAvgWeekend();
    double getLunchAvgWeekend();
}