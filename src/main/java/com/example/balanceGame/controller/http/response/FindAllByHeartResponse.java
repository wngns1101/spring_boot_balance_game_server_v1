package com.example.balanceGame.controller.http.response;

import com.example.balanceGame.dto.FindAllBoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllByHeartResponse {
    private String message;
    private List<FindAllBoardDto> findAllBoardDtos;
}
