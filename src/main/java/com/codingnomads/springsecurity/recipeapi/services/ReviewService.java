/* CodingNomads (C)2024 */
package com.codingnomads.springsecurity.recipeapi.services;

import com.codingnomads.springsecurity.recipeapi.exceptions.NoSuchRecipeException;
import com.codingnomads.springsecurity.recipeapi.exceptions.NoSuchReviewException;
import com.codingnomads.springsecurity.recipeapi.models.Recipe;
import com.codingnomads.springsecurity.recipeapi.models.Review;
import com.codingnomads.springsecurity.recipeapi.repositories.ReviewRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    RecipeService recipeService;

    public List<Review> getAllReviews() throws NoSuchReviewException {
        List<Review> reviews = reviewRepo.findAll();

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("There are no reviews to be found");
        }

        return reviews;
    }

    public Review getReviewById(Long id) throws NoSuchReviewException {
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isEmpty()) {
            throw new NoSuchReviewException("The review with ID " + id + " could not be found");
        }

        return review.get();
    }

    public List<Review> getReviewByRecipeId(Long recipeId) throws NoSuchRecipeException, NoSuchReviewException {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        List<Review> reviews = recipe.getReviews().stream().toList();

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("There are no reviews for this recipe");
        }

        return reviews;
    }

    public List<Review> getReviewByUsername(String username) throws NoSuchReviewException {
        List<Review> reviews = reviewRepo.findByUser_Username(username);

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException("No reviews could be found for username " + username);
        }

        return reviews;
    }

    public Recipe postNewReview(Review review, Long recipeId) throws NoSuchRecipeException {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipe.getReviews().add(review);
        recipeService.updateRecipe(recipe, false);
        return recipe;
    }

    public Review deleteReviewById(Long id) throws NoSuchReviewException {
        Review review = getReviewById(id);

        if (null == review) {
            throw new NoSuchReviewException("The review you are trying to delete does not exist");
        }

        reviewRepo.deleteById(id);
        return review;
    }

    public Review updateReviewById(Review reviewToUpdate) throws NoSuchReviewException {
        try {
            Review review = getReviewById(reviewToUpdate.getId());
        } catch (NoSuchReviewException e) {
            throw new NoSuchReviewException(
                    "The review you are trying to update. Maybe you meant to create one? If not,"
                            + "Please double check the ID you passed in");
        }

        reviewRepo.save(reviewToUpdate);
        return reviewToUpdate;
    }
}
