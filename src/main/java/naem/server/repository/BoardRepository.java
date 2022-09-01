package naem.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import naem.server.domain.Board;
import naem.server.domain.BoardType;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByBoardType(BoardType boardType);

    // boolean existsByBoardType(String boardType);
}
