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
        String mercadoPagoSamplePublicKey = "TEST-6ff32754-d32e-4bc7-b5db-e11e7bbcb786";
        model.addAttribute("publicKey", mercadoPagoSamplePublicKey);
        return "index";
    }
}
