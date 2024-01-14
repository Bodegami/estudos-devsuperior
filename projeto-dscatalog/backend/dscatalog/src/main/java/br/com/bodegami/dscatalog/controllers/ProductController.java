package br.com.bodegami.dscatalog.controllers;

import br.com.bodegami.dscatalog.dto.ProductDTO;

import br.com.bodegami.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                    @RequestParam(value = "direction", defaultValue = "DESC") String direction,
                                                    @RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ProductDTO> categories = productService.findAllPaged(pageRequest);
        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = productService.findById(id);
        return ResponseEntity.ok(dto);
    }
//
//    @PostMapping
//    public ResponseEntity<ProductDTO> insert(@RequestBody CategoryDTO request) {
//        ProductDTO response = productService.insert(request);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
//                .path("/{id}")
//                .buildAndExpand(response.id())
//                .toUri();
//
//        return ResponseEntity.created(uri).body(response);
//    }

//    @PutMapping(value = "/{id}")
//    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO request) {
//        ProductDTO response = productService.update(id, request);
//        return ResponseEntity.ok(response);
//    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
