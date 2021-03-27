package com.epam.brest.webapp;

import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.model.sample.ReaderSample;
import com.epam.brest.service.IBookService;
import com.epam.brest.service.IReaderService;
import com.epam.brest.service.exception.ReaderCreationException;
import com.epam.brest.service.exception.ReaderNotFoundException;
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
     * Goto profile list page.
     *
     * @param model
     * @param session keep a library card of a reader
     * @return view profile
     */
    @GetMapping(value = "/profile")
    public String getProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("GET /profile");
        Integer card = (Integer) session.getAttribute("libraryCard");
        LOGGER.debug("card={}", card);
        ReaderSample readerSample = readerService.getProfile(card);
        if(readerSample == null){
            String message = messageSource.getMessage("error.not.found",
                    new String[]{"reader"}, localeResolver.resolveLocale(request));
            model.addAttribute("message", message);
            return "error";
        }
        model.addAttribute("readerSample", readerSample);
        LOGGER.debug("READER: {}", readerSample);
        return "profile";
    }

    /**
     * Go to edit_profile list page
     *
     * @param model
     * @param session keep a library card of a reader
     * @return view edit_profile
     */
    @GetMapping(value = "/profile/edit")
    public String editProfile(Model model, HttpSession session, HttpServletRequest request){
        LOGGER.info("GET /profile/edit");
        Integer card = (Integer) session.getAttribute("libraryCard");
        ReaderSample readerSample = readerService.getProfileWithoutBooks(card);
        if(readerSample == null){
            String message = messageSource.getMessage("error.not.found",
                    new String[]{"reader"}, localeResolver.resolveLocale(request));
            model.addAttribute("message", message);
            return "error";
        }
        model.addAttribute("readerSample", readerSample);
        LOGGER.debug("READER: {}", readerSample);
        model.addAttribute("isNew", false);
        return "reader";
    }

    /**
     * Edit profile and go to profile list page.
     *
     * @param readerSample with field data
     * @return view profile
     */
    @PostMapping(value = "/profile/edit")
    public String editProfile(@ModelAttribute("readerSample") ReaderSample readerSample, HttpSession session,
                              Model model, BindingResult bindingResult, HttpServletRequest request){
        LOGGER.info("POST /profile/edit");
        if(bindingResult.hasErrors()){
            LOGGER.info("bindingResult has errors");
            return "error";
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
        return getProfile(model, session, request);
    }

    @GetMapping(value = "/registry")
    public String createReader(Model model){
        LOGGER.info("GET /registry");
        model.addAttribute("readerSample", new ReaderSample());
        model.addAttribute("isNew", true);
        return "reader";
    }

    @PostMapping(value = "/registry")
    public String createReader(@ModelAttribute("readerSample") ReaderSample readerSample,
                               Model model, BindingResult bindingResult, HttpServletRequest request) {
        LOGGER.info("POST /registry");
        if(bindingResult.hasErrors()){
            LOGGER.info("bindingResult has errors");
            return "error";
        }
        String messageCode;
        ReaderSample reader = readerService.createReader(readerSample);
        if(reader != null) {
            messageCode = "message.reader.create.good";
            model.addAttribute("card", reader.getReaderId());
        } else {
            messageCode = "message.reader.create.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "redirect:/result/card";
    }

    /**
     * Remove profile
     *
     * @return view result
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
        return "redirect:/result";
    }

    /**
     * Restore profile
     *
     * @param card profile
     * @return view catalog
     */
    @GetMapping(value = "/restore/{card}")
    public String restoreProfile(@PathVariable("card") Integer card, Model model, HttpServletRequest request){
        LOGGER.info("GET /profile/restore/card={}", card);

        String messageCode;
        if(readerService.restoreProfile(card)){
            messageCode = "message.reader.restore.good";
            model.addAttribute("card", card);
        } else {
            messageCode = "message.reader.restore.bad";
        }
        String message = messageSource.getMessage(messageCode, null, localeResolver.resolveLocale(request));
        LOGGER.info(message);
        model.addAttribute("resultMessage", message);
        return "redirect:/result/card";
    }

    //TODO Mock test
    @PostMapping(value = "/profile/book/delete/{book}")
    public String deleteBookFromProfile(@PathVariable("book") Integer bookId, Model model, HttpSession session){
        LOGGER.info("POST /profile/book/delete/bookId={}", bookId);
        Integer readerId = (Integer) session.getAttribute("libraryCard");
        bookService.removeFieldReaderFromBook(bookId, readerId);
        return "redirect:/profile";
    }

}
