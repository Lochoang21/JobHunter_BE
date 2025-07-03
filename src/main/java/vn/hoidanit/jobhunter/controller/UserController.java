package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec , Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.handleGetUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User với Id: " + id + " không tồn tại!");
        }
        // return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    @ApiMessage("create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postmanUser) throws IdInvalidException {
        boolean isEmailExits = this.userService.isEmailExits(postmanUser.getEmail());
        if (isEmailExits) {
           throw new IdInvalidException("Email "+ postmanUser.getEmail()+" đã tồn tại!") ;
        }
        String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
        postmanUser.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(postmanUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
        // return ResponseEntity.ok(newUser);
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User userUpdate = this.userService.handleUpdateUser(user);

        if (userUpdate == null) {
            throw new IdInvalidException("user với Id: "+user.getId()+" không tồn tại");
        }
        // return ResponseEntity.status(HttpStatus.OK).body(userUpdate);
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(userUpdate));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.handleGetUserById(id);
        if(currentUser == null) {
            throw new IdInvalidException("User với Id: " + id + "không tồn tại!" );
        }
        this.userService.handleDeleteUser(id);
        // return ResponseEntity.status(HttpStatus.OK).body("UserIsDeleted");
        return ResponseEntity.ok(null);
    }
}
