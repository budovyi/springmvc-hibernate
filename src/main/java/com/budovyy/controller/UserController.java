package com.budovyy.controller;

import com.budovyy.controller.external.model.UserRegistration;
import com.budovyy.model.User;
import com.budovyy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login", method = GET)
    public ModelAndView login(ModelAndView vm) {
        vm.addObject("user", new User());
        vm.setViewName("login");
        return vm;
    }

    /*@RequestMapping(value = "/login", method = POST)
    public String login(@ModelAttribute User user) {
        User u = userService.getByUsername(user.getUsername());
        // some logic
        return "login";
    }*/

    @RequestMapping(value = "/register", method = GET)
    public ModelAndView register(ModelAndView vm) {
        vm.addObject("userRegistration", new UserRegistration());
        vm.setViewName("register");
        return vm;
    }

    @RequestMapping(value = "/register", method = POST)
    public ModelAndView register(@ModelAttribute @Valid UserRegistration ur, BindingResult br, ModelAndView vm) {
        if (br.hasErrors()) {
            return new ModelAndView("register", br.getModel());
        }
        User user = UserRegistration.of(ur);
        vm.addObject("user", userService.register(user));
        vm.setViewName("welcome");
        return vm;
    }

    @RequestMapping(value = "/email-verification/{token}", method = GET)
    public ModelAndView emailVerification(@PathVariable String token, ModelAndView vm) {
        vm.setViewName("success-verification");
        vm.addObject("user", userService.emailVerification(token));
        return vm;
    }
}
