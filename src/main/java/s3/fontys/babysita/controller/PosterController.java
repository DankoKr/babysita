package s3.fontys.babysita.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.dto.PosterDTO;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/posters")
@CrossOrigin(origins="*", allowedHeaders = "*")
public class PosterController {
    private final PosterService posterService;

    @GetMapping()
    public ResponseEntity<Map<Integer, Poster>> getAllPosters() {
        return ResponseEntity.ok(this.posterService.getAllPosters());
    }

    @PostMapping()
    public ResponseEntity<Void> createPoster(@RequestBody @Valid Poster poster)
    {
        posterService.createPoster(poster);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPosterById(@PathVariable("id") int id) {
        try{
            return ResponseEntity.ok(this.posterService.getPoster(id));
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

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

    @PutMapping("{posterId}")
    public ResponseEntity<Void> editPoster(@PathVariable("posterId") int id,
                                              @RequestBody @Valid Poster poster) {
        try{
            Poster oldPoster = posterService.getPoster(id);
            posterService.editPoster(oldPoster, poster.getTitle(), poster.getDescription(),
                    poster.getImageUrl(), poster.getEventDate());
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @PatchMapping("{posterId}")
    public ResponseEntity<Void> patchPoster(@PathVariable("posterId") int id,
                                            @RequestBody @Valid PosterDTO patchedPoster) {

        try {
            Poster oldPoster = posterService.getPoster(id);
            posterService.patchPoster(oldPoster, patchedPoster.getTitle(), patchedPoster.getDescription(),
                    patchedPoster.getImageUrl(), patchedPoster.getEventDate());
            return ResponseEntity.noContent().build();
        } catch (InvalidIdException ex) {
            throw new InvalidIdException("Invalid ID.");
        }
    }

}
