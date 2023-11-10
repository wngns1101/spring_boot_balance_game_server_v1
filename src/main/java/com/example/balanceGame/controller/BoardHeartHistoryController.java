package com.example.balanceGame.controller;

import com.example.balanceGame.service.BoardHeartHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Tag(name = "좋아요")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boardHeartHistory")
public class BoardHeartHistoryController {

    private final BoardHeartHistoryService boardHeartHistoryService;

    @PostMapping("/insert")
    public void insert(@RequestBody Map<String, Long> request, Principal principal) {
        Long boardKey = request.get("boardKey");
        String userName = principal.getName();
        boardHeartHistoryService.insert(boardKey, userName);
    }
}
