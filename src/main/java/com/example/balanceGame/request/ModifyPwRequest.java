package com.example.balanceGame.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ModifyPwRequest {
    private String currentPw;
    private String modifyPw;
}
