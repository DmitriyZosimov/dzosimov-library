package com.epam.brest.webapp;

import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.service.IBookService;
import com.epam.brest.service.IReaderService;
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

@Controller
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    private final IReaderService readerService;
    private final IBookService bookService;
    private SessionLocaleResolver localeResolver;
    private final MessageSource messageSource;

    @Autowired
    public ProfileController(IReaderService readerService, IBookService bookService, SessionLocaleResolver localeResolver, MessageSource messageSource) {
        this.readerService = readerService;
        this.bookService = bookService;
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    /**
     * Goto profile list page. If the profile is not found, forward to catalog
     * with message that reader is not found by identification
     * @param model used for rendering views
     * @param session HttpSession is necessary to get reader id
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return view profile or forward to catalog
     */
    @GetMapping(value = "/profile")
    public String getProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("GET /profile");
        Integer card = (Integer) session.getAttribute("libraryCard");
        LOGGER.debug("card={}", card);
        ReaderSample readerSample = readerService.getProfile(card);
        if(readerSample == null){
            String message = messageSource.getMessage("error.not.found",
                    new String[]{"reader", card.toString()}, localeResolver.resolveLocale(request));
            model.addAttribute("resultMessage", message);
            return "forward:/catalog";
        }
        model.addAttribute("readerSample", readerSample);
        LOGGER.debug("READER: {}", readerSample);
        return "profile";
    }

    /**
     * Goto a reader page with model attribute "isNew" is false
     * and with found reader by id. If the reader is not found, forward to catalog.
     * @param model used for rendering views
     * @param session HttpSession is necessary to get reader id
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return book page or forward to catalog when the book was not found
     */
    @GetMapping(value = "/profile/edit")
    public String editProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("GET /profile/edit");
        Integer card = (Integer) session.getAttribute("libraryCard");
        ReaderSample readerSample = readerService.getProfileWithoutBooks(card);
        if(readerSample == null){
            String message = messageSource.getMessage("error.not.found",
                    new String[]{"reader", card.toString()}, localeResolver.resolveLocale(request));
            model.addAttribute("resultMessage", message);
            return "forward:/catalog";
        }
        model.addAttribute("readerSample", readerSample);
        LOGGER.debug("READER: {}", readerSample);
        model.addAttribute("isNew", false);
        return "reader";
    }

    /**
     * Edit a reader and return a message by the result of editing.
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to profile or return reader page when a binding result has errors
     */
    @PostMapping(value = "/profile/edit")
    public String editProfile(@Valid @ModelAttribute("readerSample") ReaderSample readerSample,
                              BindingResult bindingResult, Model model, HttpServletRequest request){
        LOGGER.info("POST /profile/edit");
        if(bindingResult.hasErrors()){
            LOGGER.info("bindingResult has errors");
            return "reader";
        }
        String messageCode;
        if(readerService.editProfile(readerSample)){
            messageCode = "message.reader.edit.good";
        } else {
            messageCode = "message.reader.edit.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "forward:/profile";
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
     * @param bindingResult represents binding results of the readerSample
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to catalog or return reader page when a binding result has errors
     */
    @PostMapping(value = "/registry")
    public String createReader(@Valid @ModelAttribute("readerSample") ReaderSample readerSample,
                               BindingResult bindingResult, Model model, HttpServletRequest request) {
        LOGGER.info("POST /registry");
        if(bindingResult.hasErrors()){
            LOGGER.info("bindingResult has errors");
            return "reader";
        }
        String messageCode;
        Integer[] objsForMessage = null;
        ReaderSample reader = readerService.createReader(readerSample);
        if(reader != null) {
            messageCode = "message.reader.create.good";
            objsForMessage = new Integer[]{reader.getReaderId()};
        } else {
            messageCode = "message.reader.create.bad";
        }
        String message = messageSource.getMessage(messageCode,
                objsForMessage, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "forward:/catalog";
    }

    /**
     * Delete a reader and return a message by the result of deleting.
     * @param model used for rendering views
     * @param session HttpSession is necessary to get reader id
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to catalog
     */
    @PostMapping(value = "/profile/delete")
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
        model.addAttribute("resultMessage", message);
        return "forward:/catalog";
    }

    /**
     * Restore a reader and return a message by the result of deleting.
     * @param card reader identification that is to be restored
     * @param model used for rendering views
     * @param request HttpServletRequest is necessary for getting a current locale
     * @return forward to catalog
     */
    @GetMapping(value = "/restore/{card}")
    public String restoreProfile(@PathVariable("card") Integer card,
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
        model.addAttribute("resultMessage", message);
        return "forward:/catalog";
    }
    /**
     * Delete a book by identification from reader books list.
     * @param bookId book identification that is to be deleted
     * @param model used for rendering views
     * @param session HttpSession is necessary to get reader id
     * @return redirect to profile
     */
    @PostMapping(value = "/profile/book/delete/{book}")
    public String deleteBookFromProfile(@PathVariable("book") Integer bookId,
                                        Model model, HttpSession session){
        LOGGER.info("POST /profile/book/delete/bookId={}", bookId);
        Integer readerId = (Integer) session.getAttribute("libraryCard");
        bookService.removeFieldReaderFromBook(bookId, readerId);
        return "redirect:/profile";
    }

}
