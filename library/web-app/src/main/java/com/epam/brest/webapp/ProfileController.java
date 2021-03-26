package com.epam.brest.webapp;

import com.epam.brest.model.dto.ReaderDto;
import com.epam.brest.service.IReaderService;
import com.epam.brest.service.exception.ReaderCreationException;
import com.epam.brest.service.exception.ReaderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    private final IReaderService readerService;

    @Autowired
    public ProfileController(IReaderService readerService) {
        this.readerService = readerService;
    }

    /**
     * Goto profile list page.
     *
     * @param model
     * @param session keep a library card of a reader
     * @return view profile
     */
    @GetMapping(value = "/profile")
    public String getProfile(Model model, HttpSession session){
        LOGGER.info("GET /profile");
        Integer card = (Integer) session.getAttribute("libraryCard");
        LOGGER.debug("card={}", card);
        ReaderDto readerDto = null;
        try {
            readerDto = readerService.getProfile(card);
        } catch (ReaderNotFoundException e) {
            LOGGER.warn("ReaderNotFoundException: {}", e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        model.addAttribute("readerDto", readerDto);
        LOGGER.debug("READER: {}", readerDto);
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
    public String editProfile(Model model, HttpSession session){
        LOGGER.info("GET /profile/edit");
        Integer card = (Integer) session.getAttribute("libraryCard");
        ReaderDto readerDto = null;
        try {
            readerDto = readerService.getProfileWithoutBooks(card);
        } catch (ReaderNotFoundException e) {
            LOGGER.warn("ReaderNotFoundException: {}", e.getMessage());
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        model.addAttribute("readerDto", readerDto);
        model.addAttribute("isNew", false);
        return "reader";
    }

    /**
     * Edit profile and go to profile list page.
     *
     * @param readerDto with field data
     * @return view profile
     */
    @PostMapping(value = "/profile/edit")
    public String editProfile(@ModelAttribute("readerDto") ReaderDto readerDto, HttpSession session,
                              Model model, BindingResult bindingResult){
        LOGGER.info("POST /profile/edit");
        if(bindingResult.hasErrors()){
            LOGGER.info("bindingResult has errors");
            return "error";
        }
        if(readerService.editProfile(readerDto)){
            model.addAttribute("result", true);
        } else {
            model.addAttribute("result", false);
        }
        return getProfile(model, session);
    }

    @GetMapping(value = "/registry")
    public String createReader(Model model){
        LOGGER.info("GET /registry");
        model.addAttribute("readerDto", new ReaderDto());
        model.addAttribute("isNew", true);
        return "reader";
    }

    @PostMapping(value = "/registry")
    public String createReader(@ModelAttribute("readerDto") ReaderDto readerDto,
                               Model model, BindingResult bindingResult) throws ReaderCreationException {
        LOGGER.info("POST /registry");
        if(bindingResult.hasErrors()){
            LOGGER.info("bindingResult has errors");
            return "error";
        }
        try{
            ReaderDto reader = readerService.createReader(readerDto);
            model.addAttribute("result", true);
            model.addAttribute("card", reader.getReaderId());
        } catch (ReaderCreationException e){
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        return "redirect:/result/card";
    }

    /**
     * Remove profile
     *
     * @return view result
     */
    @PostMapping(value = "/profile/delete")
    public String removeProfile(Model model, HttpSession session){
        LOGGER.info("POST /profile/delete");
        Integer card = (Integer) session.getAttribute("libraryCard");
        if(readerService.removeProfile(card)){
            LOGGER.info("a reader(id={}) was removed", card);
            session.invalidate();
            model.addAttribute("result", true);
            return "redirect:/catalog";
        } else {
            LOGGER.info("a reader(id={}) was not removed", card);
            model.addAttribute("result", false);
            return "redirect:/profile";
        }
    }

    /**
     * Restore profile
     *
     * @param card profile
     * @return view result
     */
    @GetMapping(value = "/restore/{card}")
    public String restoreProfile(@PathVariable("card") Integer card, Model model){
        LOGGER.info("GET /profile/restore/card={}", card);

        if(readerService.restoreProfile(card)){
            model.addAttribute("result", true);
            model.addAttribute("card", card);
            return "redirect:/result/card";
        } else {
            model.addAttribute("result", false);
            return "redirect:/catalog";
        }
    }

}
