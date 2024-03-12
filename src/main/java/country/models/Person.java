package country.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @NotEmpty(message = "Name should not be empty")
    @Size(min=2, max=30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int person_id;
    @Min(value=1900, message = "Year of birth should be greater than 1990")
    @Column(name = "year_of_birth")
    private int yearOfBirth;
    @OneToMany(mappedBy = "owner")
    private List<Book> books;
    public Person() {

    }

    public Person(int yearOfBirth, String name) {
        this.yearOfBirth = yearOfBirth;
        this.name = name;
    }

}
