package com.epam.brest.webapp;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.model.sample.SearchReaderSample;
import com.epam.brest.service.BookService;
import com.epam.brest.service.ReaderService;
import com.epam.brest.service.SearchReaderValidator;
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
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Controller
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    private final ReaderService readerService;
    private final BookService bookService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    private final SearchReaderValidator searchReaderValidator;

    @Autowired
    public ProfileController(ReaderService readerService, BookService bookService, LocaleResolver localeResolver,
                             MessageSource messageSource, SearchReaderValidator searchReaderValidator) {
        this.readerService = readerService;
        this.bookService = bookService;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
        this.searchReaderValidator = searchReaderValidator;
    }

    /**
     * Goto readers list page
     * @param model used for rendering views
     * @return view readers page
     */
    @GetMapping(value = "/profiles")
    public String getAllReaders(Model model){
        LOGGER.info("GET /profiles");
        List<ReaderSample> readers = readerService.findAll();
        model.addAttribute("readers", readers);
        model.addAttribute(new SearchReaderSample());
        return "readers";
    }

    /**
     * Goto profile list page.
     * @param model used for rendering views
     * @param session {@link HttpSession} is necessary to get reader id
     * @param request {@link HttpServletRequest} is necessary for getting a current locale
     * @return view profile page
     */
    @GetMapping(value = "/profile")
    public String getProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("GET /profile");
        Integer card = (Integer) session.getAttribute("libraryCard");
        LOGGER.debug("card={}", card);
        ReaderSample readerSample = readerService.getProfile(card);
        model.addAttribute("readerSample", readerSample);
        LOGGER.debug("READER: {}", readerSample);
        return "profile";
    }

    /**
     * Goto a reader page with model attribute "isNew" is false
     * and with found reader by identification.
     * @param model used for rendering views
     * @param session {@link HttpSession} is necessary to get reader id
     * @param request {@link HttpServletRequest} is necessary for getting a current locale
     * @return book page.
     */
    @GetMapping(value = "/profile/edit")
    public String editProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("GET /profile/edit");
        Integer card = (Integer) session.getAttribute("libraryCard");
        ReaderSample readerSample = readerService.getProfileWithoutBooks(card);
        model.addAttribute("readerSample", readerSample);
        LOGGER.debug("READER: {}", readerSample);
        model.addAttribute("isNew", false);
        return "reader";
    }

    /**
     * Edit the reader and return a message by the result of editing.
     * @param readerSample sample of the reader who is to editing
     * @param bindingResult used for error registration capabilities, allowing for a
     *                      {@link javax.validation.Validator} to be applied.
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return redirect to result or return reader page, if the binding result has errors
     */
    @PostMapping(value = "/profile/edit")
    public String editProfile(@Valid ReaderSample readerSample, BindingResult bindingResult,
                              Model model, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            model.addAttribute("isNew", false);
            return "reader";
        }
        LOGGER.info("POST /profile/edit");
        String messageCode;
        if(readerService.editProfile(readerSample)){
            messageCode = "message.reader.edit.good";
        } else {
            messageCode = "message.reader.edit.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        return "redirect:/result?resultMessage=" + message;
    }

    /**
     * Goto a reader page with model attribute "isNew" is true
     * @param model used for rendering views
     * @return view reader
     */
    @GetMapping(value = "/registry")
    public String createReader(Model model){
        LOGGER.info("GET /registry");
        model.addAttribute("readerSample", new ReaderSample());
        model.addAttribute("isNew", true);
        return "reader";
    }

    /**
     * Add a new reader and return a message by the result of adding.
     * @param readerSample sample of the reader that is to be added
     * @param bindingResult used for error registration capabilities, allowing for a
     *                      {@link javax.validation.Validator} to be applied.
     * @param model used for rendering views
     * @param request {@link HttpServletRequest} is necessary for getting a current locale
     * @return redirect to catalog or return reader page, if the binding result has errors
     */
    @PostMapping(value = "/registry")
    public String createReader(@Valid ReaderSample readerSample, BindingResult bindingResult,
                               Model model, HttpServletRequest request) {
        LOGGER.info("POST /registry");
        if(bindingResult.hasErrors()){
            model.addAttribute("isNew", true);
            return "reader";
        }
        String messageCode;
        Integer[] objsForMessage = null;
        ReaderSample reader = readerService.createReader(readerSample);
        if(reader.getReaderId() != null) {
            messageCode = "message.reader.create.good";
            objsForMessage = new Integer[]{reader.getReaderId()};
        } else {
            messageCode = "message.reader.create.bad";
        }
        String message = messageSource.getMessage(messageCode,
                objsForMessage, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        return "redirect:/result?resultMessage=" + message;
    }

    /**
     * Delete the reader and return a message by the result of deleting.
     * @param model used for rendering views
     * @param session {@link HttpSession} is necessary to get reader id
     * @param request {@link HttpServletRequest} is necessary for getting a current locale
     * @return redirect to result
     */
    @GetMapping(value = "/profile/delete")
    public String removeProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("POST /profile/delete");
        Integer card = (Integer) session.getAttribute("libraryCard");
        String messageCode;
        if(readerService.removeProfile(card)){
            LOGGER.info("a reader(id={}) was removed", card);
            session.invalidate();
            messageCode = "message.reader.delete.good";
        } else {
            messageCode = "message.reader.delete.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        return "redirect:/result?resultMessage=" + message;
    }

    /**
     * Restore the reader and return a message by the result of deleting.
     * @param card reader identification that is to be restored
     * @param model used for rendering views
     * @param request {@link HttpServletRequest} is necessary for getting a current locale
     * @return redirect to result
     */
    @GetMapping(value = "/restore/{card}")
    public String restoreProfile(@PathVariable("card") @Min(1) Integer card,
                                 Model model, HttpServletRequest request){
        LOGGER.info("GET /restore/card={}", card);
        String messageCode;
        Integer[] objsForMessage = null;
        if(readerService.restoreProfile(card)){
            messageCode = "message.reader.restore.good";
            objsForMessage = new Integer[]{card};
        } else {
            messageCode = "message.reader.restore.bad";
        }
        String message = messageSource.getMessage(messageCode, objsForMessage,
                localeResolver.resolveLocale(request));
        LOGGER.info(message);
        return "redirect:/result?resultMessage=" + message;
    }
    /**
     * Delete a book by identification from reader books list.
     * @param bookId book identification that is to be deleted
     * @param model used for rendering views
     * @param session {@link HttpSession} is necessary to get reader id
     * @return redirect to profile
     */
    @GetMapping(value = "/profile/book/delete/{book}")
    public String deleteBookFromProfile(@PathVariable("book") @Min(1) Integer bookId,
                                        Model model, HttpSession session){
        LOGGER.info("POST /profile/book/delete/bookId={}", bookId);
        Integer readerId = (Integer) session.getAttribute("libraryCard");
        bookService.removeFieldReaderFromBook(bookId, readerId);
        return "redirect:/profile";
    }

    @GetMapping(value = "/readers/search")
    public String searchReaderByDate(SearchReaderSample searchReaderSample, BindingResult bindingResult,
                                     Model model){
        LOGGER.info("GET /readers/search");
        searchReaderValidator.validate(searchReaderSample, bindingResult);
        if(bindingResult.hasErrors()){
            return "readers";
        }
        model.addAttribute("readers", readerService.searchReaders(searchReaderSample));
        return "readers";
    }

}
