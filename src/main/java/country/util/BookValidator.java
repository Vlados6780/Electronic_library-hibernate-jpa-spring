package country.util;

import country.services.BooksService;
import country.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if (!Character.isUpperCase(book.getTitle().codePointAt(0)))
            errors.rejectValue("title", "",
                    "The title of the book must begin with a capital letter.");

        if (!Character.isUpperCase(book.getAuthor_book().codePointAt(0)))
            errors.rejectValue("author_book", "",
                    "The author's name must begin with a capital letter.");
    }
}