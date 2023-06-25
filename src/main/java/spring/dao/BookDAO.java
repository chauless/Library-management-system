package spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import spring.models.Book;
import spring.models.Person;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        BeanPropertyRowMapper<Book> rowMapper = new BeanPropertyRowMapper<>(Book.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true); // Обработка NULL значений

        return jdbcTemplate.query("SELECT * FROM Book", rowMapper);
    }

    public Object show(int id) {
        BeanPropertyRowMapper<Book> rowMapper = new BeanPropertyRowMapper<>(Book.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true); // Обработка NULL значений

        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, rowMapper)
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES(?, ?, ?)", book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?", book.getTitle(), book.getAuthor(), book.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }

    public Person getBookOwner(int id) {
        BeanPropertyRowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
        rowMapper.setPrimitivesDefaultedForNullValue(true); // Обработка NULL значений

        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id = Person.id " +
                        "WHERE Book.id = ?", new Object[]{id}, rowMapper)
                .stream().findAny().orElse(null);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE Book SET person_id=NULL WHERE id=?", id);
    }

    public void assign(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", person.getId(), id);
    }
}
