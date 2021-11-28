package sogong.restaurant.summary;

import org.springframework.data.jpa.repository.Query;

public interface CustomerAvgTimeALL {
    double getTotalAvg();
    double getDinnerAvg();
    double getLunchAvg();
}