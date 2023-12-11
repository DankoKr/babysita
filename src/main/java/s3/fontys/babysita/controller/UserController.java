package s3.fontys.babysita.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.exception.NoMatchException;
import s3.fontys.babysita.domain.UserRequest;
import s3.fontys.babysita.domain.UserResponse;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @RolesAllowed({"admin"})
    @GetMapping()
    public ResponseEntity<Map<Integer, UserResponse>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @RolesAllowed({"admin", "babysitter", "parent"})
    @GetMapping("{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id) {
        try{
            return ResponseEntity.ok(this.userService.getUser(id));
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"admin", "babysitter", "parent"})
    @GetMapping("/{firstUserId}/{secondUserId}")
    public ResponseEntity<List<UserResponse>> getUsersById(@PathVariable int firstUserId,
                                           @PathVariable int secondUserId) {
        try {
            return ResponseEntity.ok(this.userService.getUsersById(firstUserId, secondUserId));
        } catch(InvalidIdException ex) {
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"admin", "parent"})
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String username) {
        try {
            return ResponseEntity.ok(this.userService.searchByUsernamePattern(username));
        } catch (NoMatchException ex) {
            throw new NoMatchException("No such user");

        }
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequest userDTO)
    {
        try {
            userService.createUser(userDTO, userDTO.getPassword());
            return ResponseEntity.noContent().build();
        }
        catch(InvalidRoleException ex){
            throw new InvalidRoleException("Invalid Role!");
        }
        catch(DuplicatedUsernameException ex){
            throw new DuplicatedUsernameException("Username is not unique!");
        }
    }

    @RolesAllowed({"admin", "babysitter", "parent"})
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

    @RolesAllowed({"babysitter", "parent", "admin"})
    @PatchMapping("{userId}")
    public ResponseEntity<Void> patchUser(@PathVariable int userId, @RequestBody UserRequest userDTO) {
        try{
            userService.partialUpdateUser(userId, userDTO);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

}
