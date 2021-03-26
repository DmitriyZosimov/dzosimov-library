package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.dto.BookDto;
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
import java.util.List;

@SessionAttributes({"bookDto"})
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

    //TODO: rename to bookSample
    @ModelAttribute("bookDto")
    public BookDto getBookDto(){
        LOGGER.info("Create a ModelAttribute \"bookDto\"");
        return bookService.getBookDto();
    }

    /**
     * Goto catalog list page.
     * @return view catalog
     */

    @GetMapping(value = {"/", "/catalog"})
    public String getMainPage(Model model){
        LOGGER.info("GET /catalog");
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "catalog";
    }

    /**
     * Goto catalog list page with param.
     * @param result boolean result
     * @return view catalog
     */
    @GetMapping(value = {"/result"})
    public String getMainPage(@RequestParam("result") Boolean result, Model model){
        LOGGER.info("GET /catalog?result={}", result);
        model.addAttribute("result", result);
        return getMainPage(model);
    }

    /**
     * Goto catalog list page with param.
     * @param result boolean result
     * @param card library card
     * @return view catalog
     */
    @GetMapping(value = {"/result/card"})
    public String getMainPage(@RequestParam("result") Boolean result,
                              @RequestParam("card") Integer card, Model model){
        LOGGER.info("GET /catalog?result={}&card={}", result, card);
        model.addAttribute("result", result);
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
        LOGGER.info("GET select a book /bookId={}", bookId);
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
     * @param bookDto book request for search
     * @return view catalog page
     */
    @GetMapping(value = "/search")
    public String searchBooks(@ModelAttribute("bookDto") BookDto bookDto,
                             Model model, BindingResult bindingResult){
        LOGGER.info("POST search a book");
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "error";
        }

        List<Book> resultBook = bookService.searchBooks(bookDto);
        model.addAttribute("books", resultBook);
        return "catalog";
    }

}
