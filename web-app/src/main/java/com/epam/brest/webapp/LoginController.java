package com.epam.brest.webapp;

import com.epam.brest.service.LoginService;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

  private LoginService loginService;

  public LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  /**
   * Goto login page
   *
   * @return view login
   */
  @GetMapping("/login")
  public String login() {
    LOGGER.info("GET /login");
    return "login";
  }

  /**
   * Added to session the library card (reader id)
   *
   * @param card    reader identification that is to be added
   * @param session used for adding the reader identification
   * @param model   used for rendering views
   * @return redirect to catalog or goto view login, if the reader was removed
   */
  @PostMapping("/login")
  public String login(@RequestParam("card") Integer card,
      HttpSession session, Model model) {
    LOGGER.info("POST /login");
    LOGGER.debug("card={}", card);
    if (loginService.isExistCard(card)) {
      LOGGER.info("card is exist and add to session");
      session.setAttribute("libraryCard", card);
    } else if (loginService.isRemovedCard(card)) {
      LOGGER.info("card is removed");
      model.addAttribute("isRemoved", true);
      model.addAttribute("removedCard", card);
      return "login";
    }
    return "redirect:/catalog";
  }

  /**
   * The current session invalidate
   *
   * @param session used to invalidate
   * @return redirect to catalog
   */
  @GetMapping("/logout")
  public String logout(HttpSession session) {
    LOGGER.info("GET /logout");
    session.invalidate();
    return "redirect:/catalog";
  }
}
