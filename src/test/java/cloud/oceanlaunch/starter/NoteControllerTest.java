package cloud.oceanlaunch.starter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import tools.jackson.databind.json.JsonMapper;

/** Runs against a real PostgreSQL in Docker (Testcontainers), as the app does on OceanLaunch. */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NoteControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    MockMvc mvc;

    @Test
    void addsListsAndDeletesNotes() throws Exception {
        String created = mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"Buy milk\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Buy milk"))
                .andReturn().getResponse().getContentAsString();
        long id = JsonMapper.builder().build().readTree(created).get("id").asLong();

        mvc.perform(get("/api/notes")).andExpect(jsonPath("$[0].text").value("Buy milk"));
        mvc.perform(delete("/api/notes/" + id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/notes/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void refusesAnEmptyNote() throws Exception {
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{\"text\":\"  \"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void isHealthyWithItsDatabase() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }
}
