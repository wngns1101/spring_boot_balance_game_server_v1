package com.example.balanceGame.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="회원")
@RestController
@RequestMapping("/member")
public class memberController {
    @PostMapping("/join")
    public void join() {

    }
}
