package ru.otus.hw.repository;

import org.springframework.stereotype.Repository;


@Repository
public class PersonRepositoryCustom {
//
//    private final R2dbcEntityTemplate template;
//    private final ObjectMapper objectMapper;
//
//    private static final String SQL_ALL = """
//            select json_agg(n.note_text) as notes, n.person_id,
//                   p.last_name, p.age
//                 from notes n
//              inner join person p
//                on n.person_id = p.id
//                group by n.person_id, p.last_name, p.age
//            """;
//
//    public PersonRepositoryCustom(R2dbcEntityTemplate template, ObjectMapper objectMapper) {
//        this.template = template;
//        this.objectMapper = objectMapper;
//    }
//
//    public Flux<PersonDto> findAll() {
//        return template.getDatabaseClient().inConnectionMany(connection ->
//                Flux.from(connection.createStatement(SQL_ALL)
//                                .execute())
//                        .flatMap(result -> result.map(this::mapper)));
//    }
//
//    private PersonDto mapper(Readable selectedRecord) {
//        var notesAsText = selectedRecord.get("notes", String.class);
//        try {
//            List<String> notes = objectMapper.readValue(notesAsText, new TypeReference<>() {
//            });
//            return new PersonDto(selectedRecord.get("person_id", String.class),
//                    selectedRecord.get("last_name", String.class),
//                    selectedRecord.get("age", Integer.class),
//                    notes);
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException("notes:" + notesAsText + " parsing error:" + e);
//        }
//    }
}
