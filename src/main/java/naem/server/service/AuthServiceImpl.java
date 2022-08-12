package naem.server.service;

import static naem.server.exception.ErrorCode.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
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
import naem.server.domain.member.dto.RegenerateTokenDto;
import naem.server.domain.member.dto.SignInReq;
import naem.server.domain.member.dto.SignUpReq;
import naem.server.domain.member.dto.SignUpRes;
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
    public SignUpRes signUp(SignUpReq signUpReq) {
        System.out.println("signUpReq = " + signUpReq.toString());

        if (userRepository.existsByUsername(signUpReq.getUsername())) {
            return new SignUpRes(false, "Your Mail already Exist.");
        }
        Member newUser = signUpReq.toUserEntity();
        newUser.hashPassword(bCryptPasswordEncoder);

        Member user = userRepository.save(newUser);
        if (!Objects.isNull(user)) {
            return new SignUpRes(true, null);
        }
        return new SignUpRes(false, "Fail to Sign Up");
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

            String refresh_token = jwtTokenProvider.generateRefreshToken(authentication);

            TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.generateAccessToken(authentication),
                refresh_token
            );

            // Redis에 저장 - 만료 시간 설정을 통해 자동 삭제 처리
            redisTemplate.opsForValue().set(
                authentication.getName(),
                refresh_token,
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
            );

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + tokenDto.getAccess_token());

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.info("log --- {}", e.getMessage());
            throw new CustomException(INVALID_CREDENTIAL);
        }
    }

    @Override
    public ResponseEntity<TokenDto> regenerateToken(RegenerateTokenDto refreshTokenDto) {
        String refresh_token = refreshTokenDto.getRefresh_token();
        try {
            // Refresh Token 검증
            if (!jwtTokenProvider.validateRefreshToken(refresh_token)) {
                throw new CustomException(INVALID_REFRESH_TOKEN);
            }

            // Access Token 에서 Username을 가져온다.
            Authentication authentication = jwtTokenProvider.getAuthenticationByRefreshToken(refresh_token);

            // Redis에서 저장된 Refresh Token 값을 가져온다.
            String refreshToken = redisTemplate.opsForValue().get(authentication.getName());
            if (!refreshToken.equals(refresh_token)) {
                throw new CustomException(REFRESH_TOKEN_NOT_MATCH);
            }

            // 토큰 재발행
            String new_refresh_token = jwtTokenProvider.generateRefreshToken(authentication);
            TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.generateAccessToken(authentication),
                new_refresh_token
            );

            // RefreshToken Redis에 업데이트
            redisTemplate.opsForValue().set(
                authentication.getName(),
                new_refresh_token,
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
            );

            HttpHeaders httpHeaders = new HttpHeaders();

            return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.info("log --- {}", e.getMessage());
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }
    }
}

