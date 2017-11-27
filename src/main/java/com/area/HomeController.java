package com.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


@Controller
@RequestMapping("/")
public class HomeController
{
    private Facebook facebook;
    private ConnectionRepository connectionRepository;
    @Autowired
    private UserRepository userRepository;


    public HomeController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @PostMapping(path="/register")
    String register(@RequestParam String FirstName, @RequestParam String LastName, @RequestParam String email, @RequestParam String passwd) {
        UserInfo n = new UserInfo();
        n.setFirstName(FirstName);
        n.setLastName(LastName);
        n.setEmail(email);
        n.setPassword(passwd);
        userRepository.save(n);
        return "redirect:/";
    }

    @GetMapping
    public String home(Model model) {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String formatted = df.format(new Date());
        String timeFormatted = tf.format(new Date());
        LocalDate d = LocalDate.now();
        LocalDate friday = d.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));

        if (connectionRepository.findPrimaryConnection(Facebook.class) != null) {
            String [] fields = { "id","name","birthday","email","location","hometown","gender","first_name","last_name"};
            User user = facebook.fetchObject("me", User.class, fields);
            String birthday=user.getBirthday();
            String email=user.getEmail();
            String gender=user.getGender();
            String firstname=user.getFirstName();
            String lastname=user.getLastName();
            String name = user.getName();
            String id = user.getId();

            model.addAttribute("id", id);
            model.addAttribute("name", name);
            model.addAttribute("birthday",birthday);
            model.addAttribute("email",email );
            model.addAttribute("gender",gender);
            model.addAttribute("firstname",firstname);
            model.addAttribute("lastname",lastname);
            model.addAttribute("facebookProfile", facebook.fetchObject("me", User.class, fields));
            PagedList<Post> feed = facebook.feedOperations().getFeed();
            model.addAttribute("feed", feed);
        }
        model.addAttribute("date", formatted);
        model.addAttribute("time", timeFormatted);
        model.addAttribute("friday", ChronoUnit.DAYS.between(d, friday));
        return "home";
    }
}