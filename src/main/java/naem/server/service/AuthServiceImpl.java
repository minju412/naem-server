package naem.server.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.member.Member;
import naem.server.domain.member.Salt;
import naem.server.domain.member.dto.ResponseMemberDto;
import naem.server.repository.MemberRepository;
import naem.server.service.util.SaltUtil;
import naem.server.service.util.SecurityUtil;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SaltUtil saltUtil;

    @Override
    @Transactional
    public void signUpMember(Member member) throws IllegalStateException {

        Optional<Member> result = Optional.ofNullable(memberRepository.findByPhoneNumber(member.getPhoneNumber()));
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        });

        result = Optional.ofNullable(memberRepository.findByUsername(member.getUsername()));
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 사용중인 아이디입니다.");
        });

        result = Optional.ofNullable(memberRepository.findByNickname(member.getNickname()));
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 사용중인 nickname 입니다.");
        });

        String password = member.getPassword();
        String salt = saltUtil.genSalt();

        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt, password));
        memberRepository.save(member);
    }

    @Override
    public Member loginMember(String id, String password) throws ResponseStatusException {

        Member member = memberRepository.findByUsername(id);
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "멤버가 조회되지 않음");
        }

        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        if (!member.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 틀립니다.");
        }

        return member;
    }

    @Override
    @Transactional
    public ResponseMemberDto getMyUserWithAuthorities() {
        return ResponseMemberDto.from(
            SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByUsername).orElse(null));
    }

}
