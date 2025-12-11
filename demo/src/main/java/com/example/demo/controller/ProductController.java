package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.config.ImageStorageConfig;
import com.example.demo.utils.FileUploadUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    @Autowired
    private ImageStorageConfig imageStorageConfig;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String show_Product(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            Model model) {
        List<Product> list_Product = productService.getAllProducts()
                .stream()
                .filter(p -> keyword == null || p.getName().contains(keyword))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice).toList();
        model.addAttribute("list_Product", list_Product);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "product/show-product";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("categoryId") Long categoryId,
            RedirectAttributes redirectAttributes) {
        try {
            String fileName = imageFile.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty()) {
                FileUploadUtil.saveFile(imageStorageConfig.getUploadDir(), fileName, imageFile);
                product.setImage(fileName);
            }
            productService.addProduct(product.getName(), product.getPrice(), product.getImage(), categoryId);
            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        return "redirect:/product";
    }
}
