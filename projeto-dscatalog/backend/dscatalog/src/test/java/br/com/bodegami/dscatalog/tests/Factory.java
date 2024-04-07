package br.com.bodegami.dscatalog.tests;

import br.com.bodegami.dscatalog.dto.ProductDTO;
import br.com.bodegami.dscatalog.entities.Category;
import br.com.bodegami.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product =  new Product(1L, "Phone", "Good Phone", 800.0,
                "https://img.com/img.png", Instant.parse("2024-04-06T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
