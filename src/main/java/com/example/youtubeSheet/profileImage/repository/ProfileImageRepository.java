package com.example.youtubeSheet.profileImage.repository;

import com.example.youtubeSheet.profileImage.Entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {
}
