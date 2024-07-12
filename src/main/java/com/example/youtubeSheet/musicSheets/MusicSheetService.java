package com.example.youtubeSheet.musicSheets;


import com.example.youtubeSheet.exception.DataNotFoundException;
import com.example.youtubeSheet.musicSheets.dto.MusicSheetDto;
import com.example.youtubeSheet.musicSheets.entitiy.MusicSheet;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MusicSheetService {

    private final MusicSheetRepository musicSheetRepository;
    private final ModelMapper modelMapper;

    public void save(String title,String url, SiteUserDto siteUserDto){

        MusicSheetDto musicSheetDto =new MusicSheetDto();
        musicSheetDto.setTitle(title);
        musicSheetDto.setUrl(url);
        musicSheetDto.setSiteUser(siteUserDto);
        musicSheetDto.setCreateLocalDate(LocalDate.now());

        musicSheetRepository.save(of(musicSheetDto));

    }

    public MusicSheetDto getSheet(Long id){
        Optional<MusicSheet> _musicSheet=this.musicSheetRepository.findById(id);

        if(_musicSheet.isPresent())  return of(_musicSheet.get());
        else throw new DataNotFoundException("MusicSheet Not Found");
    }

    public List<MusicSheet> showSheetList(SiteUserDto siteUserDto){
        return musicSheetRepository.findBySiteUser_username(siteUserDto.getUsername());
    }

    public void delete(MusicSheetDto musicSheetDto){
        this.musicSheetRepository.delete(of(musicSheetDto));
    }

    public void modify(MusicSheetDto musicSheetDto, String title){
        musicSheetDto.setTitle(title);
        this.musicSheetRepository.save(of(musicSheetDto));

    }

    private MusicSheet of(MusicSheetDto musicSheetDto){
        return modelMapper.map(musicSheetDto,MusicSheet.class);
    }

    private MusicSheetDto of(MusicSheet musicSheet){
        return modelMapper.map(musicSheet,MusicSheetDto.class);
    }

}
