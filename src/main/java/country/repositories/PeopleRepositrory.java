package country.repositories;

import country.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepositrory extends JpaRepository<Person, Integer> {
    Optional<Object> getPersonByName(String name);
}
