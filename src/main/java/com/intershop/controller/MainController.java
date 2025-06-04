package com.intershop.controller;

import com.intershop.dto.*;
import com.intershop.service.CartService;
import com.intershop.service.ItemService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

    ItemService itemService;

    CartService cartService;


    public MainController(ItemService itemService, CartService cartService) {
        this.itemService = itemService;
        this.cartService = cartService;
    }

    @GetMapping("/items")
    public String getItems(Model model,
                           @RequestParam(name = "search", defaultValue = "") String search,
                           @RequestParam(name = "sort", defaultValue = "NO") ItemSortEnum sort,
                           @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                           @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sort.getSortColumnName()));
        ItemPageDto itemPageDto = itemService.findByTitle(search, pageable);

        int colCount = 2;
        List<List<ItemDto>> itemsByRows = new ArrayList<>();
        List<ItemDto> itemDtoRow = new ArrayList<>();
        itemsByRows.add(itemDtoRow);

        for (ItemDto itemDto : itemPageDto.getItemDtoList()) {
            itemDtoRow.add(itemDto);
            if (itemDtoRow.size() >= colCount) {
                itemDtoRow = new ArrayList<>();
                itemsByRows.add(itemDtoRow);
            }
        }

        model.addAttribute("items", itemsByRows);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort.name());
        model.addAttribute("paging", itemPageDto.getPagingDto());
        return "main";
    }

    @PostMapping("/items/{id}")
    public String changeCartItem(@PathVariable(name = "id") Long id, @RequestParam(name = "action") ItemActionEnum action) throws IOException {
        cartService.changeCartItem(id, action);
        return "redirect:/main/items";
    }

}
