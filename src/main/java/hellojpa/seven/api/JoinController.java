package hellojpa.seven.api;

import hellojpa.seven.domain.user.UserRequestDTO;
import hellojpa.seven.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public String join(UserRequestDTO dto) {
        userService.join(dto);

        return "redirect:/";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }
}
