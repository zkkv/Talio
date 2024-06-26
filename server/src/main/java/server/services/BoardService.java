package server.services;

import commons.Board;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board getBoard(long boardId){
        return boardRepository.findById(boardId).get();
    }

    public List<Board> getAllBoards(){
        return boardRepository.findAll();
    }

    public Board getBoardByKey(String key){
        return boardRepository.findBoardByKey(key);
    }

    public boolean exists(long boardId){
        return boardRepository.existsById(boardId);
    }

    public void delete(long boardId){
        boardRepository.deleteById(boardId);
    }

    public boolean existsByKey(String key){
        return boardRepository.existsByKey(key);
    }
    public Board save(Board board){
        return boardRepository.save(board);
    }
}
