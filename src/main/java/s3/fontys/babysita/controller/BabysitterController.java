package s3.fontys.babysita.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.dto.BabysitterDTO;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/babysitters")
public class BabysitterController {
    private final BabysitterService babysitterService;

    @GetMapping()
    public ResponseEntity<Map<Integer, BabysitterDTO>> getAllBabysitters() {
        return ResponseEntity.ok(this.babysitterService.getAllBabysitters());
    }

}
