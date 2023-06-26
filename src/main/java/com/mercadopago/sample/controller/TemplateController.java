package com.mercadopago.sample.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping
    public String renderMainPage(Model model) {
        String mercadoPagoSamplePublicKey = "";
        model.addAttribute("publicKey", mercadoPagoSamplePublicKey);
        return "index";
    }
}
