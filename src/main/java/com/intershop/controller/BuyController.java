package com.intershop.controller;

import com.intershop.service.BuyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("buy")
public class BuyController {

    BuyService buyService;

    public BuyController(BuyService buyService) {
        this.buyService = buyService;
    }

    @PostMapping
    public String buy(){
        Long orderId = buyService.buyCart();
        return "redirect:/orders/%d?newOrder=true".formatted(orderId);
    }
}
