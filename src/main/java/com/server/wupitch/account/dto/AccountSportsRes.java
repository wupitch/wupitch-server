package com.server.wupitch.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSportsRes {

    private Long accountId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SportsInfo{
        private Long sportsId;
        private String sportsName;
    }

    private List<SportsInfo> list = new ArrayList<>();

}
