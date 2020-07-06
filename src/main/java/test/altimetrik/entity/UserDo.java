package test.altimetrik.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDo {
    private String userName;
    private String password;
}
