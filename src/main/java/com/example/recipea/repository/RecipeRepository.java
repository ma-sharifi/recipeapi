package com.example.recipea.repository;

import com.example.recipea.entity.Recipe;
import com.example.recipea.service.dto.RecipeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Mahdi Sharifi
 * Repository for the Recipe entity.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    Page<Recipe> findByUsername(Pageable pageable,String username);
    Optional<Recipe> findByIdAndUsername(Long id,String username);
    @Transactional
    void deleteByIdAndUsername(Long id,String username);
}
