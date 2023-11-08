package s3.fontys.babysita.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.dto.PosterDTO;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/posters")
public class PosterController {
    private final PosterService posterService;

    @RolesAllowed({"admin"})
    @GetMapping()
    public ResponseEntity<Map<Integer, PosterDTO>> getAllPosters() {
        return ResponseEntity.ok(this.posterService.getAllPosters());
    }

    @RolesAllowed({"babysitter"})
    @GetMapping("noBabysitter")
    public ResponseEntity<Map<Integer, PosterDTO>> getPostersWithoutBabysitter() {
        return ResponseEntity.ok(this.posterService.getPostersWithoutBabysitterId());
    }

    @RolesAllowed({"parent", "admin", "babysitter"})
    @GetMapping("user/{userId}")
    public ResponseEntity<List<PosterDTO>> getUserPosters(@PathVariable("userId") int userId) {
        return ResponseEntity.ok(this.posterService.getUserPosters(userId));
    }

    @RolesAllowed({"parent"})
    @PostMapping()
    public ResponseEntity<Void> createPoster(@RequestBody @Valid PosterDTO poster)
    {
        posterService.createPoster(poster);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"parent", "babysitter", "admin"})
    @GetMapping("{id}")
    public ResponseEntity<Object> getPosterById(@PathVariable("id") int id) {
        try{
            return ResponseEntity.ok(this.posterService.getPoster(id));
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"parent", "admin"})
    @DeleteMapping("{posterId}")
    public ResponseEntity<Void> deletePoster(@PathVariable int posterId) {
        try{
            posterService.deletePoster(posterId);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"parent", "admin"})
    @PutMapping("{posterId}")
    public ResponseEntity<Void> editPoster(@PathVariable("posterId") int id,
                                              @RequestBody @Valid PosterDTO updatedPosterDTO) {
        try{
            posterService.editPoster(id, updatedPosterDTO);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"parent"})
    @PatchMapping("{posterId}")
    public ResponseEntity<Void> patchPoster(@PathVariable("posterId") int id,
                                            @RequestBody @Valid PosterDTO patchedPosterDTO) {

        try {
            posterService.patchPoster(id, patchedPosterDTO);
            return ResponseEntity.noContent().build();
        } catch (InvalidIdException ex) {
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"parent"})
    @PatchMapping("{posterId}/assign/{babysitterId}")
    public ResponseEntity<String> assignPosterToBabysitter(@PathVariable int posterId, @PathVariable int babysitterId) {
        try {
            posterService.assignPosterToBabysitter(posterId, babysitterId);
            return ResponseEntity.ok("Poster successfully assigned to babysitter.");
        } catch (InvalidIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while processing your request.");
        }
    }

}
