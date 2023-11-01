package com.example.balanceGame.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="댓글 신고")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/commentReport")
public class CommentReportController {
}
