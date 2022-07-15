package naem.server.service;

import naem.server.domain.Member;

public interface AuthService {

    void signUpUser(Member member);

    Member loginUser(String id, String password) throws Exception;
}
