package com.epam.brest.webapp;

import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@SessionAttributes({"bookSample", "searchBookSample"})
@Controller
public class CatalogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class);

    private final IBookService bookService;

    @Autowired
    public CatalogController(IBookService bookService) {
        this.bookService = bookService;
    }
    /**
     * Create ModelAttribute of a BookSample
     * @return object of a BookSample.class
     */
    @ModelAttribute("bookSample")
    public BookSample getBookSample(){
        LOGGER.info("Create a ModelAttribute \"bookSample\"");
        return new BookSample();
    }

    /**
     * Create ModelAttribute of a SearchBookSample
     * @return object of a SearchBookSample.class
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
     * Add a book to the reader
     * and goto catalog list page.
     *
     * @param bookId book id
     * @param model used for rendering views
     * @param session used for getting readerId ("library card") of a reader
     * @return view catalog
     */
    @PostMapping(value = "/catalog/select/{book}")
    public String selectBook(@PathVariable("book") int bookId, Model model,
                             HttpSession session){
        LOGGER.info("POST select a book /catalog/select/bookId={}", bookId);
        Integer readerId = (Integer) session.getAttribute("libraryCard");
        bookService.addReaderForBook(readerId, bookId);
        return "redirect:/catalog";
    }

    /**
     * Search books
     *
     * @param bookSample book request for searching
     * @param model used for rendering views
     * @param bindingResult represents binding results of the bookSample
     * @return view catalog page
     */
    @GetMapping(value = "/search")
    public String searchBooks(
            @Valid @ModelAttribute("searchBookSample") SearchBookSample bookSample,
                              BindingResult bindingResult, Model model){
        LOGGER.info("POST /search");
        LOGGER.debug("searchBooks({}, {}, {})", bookSample, bindingResult, model);
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "catalog";
        }
        model.addAttribute("books", bookService.searchBooks(bookSample));
        return "catalog";
    }

}
