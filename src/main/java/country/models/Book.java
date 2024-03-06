package country.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @NotEmpty(message = "Name should not be empty")
    @Size(min=2, max=150, message = "Name of book should be between 2 and 150 characters")
    @Column(name = "name_book")
    private String title;
    @Size(min=2, max=100, message = "Name should be between 2 and 100 characters")
    @Column(name = "author_book")
    private String author_book;
    @Min(value = 1500, message = "The year of publication must be greater than 1500")
    @Column(name = "year_book")
    private int year_book;
    @Id
    @Column(name = "id_book")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_book;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    @Column(name = "time_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeAt;

    @Transient
    private boolean expired;

    public Book() {

    }

    public Book(String name_book, String author_book, int year_book) {
        this.title = name_book;
        this.author_book = author_book;
        this.year_book = year_book;
    }

}
