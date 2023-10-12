package s3.fontys.babysita.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.dto.ParentDTO;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/parents")
public class ParentController {
    private final ParentService parentService;

    @GetMapping()
    public ResponseEntity<Map<Integer, ParentDTO>> getAllParents() {
        return ResponseEntity.ok(this.parentService.getAllParents());
    }
}
