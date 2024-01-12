package br.com.bodegami.dscatalog.controllers;

import br.com.bodegami.dscatalog.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Books"));
        categories.add(new Category(2L, "Eletronics"));

        return ResponseEntity.ok(categories);
    }

}
