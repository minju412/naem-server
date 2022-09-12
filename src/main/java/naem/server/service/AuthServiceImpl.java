package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naem.server.config.JwtTokenProvider;
import naem.server.domain.member.Member;
import naem.server.domain.member.dto.MemberConflictCheckDto;
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.TokenDto;
import naem.server.exception.CustomException;
import naem.server.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MemberRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;
    private long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    @Override
    @Transactional
    public void signUp(SignUpReq signUpReq) {

        if (userRepository.existsByPhoneNumber(signUpReq.getPhoneNumber())) {
            throw new CustomException(DUPLICATE_MEMBER);
        }
        if (userRepository.existsByUsername(signUpReq.getUsername())) {
            throw new CustomException(CONFLICT_ID);
        }
        if (userRepository.existsByNickname(signUpReq.getNickname())) {
            throw new CustomException(CONFLICT_NICKNAME);
        }
        Member newUser = signUpReq.toUserEntity();
        newUser.hashPassword(bCryptPasswordEncoder);

        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void isConflict(MemberConflictCheckDto memberConflictCheckDto) {
        if (userRepository.existsByUsername(memberConflictCheckDto.getUsername())) {
            throw new CustomException(CONFLICT_ID);
        }
    }

    @Override
    public ResponseEntity<TokenDto> signIn(SignInReq signInReq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    signInReq.getUsername(),
                    signInReq.getPassword()
                )
            );

            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.generateAccessToken(authentication),
                refreshToken
            );

            // Redis에 저장 - 만료 시간 설정을 통해 자동 삭제 처리
            redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
            );

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + tokenDto.getAccess_token());

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new CustomException(INVALID_CREDENTIAL);
        }
    }

    @Override
    public ResponseEntity<TokenDto> regenerateToken(RegenerateTokenDto refreshTokenDto) {

        String refreshToken = refreshTokenDto.getRefresh_token();
        try {
            // Refresh Token 검증
            if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
                throw new CustomException(INVALID_REFRESH_TOKEN);
            }

            // Access Token 에서 Username을 가져온다.
            Authentication authentication = jwtTokenProvider.getAuthenticationByRefreshToken(refreshToken);

            // Redis에서 저장된 Refresh Token 값을 가져온다.
            String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
            if (!redisRefreshToken.equals(refreshToken)) {
                throw new CustomException(REFRESH_TOKEN_NOT_MATCH);
            }

            // 토큰 재발행
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.generateAccessToken(authentication),
                newRefreshToken
            );

            // RefreshToken Redis에 업데이트
            redisTemplate.opsForValue().set(
                authentication.getName(),
                newRefreshToken,
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
            );

            HttpHeaders httpHeaders = new HttpHeaders();

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);

        } catch (AuthenticationException e) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }
    }
}

