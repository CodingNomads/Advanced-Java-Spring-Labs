/* CodingNomads (C)2024 */
package com.codingnomads.springsecurity.recipeapi.security;

import com.codingnomads.springsecurity.recipeapi.models.Recipe;
import com.codingnomads.springsecurity.recipeapi.models.Review;
import com.codingnomads.springsecurity.recipeapi.models.securitymodels.CustomUserDetails;
import com.codingnomads.springsecurity.recipeapi.models.securitymodels.Role;
import com.codingnomads.springsecurity.recipeapi.repositories.RecipeRepo;
import com.codingnomads.springsecurity.recipeapi.repositories.ReviewRepo;
import jakarta.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    RecipeRepo recipeRepo;

    @Autowired
    ReviewRepo reviewRepo;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // this method will not be used. But if used by accident should always block access for good measure.
        return false;
    }

    @Override
    public boolean hasPermission(
            Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (!permission.getClass().equals(String.class)) {
            throw new SecurityException("Cannot execute hasPermission() calls where permission is not in String form");
        }

        // if the user is an admin they should be allowed to proceed
        if (userIsAdmin(authentication)) {
            return true;
        } else {
            // otherwise, the user must be the owner of the object to edit it.
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (targetType.equalsIgnoreCase("recipe")) {
                Optional<Recipe> recipe = recipeRepo.findById(Long.parseLong(targetId.toString()));
                if (recipe.isEmpty()) {
                    throw new EntityNotFoundException("The recipe you are trying to access does not exist");
                }

                // if the author of the entity matches the current user they are the owner of the recipe and should be
                // allowed access
                return recipe.get().getAuthor().equals(userDetails.getUsername());

            } else if (targetType.equalsIgnoreCase("review")) {
                Optional<Review> review = reviewRepo.findById(Long.parseLong(targetId.toString()));
                if (review.isEmpty()) {
                    throw new EntityNotFoundException("The review you are trying to access does not exist");
                }

                // if the author of the entity matches the current user they are the owner of the review and should be
                // allowed access
                return review.get().getAuthor().equals(userDetails.getUsername());
            }
        }

        return true;
    }

    public boolean userIsAdmin(Authentication authentication) {
        Collection<Role> grantedAuthorities = (Collection<Role>) authentication.getAuthorities();

        for (Role r : grantedAuthorities) {
            if (r.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
