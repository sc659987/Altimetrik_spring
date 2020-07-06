package test.altimetrik.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import test.altimetrik.entity.UserDo;

import java.security.Principal;

@RestController
public class DemoController {
    @GetMapping("/getGreeting")
    public UserDo getUserDetail(Principal principal) {
        return new UserDo(principal.getName(), null);
    }
}
