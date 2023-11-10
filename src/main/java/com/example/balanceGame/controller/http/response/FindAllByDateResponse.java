package com.example.balanceGame.controller.http.response;

import com.example.balanceGame.dto.FindAllByDateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAllByDateResponse {
    private String message;
    private List<FindAllByDateDto> findAllByDateDtos;
}
