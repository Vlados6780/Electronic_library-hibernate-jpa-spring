package country.services;

import country.models.Book;
import country.models.Person;
import country.repositories.PeopleRepositrory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepositrory peopleRepositrory;

    @Autowired
    public PeopleService(PeopleRepositrory peopleRepositrory) {
        this.peopleRepositrory = peopleRepositrory;
    }

    public List<Person> findAll() {
        return peopleRepositrory.findAll();
    }
    public Person findOne(int id) {
        return peopleRepositrory.findById(id).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepositrory.save(person);
    }
    @Transactional
    public void delete(int id) {
        peopleRepositrory.deleteById(id);
    }
    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setPerson_id(id);
        peopleRepositrory.save(updatedPerson);
    }
    public Optional<Object> getPersonByName(String name) {
        return peopleRepositrory.getPersonByName(name);
    }
    @Transactional
    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepositrory.findById(id);

        if(person.isPresent()) {

            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                long differenceInmillies = Math.abs(book.getTimeAt().getTime() - new Date().getTime());
                long days = differenceInmillies / (24 * 60 * 60 * 1000);
                if (days >= 10) {
                    book.setExpired(true);
                }
            });

            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

}
