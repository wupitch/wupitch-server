package com.server.wupitch.club.dto;

import com.server.wupitch.club.Club;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestInfoRes {

    private Long clubId;

    private Integer guestDue;

    private List<LocalDate> localDates = new ArrayList<>();

    private List<String> days = new ArrayList<>();

    public GuestInfoRes(Club club) {
        this.clubId = club.getClubId();

        this.guestDue = club.getGuestConference();
        if (guestDue == null) this.guestDue = 0;

        int cnt = 0;

        LocalDate now = LocalDate.now();

        while (true) {
            now = now.plusDays(1);
            if (cnt == 3) break;
            int dayIdx = now.getDayOfWeek().getValue();
            if (dayIdx == 1) {
                if (club.getMonday() != null && club.getMonday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("월요일");
                }
            }
            else if (dayIdx == 2) {
                if (club.getTuesday() != null && club.getTuesday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("화요일");
                }
            }
            else if (dayIdx == 3) {
                if (club.getWednesday() != null && club.getWednesday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("수요일");
                }
            }
            else if (dayIdx == 4) {
                if (club.getThursday() != null && club.getThursday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("목요일");
                }
            }
            else if (dayIdx == 5) {
                if (club.getFriday() != null && club.getFriday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("금요일");
                }
            }
            else if (dayIdx == 6) {
                if (club.getSaturday() != null && club.getSaturday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("토요일");
                }
            }
            else if (dayIdx == 7) {
                if (club.getSunday() != null && club.getSunday()) {
                    cnt++;
                    this.localDates.add(now);
                    this.days.add("일요일");
                }
            }

        }
    }

}
