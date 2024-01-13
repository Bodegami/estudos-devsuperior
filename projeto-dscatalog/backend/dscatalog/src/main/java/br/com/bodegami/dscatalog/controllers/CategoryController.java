package br.com.bodegami.dscatalog.controllers;

import br.com.bodegami.dscatalog.entities.Category;
import br.com.bodegami.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {

        List<Category> categories = categoryService.findAll();

        return ResponseEntity.ok(categories);
    }

}
