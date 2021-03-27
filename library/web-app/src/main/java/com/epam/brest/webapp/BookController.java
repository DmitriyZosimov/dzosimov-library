package com.epam.brest.webapp;

import com.epam.brest.model.Book;
import com.epam.brest.model.sample.BookSample;
import com.epam.brest.service.IBookService;
import com.epam.brest.service.exception.BookCreationException;
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
     *
     * @return view add_book
     */
    @GetMapping(value = {"/add"})
    public String addBook(Model model){
        LOGGER.info("GET /book/add");
        model.addAttribute("isNew", true);
        return "book";
    }

    /**
     * Add a book to DAO
     *
     * @param bookSample request model of a book
     * @return view catalog
     */
    @PostMapping(value = "/add")
    public String addBook(@ModelAttribute("bookSample") BookSample bookSample, Model model,
                          BindingResult bindingResult, HttpServletRequest request){
        LOGGER.info("POST /add, create a book");
        LOGGER.debug("bookSample={}", bookSample);
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "error";
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
        return "redirect:/result";
    }

    //TODO:test
    @GetMapping(value = "/edit/{bookId}")
    public String editBook(@PathVariable("bookId") Integer bookId, Model model, HttpServletRequest request){
        LOGGER.info("GET /book/edit");
        BookSample bookSample = bookService.findBookById(bookId);
        if(bookSample == null){
            String message = messageSource.getMessage("error.not.found",
                    new String[]{"book"}, localeResolver.resolveLocale(request));
            model.addAttribute("message", message);
            return "error";
        }
        model.addAttribute("isNew", false);
        model.addAttribute("bookSample", bookSample);
        return "book";
    }

    //TODO:mock test
    @PostMapping(value = "/edit/{bookId}")
    public String editBook(@ModelAttribute("bookSample") BookSample bookSample,
                           Model model, BindingResult bindingResult, HttpServletRequest request){
        LOGGER.info("POST /book/edit, edit a book");
        LOGGER.debug("bookSample={}", bookSample);
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "error";
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
        return "redirect:/result";
    }


    /**
     * Delete a book
     * and goto catalog list page.
     *
     * @param bookId book id
     * @return view catalog
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
        return "redirect:/result";
    }
}
