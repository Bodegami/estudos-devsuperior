package br.com.bodegami.dscatalog.controllers;

import br.com.bodegami.dscatalog.dto.CategoryDTO;
import br.com.bodegami.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO dto = categoryService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO request) {
        CategoryDTO response = categoryService.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }


}
