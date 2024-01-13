package br.com.bodegami.dscatalog.repositories;

import br.com.bodegami.dscatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long > {
}
