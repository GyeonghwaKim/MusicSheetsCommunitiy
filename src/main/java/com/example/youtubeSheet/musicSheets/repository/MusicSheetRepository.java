package com.example.youtubeSheet.musicSheets.repository;


import com.example.youtubeSheet.musicSheets.entitiy.MusicSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MusicSheetRepository extends JpaRepository<MusicSheet,Long> {
    List<MusicSheet> findBySiteUser_username(String username);


}
