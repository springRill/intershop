package com.intershop.controller;

import com.intershop.dto.ItemActionEnum;
import com.intershop.dto.ItemDto;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    ItemService itemService;

    CartService cartService;

    public CartController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping("/items")
    public String getCartItems(Model model) {
        List<ItemDto> itemDtoList = itemService.getCartItems();
        Double total = itemDtoList.stream().mapToDouble(itemDto -> itemDto.getCount() * itemDto.getPrice()).sum();

        model.addAttribute("items", itemDtoList);
        model.addAttribute("total", BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("empty", itemDtoList.isEmpty());
        return "cart";
    }

    @PostMapping("/items/{id}")
    public String changeCartItem(@PathVariable(name = "id") Long id, @RequestParam(name = "action") ItemActionEnum action) throws IOException {
        cartService.changeCartItem(id, action);
        return "redirect:/cart/items";
    }

}
