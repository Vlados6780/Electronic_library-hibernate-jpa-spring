package country.services;

import country.models.Book;
import country.models.Person;
import country.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(boolean sortByYear) {

        if (sortByYear) {
            return booksRepository.findAll(Sort.by("year_book"));
        }else {
            return booksRepository.findAll();
        }

    }

    public List<Book> findWithPagination(int page, int booksPerPage, boolean sortByYear) {

        if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year_book"))).getContent();
        }else {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
    }

    public List<Book> search(String name) {
        return booksRepository.findBookByTitleContainingIgnoreCase (name);
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }
    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }
    @Transactional
    public void update(int id, Book updatedBook) {
        Book book = booksRepository.findById(id).orElse(null);
        updatedBook.setId_book(id);
        updatedBook.setOwner(book.getOwner());
        booksRepository.save(updatedBook);
    }


    public Person getBookOwner(int id) {
        Optional<Book> bookOptional = booksRepository.findById(id);
        return bookOptional.map(Book::getOwner).orElse(null);
    }
    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).ifPresent(
                book-> {
                    book.setOwner(selectedPerson);
                    book.setTimeAt(new Date());
                });
    }
    @Transactional
    public void setFree(int id) {
        booksRepository.findById(id).ifPresent(
            book-> {
                book.setOwner(null);
                book.setTimeAt(null);
            });
    }
}
