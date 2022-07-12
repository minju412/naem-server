package naem.server.repository;


import naem.server.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByUsername(String username);

}
