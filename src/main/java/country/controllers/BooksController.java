package country.controllers;

import country.models.Book;
import country.models.Person;
import country.services.BooksService;
import country.services.PeopleService;
import country.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final BookValidator bookValidator;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService,
                           BookValidator bookValidator,
                           PeopleService peopleService) {
        this.booksService = booksService;
        this.bookValidator = bookValidator;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {


        if (page == null || booksPerPage == null) {
            model.addAttribute("books", booksService.findAll(sortByYear));
        }else {
            model.addAttribute("books", booksService.findWithPagination(page, booksPerPage, sortByYear));
        }

        return "books/index.html";

    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("book") Book book) {
        return "books/new.html";
    }
    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/new.html";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);
        return "redirect:/books";
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showOne(@PathVariable("id") int id, Model model,
                          @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Person bookOwner = booksService.getBookOwner(id);
        if(bookOwner != null){
            model.addAttribute("owner", bookOwner);
        }else {
            model.addAttribute("people", peopleService.findAll());
        }

        return "books/one";
    }

    @PatchMapping("/{id}/set_free")
    public String setFree(@PathVariable("id") int id) {
        booksService.setFree(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(@ModelAttribute("query") String query,
                             Model model) {
        model.addAttribute("query", query);
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(@ModelAttribute("query") String query,
                             Model model) {
        model.addAttribute("books", booksService.search(query));
        return "books/search";
    }


}
