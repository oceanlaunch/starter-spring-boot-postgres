package cloud.oceanlaunch.starter;

import java.time.Instant;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Notes kept in PostgreSQL. The page at / (src/main/resources/static/index.html) uses this API. */
@RestController
@RequestMapping("/api/notes")
class NoteController {

    record NewNote(@NotBlank @Size(max = 500) String text) {
    }

    private final NoteRepository notes;

    NoteController(NoteRepository notes) {
        this.notes = notes;
    }

    @GetMapping
    List<Note> list() {
        return notes.findTop100ByOrderByCreatedAtDesc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Note add(@Valid @RequestBody NewNote body) {
        return notes.save(new Note(body.text().strip(), Instant.now()));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable long id) {
        if (!notes.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        notes.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
