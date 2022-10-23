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
@Table(name = "T_INGREDIENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNQ_INGRE_TITLE", columnNames = {"TITLE"})
        })

@Getter
@Setter
@NoArgsConstructor
public class Ingredient extends BaseEntity {

    @Column(name = "title", length = 60, nullable = false)
    private String title;

    //The mappedBy attribute of the recipes here marks that, in this bidirectional relationship, the Recipe entity owns the association.
    @JsonIgnoreProperties(value = {"ingredients", "parent", "tags"}, allowSetters = true)
    @ManyToMany(mappedBy = "ingredients")
    //bidirectional relationship-> you must use mappedBy on one side of the relationship, otherwise it will be assumed to be two different relationships and you will get duplicate rows inserted into the join table.
    private Set<Recipe> recipes = new HashSet<>();


    public Ingredient(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingredient)) {
            return false;
        }
        return getId() != null && getId().equals(((Ingredient) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
