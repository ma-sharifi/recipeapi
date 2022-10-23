package com.example.recipea.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mahdi Sharifi
 */
@Entity
@Table(name = "T_RECIPE",
        indexes = {
//              @Index(name = "IDX_RECIPE_INSTRUCTION", columnList = "INSTRUCTION"), // It depends-> I recommand save this field + id on Elasticsearch
                @Index(name = "IDX_RECIPE_SERVE", columnList = "SERVE")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UNQ_TITLE_USERNAEM", columnNames = {"TITLE", "USERNAME"})
        })

@Setter
@Getter
@NoArgsConstructor
public class Recipe extends BaseEntity {

    @Column(name = "TITLE", length = 60, nullable = false)
    private String title;

    @Column(name = "VEGETARIAN")
    private boolean vegetarian;

    @Column(name = "INSTRUCTION", length = 2048, nullable = false)
    private String instruction;

    @Column(name = "SERVE", nullable = false)
    private Integer serve;

    @Column(name = "USERNAME", length = 60,nullable = false)
    private String username;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "JND_RECIPE_INGREDIENT",
            joinColumns = @JoinColumn(name = "RECIPE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "INGREDIENT_ID", referencedColumnName = "ID")
    )
    @JsonIgnoreProperties(value = {"recipes"}, allowSetters = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    public Recipe(String title, String instruction, Integer serve, String username) {
        this.title = title;
        this.instruction = instruction;
        this.serve = serve;
        this.username = username;
    }

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return getId() != null && getId().equals(((Recipe) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + getId() +
                ", title='" + getTitle() + "'" +
                ", ingredients='" + getIngredients() + "'" +
                ", instruction='" + getInstruction() + "'" +
                ", serve=" + getServe() +
                "}";
    }
}
