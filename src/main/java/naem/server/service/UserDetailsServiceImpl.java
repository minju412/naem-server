package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import naem.server.domain.member.Member;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<Member> oMember = memberRepository.findByUsername(username);
        if (oMember.isEmpty()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        Member member = oMember.get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        return new org
            .springframework
            .security
            .core
            .userdetails
            .User(member.getUsername(), member.getPassword(), grantedAuthorities);
    }
}
