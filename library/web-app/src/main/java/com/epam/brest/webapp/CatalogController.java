package com.epam.brest.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogController {

    @GetMapping(value = "/")
    public String defaultPageRedirect(){
        return "redirect:catalog";
    }

}
