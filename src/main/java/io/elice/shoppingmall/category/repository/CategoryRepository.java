package io.elice.shoppingmall.category.repository;

import io.elice.shoppingmall.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);

    List<Category> findByParentIdIsNull();

    List<Category> findByParentIdIsNotNull();
}
