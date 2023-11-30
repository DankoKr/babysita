package s3.fontys.babysita.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.domain.BabysitterResponse;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/babysitters")
public class BabysitterController {
    private final BabysitterService babysitterService;

    @RolesAllowed({"parent", "admin"})
    @GetMapping()
    public ResponseEntity<Map<Integer, BabysitterResponse>> getAllBabysitters() {
        return ResponseEntity.ok(this.babysitterService.getAvailableBabysitters());
    }

    @RolesAllowed({"parent"})
    @PatchMapping("{babysitterId}")
    public ResponseEntity<Void> updateBabysitterPoints(@PathVariable int babysitterId) {
        try{
            babysitterService.updateBabysitterPoints(babysitterId);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

}
