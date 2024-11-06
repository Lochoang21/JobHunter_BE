package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user){
        return this.userRepository.save(user);
    }

    public List<User> handleGetAllUser(){
        return this.userRepository.findAll();
    }

    public User handleGetUserById(long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }

    public void handleDeleteUser(long id){
         this.userRepository.deleteById(id);
    }

    public User handleUpdateUser(User user){
        User currentUser = this.handleGetUserById(user.getId());
        if(currentUser != null){
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            return this.userRepository.save(user);

        }
        return currentUser;
    }
}
