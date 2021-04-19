package com.epam.brest.webapp;

import com.epam.brest.model.sample.BookSample;
import com.epam.brest.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping("/book/**")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Autowired
    public BookController(BookService bookService, LocaleResolver localeResolver, MessageSource messageSource) {
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
        model.addAttribute("bookSample", new BookSample());
        model.addAttribute("isNew", true);
        return "book";
    }

    /**
     * Add a book and return message by the result of adding
     * @param bookSample sample of the book that is to be added
     * @param bindingResult used for error registration capabilities, allowing for a {@link javax.validation.Validator}
     *                      to be applied.
     * @param model used for rendering views
     * @param request used for getting a current locale
     * @return redirect to result or return book page, if the binding result has errors
     */
    @PostMapping(value = "/new")
    public String addBook(@Valid BookSample bookSample, BindingResult bindingResult,
                          Model model, HttpServletRequest request){
        LOGGER.info("POST /book/new, create a book");
        LOGGER.debug("bookSample={}", bookSample);
        if(bindingResult.hasErrors()){
            LOGGER.info("validation errors");
            model.addAttribute("isNew", true);
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
        return "redirect:/result?resultMessage=" + message;
    }

    /**
     * Goto a book page with model attribute "isNew" is false
     * and with found book by identification.
     * @param bookId a book identification which will be editing
     * @param model used for rendering views
     * @param request used for getting a current locale
     * @return book page.
     */
    @GetMapping(value = "/{bookId}")
    public String editBook(@PathVariable("bookId") @Min(1) Integer bookId, Model model,
                           HttpServletRequest request){
        LOGGER.info("GET /book/{}", bookId);
        BookSample bookSample = bookService.findBookById(bookId);
        model.addAttribute("isNew", false);
        model.addAttribute("bookSample", bookSample);
        return "book";
    }

    /**
     * Edit the book and return a message by the result of editing
     * @param bookSample sample of the book that is to be changed
     * @param bindingResult used for error registration capabilities, allowing for a
     *                      {@link javax.validation.Validator} to be applied.
     * @param model used for rendering views
     * @param request used for getting a current locale
     * @return redirect to result or return book page, if the binding result has errors
     */
    @PostMapping(value = "/{bookId}")
    public String editBook(@Valid BookSample bookSample, BindingResult bindingResult,
                           Model model, HttpServletRequest request){
        LOGGER.info("POST /book/{}, edit a book", bookSample.getId());
        LOGGER.debug("bookSample={}", bookSample);
        if(bindingResult.hasErrors()){
            LOGGER.info("validation errors");
            model.addAttribute("isNew", false);
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
        return "redirect:/result?resultMessage=" + message;
    }


    /**
     * Delete the book by identification and return message by the result of deleting
     * @param bookId identification of the book that is to be removed
     * @param model used for rendering views
     * @param request used for getting a current locale
     * @return redirect to result
     */
    @GetMapping(value = "/delete/{id}")
    public String deleteBook(@PathVariable("id") @Min(1) Integer bookId, Model model, HttpServletRequest request){
        LOGGER.info("Post delete a book /delete/bookId={}", bookId);
        String messageCode;
        if(bookService.delete(bookId)){
            messageCode = "message.book.delete.good";
        } else {
            messageCode = "message.book.delete.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        return "redirect:/result?resultMessage=" + message;
    }
}
