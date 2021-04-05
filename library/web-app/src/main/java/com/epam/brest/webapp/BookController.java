package com.epam.brest.webapp;

import com.epam.brest.model.sample.BookSample;
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
import javax.validation.Valid;

@SessionAttributes({"bookSample"})
@Controller
@RequestMapping("/book/**")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final IBookService bookService;
    private SessionLocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Autowired
    public BookController(IBookService bookService, SessionLocaleResolver localeResolver, MessageSource messageSource) {
        this.bookService = bookService;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    /**
     * Goto book page
     * @param model used for rendering views
     * @return view book
     */
    @GetMapping(value = {"/new"})
    public String addBook(Model model){
        LOGGER.info("GET /book/new");
        model.addAttribute("isNew", true);
        return "book";
    }

    /**
     * Add a book and return message by the result of adding
     * @param bookSample sample of the book that is to be added
     * @param bindingResult represents binding results of the bookSample
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to catalog or return book page when binding result has errors
     */
    @PostMapping(value = "/new")
    public String addBook(@Valid @ModelAttribute("bookSample") BookSample bookSample,
                          BindingResult bindingResult, Model model, HttpServletRequest request){
        LOGGER.info("POST /book/new, create a book");
        LOGGER.debug("bookSample={}", bookSample);
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "book";
        }
        String messageCode;
        if(bookService.createBook(bookSample)){
            messageCode = "message.book.create.good";
        } else {
            messageCode = "message.book.create.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "forward:/catalog";
    }

    /**
     * Goto a book page with model attribute "isNew" is false
     * and with found book by id. If the book is not found, forward to catalog.
     * @param bookId a book identification which will edit
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return book page or forward to catalog when the book was not found
     */
    @GetMapping(value = "/{bookId}")
    public String editBook(@PathVariable("bookId") Integer bookId, Model model,
                           HttpServletRequest request){
        LOGGER.info("GET /book/{}", bookId);
        BookSample bookSample = bookService.findBookById(bookId);
        if(bookSample == null){
            String message = messageSource.getMessage("error.not.found",
                    new String[]{"book", bookId.toString()},
                    localeResolver.resolveLocale(request));
            model.addAttribute("resultMessage", message);
            return "forward:/catalog";
        }
        model.addAttribute("isNew", false);
        model.addAttribute("bookSample", bookSample);
        return "book";
    }

    /**
     * Edit a book and return a message by the result of editing
     * @param bookSample sample of the book that is to be changed
     * @param bindingResult represents binding results of the bookSample
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to catalog or return book page when binding result has errors
     */
    @PostMapping(value = "/{bookId}")
    public String editBook(@Valid BookSample bookSample,
                           BindingResult bindingResult, Model model, HttpServletRequest request){
        LOGGER.info("POST /book/{}, edit a book", bookSample.getId());
        LOGGER.debug("bookSample={}", bookSample);
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "book";
        }
        String messageCode;
        if(bookService.editBook(bookSample)){
            messageCode = "message.book.edit.good";
        } else {
            messageCode = "message.book.edit.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "forward:/catalog";
    }


    /**
     * Delete a book by id and return message by the result of deleting
     * @param bookId identification of the book that is to be removed
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to catalog
     */
    @PostMapping(value = "/delete/{id}")
    public String deleteBook(@PathVariable("id") Integer bookId, Model model, HttpServletRequest request){
        LOGGER.info("Post delete a book /delete/bookId={}", bookId);
        String messageCode;
        if(bookService.delete(bookId)){
            messageCode = "message.book.delete.good";
        } else {
            messageCode = "message.book.delete.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "forward:/catalog";
    }
}
