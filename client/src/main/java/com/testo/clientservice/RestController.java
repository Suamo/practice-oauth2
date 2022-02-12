package com.testo.clientservice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RestController {
    private final AuthState authState;

    public RestController(AuthState authState) {
        this.authState = authState;
    }

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("login");
    }

    @GetMapping("/secured")
    public String secured(Model model) {
        model.addAttribute("clientAccess", authState.isClientAccess());
        model.addAttribute("serviceAccess", authState.isServiceAccess());
        model.addAttribute("serviceMessage", authState.getServiceMessage());
        return "secured";
    }

}
