package naem.server.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import naem.server.domain.Salt;
import naem.server.domain.User;
import naem.server.repository.UserRepository;
import naem.server.service.util.SaltUtil;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    //    @Autowired
    //    private RedisUtil redisUtil;

    @Autowired
    private SaltUtil saltUtil;

    @Override
    @Transactional
    public void signUpUser(User user) {

        String password = user.getPassword();
        String salt = saltUtil.genSalt();
        log.info(salt);

        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodePassword(salt, password));
        userRepository.save(user);
    }

    @Override
    public User loginUser(String id, String password) throws Exception {

        User user = userRepository.findByUsername(id);
        if (user == null)
            throw new Exception("멤버가 조회되지 않음");

        String salt = user.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);
        if (!user.getPassword().equals(password))
            throw new Exception("비밀번호가 틀립니다.");

        return user;
    }

}
