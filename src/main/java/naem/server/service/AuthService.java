package naem.server.service;

import naem.server.domain.User;

public interface AuthService {

    void signUpUser(User user);

    User loginUser(String id, String password) throws Exception;
}
