package com.example.recipea.repository;

import com.example.recipea.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Mahdi Sharifi
 * Repository for the Ingredient entity.
 */

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
