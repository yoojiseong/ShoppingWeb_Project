package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}