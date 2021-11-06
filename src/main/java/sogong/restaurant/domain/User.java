package sogong.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String password;
    private String userName;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private String birthDay;
    private String phoneNumber;
    private String email;

}
