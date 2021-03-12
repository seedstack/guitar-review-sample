package org.seedstack.samples.angular.domain.services.category;

import org.seedstack.business.domain.Repository;
import org.seedstack.business.specification.Specification;
import org.seedstack.business.util.inmemory.InMemory;
import org.seedstack.jpa.Jpa;
import org.seedstack.samples.angular.domain.model.category.Category;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementation for CategoryService
 */
public class CategoryServiceImpl implements CategoryService{

    @Inject
    @Jpa
    private Repository<Category,String> categoryRepository;

    @Override
    public Stream<Category> all() {
        Specification<Category> allSpec = categoryRepository.getSpecificationBuilder()
                .of(Category.class)
                .all()
                .build();
        return categoryRepository.get(allSpec);
    }

    @Override
    public Optional<Category> single(String tag) {
        return categoryRepository.get(tag);
    }

    @Override
    public void delete(String catId) {
        Optional<Category> optCat = single(catId);
        if(optCat.isPresent()){
            categoryRepository.remove(catId);
        }
    }

    @Override
    public Optional<Category> updateCategory(Category updated) {
        Optional<Category> optCat = single(updated.getId());
        if(optCat.isPresent()){
            return Optional.of(categoryRepository.update(updated));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> create(Category category) {
        Optional<Category> optCat = single(category.getId());
        if(optCat.isPresent()){
            throw new IllegalArgumentException("Category "+category.getId()+" already exists");
        }
        categoryRepository.add(category);
        return single(category.getId());
    }
}
