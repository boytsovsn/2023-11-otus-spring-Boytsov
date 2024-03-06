package ru.otus.hw.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.scheduler.Scheduler;
import ru.otus.hw.repository.NotesRepository;
import ru.otus.hw.repository.PersonRepository;
import ru.otus.hw.repository.PersonRepositoryCustom;
import ru.otus.hw.services.BookService;

@EnableConfigurationProperties
@SpringBootTest(classes = {PersonController.class, PersonRepository.class},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan({"ru.otus.hw.config.test.funcendpoint","ru.otus.hw.config.test.reactiverest","ru.otus.hw.changelogs.test", "ru.otus.hw.repository"})
class PersonControllerTest {

    @Autowired
    private RouterFunction<ServerResponse> route;

    @Autowired
    private PersonRepository personRepository;

    @MockBean
    private NotesRepository notesRepository;

    @MockBean
    private PersonRepositoryCustom personRepositoryCustom;

    @MockBean
    private Scheduler workerPool;

    @Test
    void testRoute() {
        WebTestClient client = WebTestClient
                .bindToRouterFunction(route)
                .build();

        client.get()
                .uri("/func/person")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
