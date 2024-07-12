package com.example.youtubeSheet.musicSheets.entitiy;

import com.example.youtubeSheet.user.siteuser.entitiy.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MusicSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String url;

    @ManyToOne
    private SiteUser siteUser;

    private LocalDate createLocalDate;

}
