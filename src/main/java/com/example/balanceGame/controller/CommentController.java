package com.example.balanceGame.controller;

import com.example.balanceGame.request.CommentRequest;
import com.example.balanceGame.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name="댓글")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/regist")
    public ResponseEntity regist(@RequestBody CommentRequest commentRequest, Principal principal) {
        return commentService.regist(commentRequest, principal);
    }
}
