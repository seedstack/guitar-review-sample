package org.seedstack.samples.guitar.infrastructure.jpa;

import org.seedstack.business.specification.Specification;
import org.seedstack.jpa.BaseJpaRepository;
import org.seedstack.samples.guitar.domain.model.category.Category;
import org.seedstack.samples.guitar.domain.model.category.CategoryRepository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * JPA Implementation for CategoryRepository
 */
public class CategoryJpaRepository extends BaseJpaRepository<Category, String> implements CategoryRepository {
    @Override
    public Stream<Category> all() {
        Specification<Category> allSpec = getSpecificationBuilder()
                .of(Category.class)
                .all()
                .build();
        return get(allSpec);
    }

    @Override
    public Optional<Category> single(String tag) {
        return get(tag);
    }

    @Override
    public void delete(String catId) {
        Optional<Category> optCat = single(catId);
        if(optCat.isPresent()){
            remove(catId);
        }
    }

    @Override
    public Optional<Category> updateCategory(Category updated) {
        Optional<Category> optCat = single(updated.getId());
        if(optCat.isPresent()){
            return Optional.of(update(updated));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> create(Category category) {
        Optional<Category> optCat = single(category.getId());
        if(optCat.isPresent()){
            throw new IllegalArgumentException("Category "+category.getId()+" already exists");
        }
        add(category);
        return single(category.getId());
    }
}
