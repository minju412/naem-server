package naem.server.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import naem.server.domain.member.Member;
import naem.server.exception.UserNotFoundException;
import naem.server.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(UserNotFoundException::new);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        return new org
            .springframework
            .security
            .core
            .userdetails
            .User(member.getUsername(), member.getPassword(), grantedAuthorities);
    }
}
