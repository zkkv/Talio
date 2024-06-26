package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findBoardByKey(String key);

    boolean existsByKey(String key);

}
