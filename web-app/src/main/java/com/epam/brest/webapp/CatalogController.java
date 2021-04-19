package com.epam.brest.webapp;

import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@SessionAttributes({"searchBookSample"})
@Controller
public class CatalogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class);

    private final BookService bookService;

    @Autowired
    public CatalogController(BookService bookService) {
        this.bookService = bookService;
    }
    /**
     * Create ModelAttribute of a SearchBookSample
     * @return object of a {@link SearchBookSample}
     */
    @ModelAttribute("searchBookSample")
    public SearchBookSample getSearhBookSample(){
        LOGGER.info("Create a ModelAttribute \"searchBookSample\"");
        return new SearchBookSample();
    }

    /**
     * Goto catalog list page.
     * @param model used for rendering views
     * @return view catalog
     */
    @GetMapping(value = {"/", "/catalog"})
    public String getMainPage(Model model){
        LOGGER.info("GET /catalog");
        LOGGER.debug("getMainPage({})", model);
        model.addAttribute("books", bookService.findAll());
        return "catalog";
    }

    /**
     * Goto catalog list page.
     * @param message the message for further adding in view
     * @param model used for rendering views
     * @return view catalog
     */
    @GetMapping(value = {"/result"})
    public String getMainPage(@RequestParam("resultMessage") String message, Model model){
        LOGGER.info("GET /catalog with param result message");
        LOGGER.debug("getMainPage({})", model);
        model.addAttribute("resultMessage", message);
        return getMainPage(model);
    }

    /**
     * Add the book to the reader
     * and goto catalog list page.
     *
     * @param bookId identification of the book
     * @param model used for rendering views
     * @param session used for getting readerId ("library card") of a reader
     * @return redirect to catalog
     */
    @GetMapping(value = "/catalog/select/{book}")
    public String selectBook(@PathVariable("book") @Min(1) Integer bookId, Model model,
                             HttpSession session){
        LOGGER.info("POST select a book /catalog/select/bookId={}", bookId);
        Integer readerId = (Integer) session.getAttribute("libraryCard");
        bookService.addReaderForBook(readerId, bookId);
        return "redirect:/catalog";
    }

    /**
     * Search books
     *
     * @param bookSample sample of the book request for searching
     * @param model used for rendering views
     * @return view catalog page
     */
    @PostMapping(value = "/search")
    public String searchBooks(
            @Valid @ModelAttribute("searchBookSample") SearchBookSample bookSample,
                               Model model){
        LOGGER.info("POST /search");
        LOGGER.debug("searchBooks({}, {})", bookSample, model);
        model.addAttribute("books", bookService.searchBooks(bookSample));
        return "catalog";
    }

}
