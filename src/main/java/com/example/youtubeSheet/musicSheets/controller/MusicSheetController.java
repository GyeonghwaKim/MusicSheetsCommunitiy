package com.example.youtubeSheet.musicSheets.controller;


import com.example.youtubeSheet.musicSheets.service.MusicSheetService;
import com.example.youtubeSheet.musicSheets.dto.MusicSheetDto;
import com.example.youtubeSheet.musicSheets.dto.MusicSheetForm;
import com.example.youtubeSheet.user.siteuser.dto.SiteUserDto;
import com.example.youtubeSheet.user.siteuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequestMapping
@RequiredArgsConstructor
@Controller
public class MusicSheetController {

    private final MusicSheetService musicSheetService;

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/saveMusicSheets")
    public String saveMusicSheets(MusicSheetForm musicSheetForm)
    {
        return "musicSheets/saveForm";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/saveMusicSheets")
    public String saveMusicSheets(@Valid @ModelAttribute("musicSheetForm") MusicSheetForm musicSheetForm,
                                  BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()) return "musicSheets/saveForm";

        SiteUserDto siteUserDto=this.userService.getUser(principal.getName());
        this.musicSheetService.save(musicSheetForm.getTitle(), musicSheetForm.getUrl(), siteUserDto);

        return "redirect:/";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/musicSheets")
    public String showMusicSheets(@RequestParam(name = "id") Long id, Model model){
        MusicSheetDto musicSheetDto=this.musicSheetService.getSheet(id);

        model.addAttribute("url",musicSheetDto.getUrl());
        model.addAttribute("title",musicSheetDto.getTitle());

        return "musicSheets/youtube";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/musicSheets/delete/{id}")
    public String deleteMusicSheets(@PathVariable(name = "id")Long id){
        MusicSheetDto musicSheetDto =this.musicSheetService.getSheet(id);
        this.musicSheetService.delete(musicSheetDto);
        return "redirect:/";

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/musicSheets/modify/{id}")
    public String modifyMusicSheets(@PathVariable(name = "id")Long id,
                                    @Valid @ModelAttribute("modifyTitle") MusicSheetForm musicSheetForm, BindingResult bindingResult){
        MusicSheetDto musicSheetDto =this.musicSheetService.getSheet(id);
        this.musicSheetService.modify(musicSheetDto, musicSheetForm.getTitle());
        return "redirect:/musicSheets?id="+id;
    }


}
