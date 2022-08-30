package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import naem.server.domain.member.Member;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetailsServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        return new org
            .springframework
            .security
            .core
            .userdetails
            .User(member.getUsername(), member.getPassword(), grantedAuthorities);
    }
}
