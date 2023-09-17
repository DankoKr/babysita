package s3.fontys.babysita.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.domain.User;
import s3.fontys.babysita.dto.UserDTO;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<Map<Integer, User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id) {
        try{
            return ResponseEntity.ok(this.userService.getUser(id));
        }
        catch(Exception ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserDTO userDTO)
    {
        try {
            userService.createUser(userDTO);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidRoleException ex){
            throw new InvalidRoleException("Invalid Role!");
        }
        catch(DuplicatedUsernameException ex){
            throw new DuplicatedUsernameException("Username is not unique!");
        }
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        try{
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @PutMapping("{userId}")
    public ResponseEntity<Void> editUser(@PathVariable("userId") int id,
                                           @RequestBody @Valid UserDTO user) {
        try{
            User oldUser = userService.getUser(id);
            userService.editUser(oldUser, user.getEmail(), user.getFirstName(), user.getLastName(),
                    user.getProfileImage(), user.getPhoneNumber(), user.getAddress(), user.getAge());
            return ResponseEntity.noContent().build();
        }
        catch(Exception ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Void> patchUser(@PathVariable("userId") int id,
                                            @RequestBody @Valid UserDTO patchedUser) {

        try {
            User oldUser = userService.getUser(id);
            userService.patchUser(oldUser, patchedUser.getEmail(), patchedUser.getFirstName(), patchedUser.getLastName(),
                    patchedUser.getProfileImage(), patchedUser.getPhoneNumber(), patchedUser.getAddress(), patchedUser.getAge());
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            throw new InvalidIdException("Invalid ID.");
        }
    }
}
