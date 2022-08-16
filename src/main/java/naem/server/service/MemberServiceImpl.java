package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;
import naem.server.service.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public void withdraw(String checkPassword) {

        Optional<Member> oMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername());

        if (oMember.isPresent()) {

            Member member = oMember.get();

            if (!member.matchPassword(passwordEncoder, checkPassword)) {
                throw new CustomException(WRONG_PASSWORD);
            }

            memberRepository.delete(member);
        } else {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }
}
