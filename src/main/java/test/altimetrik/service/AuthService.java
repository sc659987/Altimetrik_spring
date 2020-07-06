package test.altimetrik.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import test.altimetrik.entity.UserDo;

@Log4j2
@Service
public class AuthService {
    public UserDo getByUsernameAndPassword(final String userName, final String password) {
        return new UserDo(userName, password);
    }
}
