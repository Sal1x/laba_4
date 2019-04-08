package com.example.lab4.laba_4.controller;

import com.example.lab4.laba_4.domain.ResultEntity;
import com.example.lab4.laba_4.repo.ResultRepository;
import com.example.lab4.laba_4.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping()
public class HomeController {

    private final
    ResultRepository resultRepository;
    private final
    UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public HomeController(ResultRepository resultRepository, UserRepository userRepository) {
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/get_hits")
    public LinkedList<ResultEntity> get_hits(@AuthenticationPrincipal User user) {
        LinkedList<ResultEntity> result = new LinkedList<>();
        log.info("Is it works?");
        resultRepository.findByOwner(userRepository.findByUsername(user.getUsername()))
                .forEach(result::add);
        log.info("abc");
        System.out.println(userRepository.findByUsername(user.getUsername()));
        return result;
    }

    @GetMapping("/hit")
    public ResultEntity hit(@RequestParam double x,
                       @RequestParam double y,
                       @RequestParam double r,
                       @AuthenticationPrincipal User user) {

        System.out.println("x:" + x);
        log.info("x:" + x);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setX(x);
        resultEntity.setY(y);
        resultEntity.setR(r);
        boolean match = false;

        if (x <= 0 && y <= 0) {
            if (x >= -(r / 2) && y >= -r) {
                match = true;
            }
        }
        if (x <= 0 && y >= 0) {
            if (y <= x + r) {
                match = true;
            }
        }
        if (x >= 0 && y <= 0) {
            if (x * x + y * y <= r * r) {
                match = true;
            }
        }
        resultEntity.setMatch(match);
        resultEntity.setOwner(userRepository.findByUsername(user.getUsername()));
        resultRepository.save(resultEntity);
        return resultEntity;
    }

    @RequestMapping(value = "/lol")
    public String lol() {
        System.out.println("313313");
        log.info("311 333 131");
        return "313";
    }
}
