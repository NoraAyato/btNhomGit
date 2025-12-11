package com.example.demo.controller;

import com.example.demo.model.Product;
//import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
//import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String show_Product(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice",required = false) Double minPrice,
            @RequestParam(name = "maxPrice",required = false) Double maxPrice,
            Model model){
        List<Product> list_Product = productService.getAllProducts()
                .stream()
                .filter(p ->minPrice == null || p.getPrice() >= minPrice)
                .filter(p ->maxPrice == null || p.getPrice() <= maxPrice).toList();
        model.addAttribute("list_Product",list_Product);
        model.addAttribute("keyword",keyword);
        model.addAttribute("minPrice",minPrice);
        model.addAttribute("maxPrice",maxPrice);
        return "product/show-product";
    }
}
