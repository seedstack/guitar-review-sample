package org.seedstack.samples.guitar.domain.model.product;

import org.seedstack.business.domain.Repository;
import org.seedstack.samples.guitar.interfaces.products.ProductRepresentation;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProductRepository extends Repository<Product, String> {
    /**
     * Retrieves all products
     * @return Stream of @{@link Product}
     */
    Stream<Product> all();

    /**
     * Gets a single product identified by its ID
     * @param id The product ID
     * @return An Optional of @{@link Product}
     */
    Optional<Product> single(String id);

    /**
     * Search for product by terms matching product title
     * @param term The term to search in titles
     * @return Stream of @{@link Product}
     */
    Stream<Product> search(String term);

    /**
     * Searches products by their category
     * @param tag The category tag
     * @return @{@link Stream} of @{@link Product}
     */
    Stream<Product> searchByCategory(String tag);

    /**
     * Creates and persists new product
     * @param newProduct the new product representation
     * @return @{@link Optional} of {@link Product}
     */
    Optional<Product> create(ProductRepresentation newProduct);

    /**
     * Deletes a product identified by its Id
     * @param id the product ID
     */
    void delete(String id);

    /**
     * Updates an existent product
     * @param updatedProduct Updated product representation
     * @return @{@link Optional} of @{@link Product}
     */
    Optional<Product> update(ProductRepresentation updatedProduct);

    /**
     * Adds a category to a product identified by its ID
     * @param productId the product ID
     * @param catId the category id to add
     */
    Optional<Product> addCategory(String productId, String catId);

    /**
     * Un-assign a category from a product identified by its ID
     * @param productId the @{@link Product ID}
     * @param catId the Category ID
     * @return @{@link Optional} of @{@link Product}
     */
    Optional<Product> removeCategory(String productId, String catId);
}
