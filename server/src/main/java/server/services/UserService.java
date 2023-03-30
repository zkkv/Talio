package server.services;

import commons.Board;
import commons.User;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Board> getUserBoards(String userName){
        return userRepository.findUserByUserName(userName).getJoinedBoards();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User createUser(String userName){
        User user = new User(userName,new ArrayList<>());
        return userRepository.save(user);
    }

    public User getUser(String userName){
        return userRepository.findUserByUserName(userName);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}