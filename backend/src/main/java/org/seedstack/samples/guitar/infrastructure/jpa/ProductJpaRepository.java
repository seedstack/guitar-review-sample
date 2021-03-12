package org.seedstack.samples.guitar.infrastructure.jpa;

import org.seedstack.business.specification.Specification;
import org.seedstack.business.specification.dsl.SpecificationBuilder;
import org.seedstack.jpa.BaseJpaRepository;
import org.seedstack.samples.guitar.domain.model.category.Category;
import org.seedstack.samples.guitar.domain.model.category.CategoryRepository;
import org.seedstack.samples.guitar.domain.model.product.Product;
import org.seedstack.samples.guitar.domain.model.product.ProductRepository;
import org.seedstack.samples.guitar.interfaces.products.ProductRepresentation;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class ProductJpaRepository extends BaseJpaRepository<Product, String> implements ProductRepository {
    private static final String CONTAINS_TERM_PATTERN="*%s*";

    @Inject
    private CategoryRepository categoryRepository;
    @Inject
    private SpecificationBuilder specificationBuilder;

    @Override
    public Stream<Product> all() {
        return get(buildAllSpecification());
    }

    @Override
    public Optional<Product> single(String id) {
        return get(id);
    }

    @Override
    public Stream<Product> search(String term) {
        Specification<Product> termMatch= specificationBuilder
                .of(Product.class)
                .property("title")
                .matching(String.format(CONTAINS_TERM_PATTERN, term))
                .ignoringCase()
                .build();

        return get(termMatch);
    }

    @Override
    public Stream<Product> searchByCategory(String tag) {
        Specification<Product> categoryMath = specificationBuilder
                .of(Product.class)
                .property("categories.tag")
                .equalTo(tag)
                .build();
        return get(categoryMath);
    }

    private  Specification<Category> buildCategorySpecification(String tag){
        return specificationBuilder.of(Category.class)
                .property("tag")
                .equalTo(tag).ignoringCase()
                .build();
    }
    private Specification<Product> buildAllSpecification(){
        return specificationBuilder.of(Product.class).all().build();
    }

    @Override
    public Optional<Product> addCategory(String productId, String catId) {
        Optional<Category> cat= categoryRepository.single(catId);
        if(!cat.isPresent()){
            throw new IllegalArgumentException("Can't add an invalid category");
        }
        Optional<Product> product =single(productId);
        if(!product.isPresent()){
            throw new IllegalArgumentException("Can't add category to an invalid product");
        }
        product.get().addCategory(cat.get());
        return Optional.of(update(product.get()));
    }

    @Override
    public Optional<Product> removeCategory(String productId, String catId) {
        Optional<Category> cat= categoryRepository.single(catId);
        if(!cat.isPresent()){
            throw new IllegalArgumentException("Can't remove an invalid category");
        }
        Optional<Product> product =single(productId);
        if(!product.isPresent()){
            throw new IllegalArgumentException("Can't remove category to an invalid product");
        }
        product.get().removeCategory(cat.get());
        return Optional.of(update(product.get()));
    }

    @Override
    public Optional<Product> create(ProductRepresentation newProduct) {
        Product prod = new Product(UUID.randomUUID().toString());
        prod.changeInformations(newProduct.getTitle(), newProduct.getDescription());
        prod.setPrice(newProduct.getPrice());
        for(String cat : newProduct.getCategories()){
            Optional<Category> category = categoryRepository.single(cat);
            if(category.isPresent()){
                prod.addCategory(category.get());
            }
            else{
                throw new IllegalArgumentException("Category "+cat+" could not be found.");
            }
        }
        add(prod);
        return single(prod.getId());
    }

    @Override
    public Optional<Product> update(ProductRepresentation updatedProduct) {
        Optional<Product> existingProduct= single(updatedProduct.getId());
        if(!existingProduct.isPresent()){
            return Optional.empty();
        }
        existingProduct.get().changeInformations(updatedProduct.getTitle(), updatedProduct.getDescription());
        existingProduct.get().setPrice(updatedProduct.getPrice());
        existingProduct.get().getCategories().clear();
        for(String cat : updatedProduct.getCategories()){
            Optional<Category> category = categoryRepository.single(cat);
            if(category.isPresent()){
                existingProduct.get().addCategory(category.get());
            }
            else{
                throw new IllegalArgumentException("Category "+cat+" could not be found.");
            }
        }
        update(existingProduct.get());
        return single(updatedProduct.getId());
    }

    @Override
    public void delete(String id) {
        Optional<Product> prodOpt= single(id);
        if(!prodOpt.isPresent()){
            throw new IllegalArgumentException("Product with id "+id+" not found");
        }
        remove(prodOpt.get());
    }
}
