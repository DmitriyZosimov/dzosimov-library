package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.model.sample.SearchBookSample;
import com.epam.brest.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@SessionAttributes({"bookSample", "searchBookSample"})
@Controller
public class CatalogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class);

    private final IBookService bookService;
    private SessionLocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Autowired
    public CatalogController(IBookService bookService, SessionLocaleResolver localeResolver,
                             MessageSource messageSource) {
        this.bookService = bookService;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    @ModelAttribute("bookSample")
    public BookSample getBookSample(){
        LOGGER.info("Create a ModelAttribute \"bookSample\"");
        return new BookSample();
    }

    @ModelAttribute("searchBookSample")
    public SearchBookSample getSearhBookSample(){
        LOGGER.info("Create a ModelAttribute \"searchBookSample\"");
        return new SearchBookSample();
    }

    /**
     * Goto catalog list page.
     * @return view catalog
     */

    @GetMapping(value = {"/", "/catalog"})
    public String getMainPage(Model model){
        LOGGER.info("GET /catalog");
        List<BookSample> books = bookService.findAll();
        model.addAttribute("books", books);
        return "catalog";
    }

    /**
     * Goto catalog list page with param.
     * @param result boolean result
     * @return load method getMainPage
     */
    @GetMapping(value = {"/result"})
    public String getMainPage(@RequestParam("resultMessage") String result, Model model){
        LOGGER.info("GET /catalog?result={}", result);
        model.addAttribute("resultMessage", result);
        return getMainPage(model);
    }

    /**
     * Goto catalog list page with param.
     * @param result String resultMessage
     * @param card library card
     * @return view catalog
     */
    @GetMapping(value = {"/result/card"})
    public String getMainPage(@RequestParam("resultMessage") String result,
                              @RequestParam("card") Integer card, Model model){
        LOGGER.info("GET /catalog?result={}&card={}", result, card);
        model.addAttribute("resultMessage", result);
        model.addAttribute("card", card);
        return getMainPage(model);
    }

    /**
     * Add a book to the reader
     * and goto catalog list page.
     *
     * @param bookId book id
     * @return view catalog
     */
    @PostMapping(value = "/catalog/select/{book}")
    public String selectBook(@PathVariable("book") int bookId, Model model, HttpSession session,
                             HttpServletRequest request){
        LOGGER.info("POST select a book /catalog/select/bookId={}", bookId);
        Integer readerId = (Integer) session.getAttribute("libraryCard");
        String messageCode;
        if(bookService.addReaderForBook(readerId, bookId)){
            messageCode = "message.select.good";
        } else {
            messageCode = "message.select.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return getMainPage(model);
    }

    /**
     * Search books
     *
     * @param bookSample book request for searching
     * @return view catalog page
     */
    @GetMapping(value = "/search")
    public String searchBooks(@Valid @ModelAttribute("searchBookSample") SearchBookSample bookSample,
                             Model model, BindingResult bindingResult){
        LOGGER.info("POST search a book");
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "error";
        }

        List<BookSample> resultBook = bookService.searchBooks(bookSample);
        model.addAttribute("books", resultBook);
        return "catalog";
    }

}
