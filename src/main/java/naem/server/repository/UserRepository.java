package naem.server.repository;

import org.springframework.data.repository.CrudRepository;

import naem.server.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLoginId(String loginId);

}
