package com.example.balanceGame.dto;

import com.example.balanceGame.entity.Enum.BoardCountResult;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardGameResultDto {
    private Long boardKey;

    private Long userKey;

    @Enumerated(EnumType.STRING)
    private BoardCountResult boardCountResult;

    private LocalDateTime boardCountTime;
}
