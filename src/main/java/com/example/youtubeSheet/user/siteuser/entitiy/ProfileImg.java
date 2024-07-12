package com.example.youtubeSheet.user.siteuser.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProfileImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalImgName;
    private String storedImgName;


    public ProfileImg() {
        this.originalImgName = "default/defaultProfile.png";
        this.storedImgName = "default/defaultProfile.png";
    }
}
