package com.example.youtubeSheet.musicSheets.dto;

import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MusicSheetDto {
    private Long id;
    private String title;
    private String url;
    private SiteUserDto siteUser;
    private LocalDate createLocalDate;
}
