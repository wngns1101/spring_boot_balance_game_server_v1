package com.example.balanceGame.entity;

import com.example.balanceGame.entity.Enum.BoardCountResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameResult {
    @Id @GeneratedValue
    @Column(name = "board_count_key")
    private Long boardCountKey;

    private Long boardKey;

    private Long userKey;

    @Enumerated(EnumType.STRING)
    private BoardCountResult boardCountResult;

    private LocalDateTime boardCountTime;
}
