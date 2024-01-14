package br.com.bodegami.dscatalog.repositories;

import br.com.bodegami.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
