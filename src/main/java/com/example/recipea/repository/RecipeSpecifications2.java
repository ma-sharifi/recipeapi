//package com.example.recipea.repository;
//
//import com.example.recipea.entity.Recipe;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.data.jpa.repository.Query;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import org.springframework.data.jpa.domain.Specification;
//import javax.persistence.criteria.ListJoin;
//import javax.persistence.criteria.Predicate;
//import java.util.List;
//
///**
// * @author Mahdi Sharifi
// */
//public class RecipeSpecifications2 implements Specification<Recipe> {
//
//    public static final double MINIMAL_AVERAGE_RATING = 8.0;
//
////    @Query(value = "SELECT r FROM Restaurant r LEFT JOIN r.ratings ra GROUP BY r having avg(ra.score) >= 8")
////    List<Restaurant> findTopRatedRestaurants();
////    @Query(value = "SELECT r FROM Restaurant r LEFT JOIN r.ratings ra "
////            + "where r.name like %:name% GROUP BY r having avg(ra.score) >= 8")
////    List<Restaurant> findTopRatedRestaurantsByName(@Param("name") String name);
//    @Override
//    public Predicate toPredicate(Root<Recipe> recipeRoot, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//        final var averageRating = criteriaBuilder.avg(recipeRoot.join("Restaurant_.RATINGS").get("Rating_.SCORE"));
//        final var topRatedPredicate = criteriaBuilder.greaterThanOrEqualTo(averageRating, MINIMAL_AVERAGE_RATING);
//        query.groupBy(recipeRoot.get("Restaurant_.ID")).having(topRatedPredicate);
//        return null;
//    }
////    @Query("SELECT DISTINCT r FROM Recipe r inner join Ingredient v on r.id=v.recipe.id inner join Tag t on r.id=t.recipe.id WHERE 1<2 ")
////    public static Specification<Recipe> getEmployeesByPhoneTypeSpec(String tagTitle) {
////        return (root, query, criteriaBuilder) -> {
//////            ListJoin<Recipe, Tag> phoneJoin = root.join("tags");
////            ListJoin<Recipe, Tag> phoneJoin = root.join(Recipe_.tags);
////            Predicate equalPredicate = criteriaBuilder.equal(phoneJoin.get(Phone_.type), phoneType);
////            query.distinct(true);
////            return equalPredicate;
////        };
////    }
//    public Predicate toPredicate2(Root<Recipe> recipeRoot, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//        final var averageRating = criteriaBuilder.avg(recipeRoot.join("Recipe_.RATINGS").get("Recipe_.SCORE"));
//        final var topRatedPredicate = criteriaBuilder.greaterThanOrEqualTo(averageRating, MINIMAL_AVERAGE_RATING);
//        query.groupBy(recipeRoot.get("Recipe_.ID")).having(topRatedPredicate);
//        return null;
//    }
//}
