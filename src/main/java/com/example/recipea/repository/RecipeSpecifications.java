//package com.example.recipea.repository;
//
//import com.example.recipea.entity.Recipe;
//import org.springframework.data.jpa.domain.Specification;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.time.LocalDate;
//
///**
// * @author Mahdi Sharifi
// */
////https://jschmitz.dev/posts/how_to_use_spring_datas_specification/
////https://ayushsarda.medium.com/joins-in-specification-jpa-60ee78be901a
////    https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/
//public class RecipeSpecifications {
//    public static Specification<Recipe> hasTitleLike(String title) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.like(root.get("title"), "%" + title + "%");
//    }
////    public Predicate toPredicate(Root<Recipe> restaurant,
////                                 CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
////        final var averageRating = criteriaBuilder.avg(restaurant.join("Restaurant_.RATINGS").get("Rating_.SCORE"));
////        final var topRatedPredicate = criteriaBuilder.greaterThanOrEqualTo(averageRating, 3);
////        query.groupBy(restaurant.get("Restaurant_.ID")).having(topRatedPredicate);
////
////        return null;
////    }
//
//
//    public static Specification<Recipe> hasInstructionLike(String instruction) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.like(root.get("instruction"), "%" + instruction + "%");
//    }
//
//    public static Specification<Recipe> hasServeEqual(Integer serve) {
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("serve"), serve);
//    }
//
//    //    public static Specification<Recipe> joinTest(SomeUser input) {
////        return new Specification<Recipe>() {
////            public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
////                Join<Recipe,SomeUser> userProd = root.join("owner");
////                Join<FollowingRelationship,Recipe> prodRelation = userProd.join("ownedRelationships");
////                return cb.equal(prodRelation.get("follower"), input);
////            }
////        };
////    }
////    public static Specification<Recipe> isLongTermCustomer() {
////        return new Specification<Recipe>{
////            public Predicate toPredicate (Root < T > root, CriteriaQuery query, CriteriaBuilder cb){
////                return cb.lessThan(root.get("Customer_.createdAt"), new LocalDate.minusYears(2));
////            }
////        }
////    }
//
//
////    public static Specification<Recipe> customerHasBirthday() {
////        return new Specification<Recipe> {
////            public Predicate toPredicate(Root<Recipe> root, CriteriaQuery query, CriteriaBuilder cb) {
////                return cb.equal(root.get("birthday"), "today");
////            }
////        };
////    }
////
////    public static Specification<Recipe> isLongTermCustomer() {
////        return new Specification<Recipe> {
////            public Predicate toPredicate(Root<T> root, CriteriaQuery query, CriteriaBuilder cb) {
////                return cb.lessThan(root.get(Customer_.createdAt), new LocalDate.minusYears(2));
////            }
////        };
////    }
//
//
//}
