package cloud.oceanlaunch.starter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findTop100ByOrderByCreatedAtDesc();
}
