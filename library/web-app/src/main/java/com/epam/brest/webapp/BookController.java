package com.epam.brest.webapp;

import com.epam.brest.model.dto.BookDto;
import com.epam.brest.service.IBookService;
import com.epam.brest.service.exception.BookCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@SessionAttributes({"bookDto"})
@Controller
@RequestMapping("/book/**")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Goto book page
     *
     * @return view add_book
     */
    @GetMapping(value = {"/add"})
    public String addBook(){
        LOGGER.info("GET /book/add, response bookDto");
        return "book";
    }

    /**
     * Add a book to DAO
     *
     * @param bookDto request model a book
     * @return view catalog
     */
    @PostMapping(value = "/add")
    public String addBook(@ModelAttribute("bookDto") BookDto bookDto, Model model, BindingResult bindingResult){
        LOGGER.info("POST /add, create a book");
        LOGGER.debug("bookDto={}", bookDto);
        if(bindingResult.hasErrors()){
            LOGGER.error("BindingResult has errors");
            return "error";
        }
        try {
            bookService.createBook(bookDto);
            model.addAttribute("result", true);
        } catch (BookCreationException e) {
            LOGGER.warn(e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        return "redirect:/catalog";
    }

    /**
     * Delete a book
     * and goto catalog list page.
     *
     * @param bookId book id
     * @return view catalog
     */
    @PostMapping(value = "/delete/{id}")
    public String deleteBook(@PathVariable("id") Integer bookId, Model model){
        LOGGER.info("Post delete a book /delete/bookId={}", bookId);

        if(bookService.delete(bookId)){
            LOGGER.info("The book {} was deleted", bookId);
            model.addAttribute("result", true);
        } else {
            LOGGER.info("The book {} was not deleted", bookId);
            model.addAttribute("result", false);
        }
        return "redirect:/result";
    }
}
