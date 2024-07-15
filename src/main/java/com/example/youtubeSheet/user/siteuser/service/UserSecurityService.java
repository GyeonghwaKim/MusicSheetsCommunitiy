package com.example.youtubeSheet.user.siteuser.service;

import com.example.youtubeSheet.user.siteuser.repository.UserRepository;
import com.example.youtubeSheet.user.siteuser.entitiy.UserRole;
import com.example.youtubeSheet.user.siteuser.entitiy.SiteUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<SiteUser> _siteUser=userRepository.findByUsername(username);
        if(_siteUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        }

        SiteUser siteUser=_siteUser.get();
        List<GrantedAuthority> authorities=new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}
