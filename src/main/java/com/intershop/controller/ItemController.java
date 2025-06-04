package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("items")
public class ItemController {

    ItemService itemService;

    CartService cartService;

    public ItemController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public String getItems(Model model, @PathVariable(name = "id") Long id) {
        model.addAttribute("item", itemService.findByItemId(id));
        return "item";
    }

    @PostMapping("/{id}")
    public String changeCartItem(@PathVariable(name = "id") Long id, @RequestParam(name = "action") ItemActionEnum action) throws IOException {
        cartService.changeCartItem(id, action);
        return "redirect:/items/%d".formatted(id);
    }

}
