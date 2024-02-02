package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA репозиторий для Book")
class BookRepositoryTest {

//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private RemarkRepository RemarkRepository;
//
//    private List<Author> dbAuthors;
//
//    private List<Genre> dbGenres;
//
//    private List<List<Remark>> dbRemarks;
//
//    private List<Book> dbBooks;
//
//    @BeforeEach
//    void setUp() {
//        dbAuthors = getDbAuthors();
//        dbGenres = getDbGenres();
//        dbRemarks = getDbRemarks();
//        dbBooks = getDbBooks(dbAuthors, dbGenres, dbRemarks);
//    }
//
//    @DisplayName("Список всех книг")
//    @Test
//    void findAll() {
//        List<Book> books = bookRepository.findAll();
//        List<Book> checkBooks = dbBooks;
//        assertThat(books.size()).isEqualTo(checkBooks.size());
//        for (int i = 0; i < books.size(); i++) {
//            assertThat(books.get(i).getId()).isEqualTo(checkBooks.get(i).getId());
//            assertThat(books.get(i).getAuthor().getId()).isEqualTo(checkBooks.get(i).getAuthor().getId());
//            assertThat(books.get(i).getGenre().getId()).isEqualTo(checkBooks.get(i).getGenre().getId());
//            assertThat(books.get(i).getGenre()).isEqualTo(checkBooks.get(i).getGenre());
//            assertThat(books.get(i).getTitle()).isEqualTo(checkBooks.get(i).getTitle());
//            assertThat(books.get(i).getRemarks().size()).isEqualTo(checkBooks.get(i).getRemarks().size());
//            for (int j = 0; j < books.get(i).getRemarks().size(); j++) {
//                assertThat(books.get(i).getRemarks().get(j)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(checkBooks.get(i).getRemarks().get(j));
//            }
//        }
//        books.forEach(System.out::println);
//    }
//
//    @DisplayName("Загрузка книги по id")
//    @ParameterizedTest
//    @MethodSource("getDbBooks")
//    void findById(Book checkBook) {
//        var book = bookRepository.findById(checkBook.getId()).get();
//        assertThat(book.getId()).isEqualTo(checkBook.getId());
//        assertThat(book.getAuthor().getId()).isEqualTo(checkBook.getAuthor().getId());
//        assertThat(book.getAuthor()).isEqualTo(checkBook.getAuthor());
//        assertThat(book.getGenre().getId()).isEqualTo(checkBook.getGenre().getId());
//        assertThat(book.getGenre()).isEqualTo(checkBook.getGenre());
//        assertThat(book.getTitle()).isEqualTo(checkBook.getTitle());
//        assertThat(book.getRemarks().size()).isEqualTo(checkBook.getRemarks().size());
//        for (int j = 0; j < book.getRemarks().size(); j++) {
//            assertThat(book.getRemarks().get(j).getId()).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(checkBook.getRemarks().get(j).getId());
//        }
//    }
//
//    @DisplayName("Cохранение новой книги")
//    @Test
//    void insertBook() {
//        var newBook = new Book(0, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(1), new ArrayList<Remark>());
//        var returnedBook = bookRepository.save(newBook);
//        newBook.setId(returnedBook.getId());
//        assertThat(returnedBook).isNotNull()
//                .matches(book -> book.getId() > 0)
//                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newBook);
//        Remark newRemark = new Remark(0, "Это особое мнение", returnedBook);
//        var returnedRemark = RemarkRepository.save(newRemark);
//        returnedBook.setRemarks(Arrays.asList(returnedRemark));
//        Optional<Book> checkBook = bookRepository.findById(returnedBook.getId());
//        assertThat(checkBook)
//                .isPresent()
//                .get()
//                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
//    }
//
//    @DisplayName("Обновление книги")
//    @Test
//    void updatedBook() {
//        long updatedBookId = 1L;
//        int fromBookIndex = 2;
//        List<Remark> expectedRemarks = dbRemarks.get((int) (updatedBookId-1));
//        int i = 0;
//        for (Remark expRemark : expectedRemarks) {
//            if (i >= dbRemarks.get(fromBookIndex).size())
//                break;
//            expRemark.setRemarkText(dbRemarks.get(fromBookIndex).get(i).getRemarkText());
//            i++;
//        }
//        var expectedBook = new Book(updatedBookId, "BookTitle_10500", dbAuthors.get(fromBookIndex), dbGenres.get(fromBookIndex), expectedRemarks);
//
//        assertThat(bookRepository.findById(expectedBook.getId()))
//                .isPresent()
//                .get()
//                .isNotEqualTo(expectedBook);
//
//        var returnedBook = bookRepository.save(expectedBook);
//        returnedBook.setRemarks(expectedRemarks);
//        assertThat(returnedBook).isNotNull()
//                .matches(book -> book.getId() > 0)
//                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
//
//        for (Remark remark : expectedRemarks) {
//            var returnedRemark = RemarkRepository.save(remark);
//        }
//        assertThat(bookRepository.findById(returnedBook.getId()))
//                .isPresent()
//                .get()
//                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
//    }
//
//    @DisplayName("Удаление книги по id")
//    @Test
//    void deleteBook() {
//        long deletedBookId = 3L;
//        assertThat(bookRepository.findById(deletedBookId)).isPresent();
//        List<Remark> expectedRemarks = bookRepository.findById(deletedBookId).get().getRemarks();
//        bookRepository.deleteById(deletedBookId);
//        Assert.isTrue(bookRepository.findById(deletedBookId).isEmpty(), "Book with id = %d is not deleted".formatted(deletedBookId));
//        for (Remark remark : expectedRemarks) {
//            Assert.isTrue(RemarkRepository.findById(remark.getId()).isEmpty(), " Remark = %s for book id %d is not deleted".formatted(remark.getRemarkText(), deletedBookId));
//        }
//    }
//
//    private static List<Author> getDbAuthors() {
//        return IntStream.range(1, 4).boxed()
//                .map(id -> new Author(id, "Author_" + id))
//                .toList();
//    }
//
//    private static List<Genre> getDbGenres() {
//        return IntStream.range(1, 4).boxed()
//                .map(id -> new Genre(id, "Genre_" + id))
//                .toList();
//    }
//
//    private static List<List<Remark>> getDbRemarks() {
//        return IntStream.range(1, 4).boxed()
//                .map(id -> IntStream.range(1, id+1).boxed()
//                        .map(id1 ->{ Book book = new Book(id, null, null, null, null);
//                            return new Remark(id*(id-1)/2+id1,"Remark_"+id+id1, book);}).toList())
//                .toList();
//    }
//
//    private static List<Book> getDbBooks(List<Author> _dbAuthors, List<Genre> _dbGenres, List<List<Remark>> _dbRemarks) {
//        return IntStream.range(1, 4).boxed()
//                .map(id -> new Book(id, "BookTitle_" + id, _dbAuthors.get(id - 1), _dbGenres.get(id - 1), _dbRemarks.get(id-1)))
//                .toList();
//    }
//
//    private static List<Book> getDbBooks() {
//        var authors = getDbAuthors();
//        var genres = getDbGenres();
//        var remarks = getDbRemarks();
//        return getDbBooks(authors, genres, remarks);
//    }
}