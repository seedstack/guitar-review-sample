package org.seedstack.samples.guitar.domain.model.category;

import org.seedstack.business.domain.Repository;

import java.util.Optional;
import java.util.stream.Stream;

public interface CategoryRepository extends Repository<Category, String> {
    /**
     * Retrieves all categories
     * @return @{@link Stream} of @{@link Category}
     */
    Stream<Category> all();

    /**
     * Retrieves a single category identified by its tag
     * @param tag The category tag
     * @return @{@link Optional} of @{@link Category}
     */
    Optional<Category> single(String tag);

    /**
     * Creates a Category and adds it to the repository
     * @param category The category to add
     * @return @{@link Optional} of @{@link Category} : The created category
     */
    Optional<Category> create(Category category);

    /**
     * Deletes a category
     * @param catId The category identifier
     */
    void delete(String catId);

    /**
     * Updates a category
     * @param updated the updated category
     * @return @{@link Optional} of @{@link Category} : The updated category
     */
    Optional<Category> updateCategory(Category updated);
}
