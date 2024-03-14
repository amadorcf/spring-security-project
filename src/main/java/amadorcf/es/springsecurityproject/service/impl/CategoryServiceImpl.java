package amadorcf.es.springsecurityproject.service.impl;

import amadorcf.es.springsecurityproject.dto.SaveCategory;
import amadorcf.es.springsecurityproject.exception.ObjectNotFoundException;
import amadorcf.es.springsecurityproject.persistance.entity.Category;
import amadorcf.es.springsecurityproject.persistance.repository.CategoryRepository;
import amadorcf.es.springsecurityproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> findOneById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category createOne(SaveCategory saveCategory) {

        Category category = new Category();
        category.setName(saveCategory.getName());
        category.setStatus(Category.CategoryStatus.ENABLED);

        return categoryRepository.save(category);

    }

    @Override
    public Category updateOneById(Long categoryId, SaveCategory saveCategory) {

        Category categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id:"+ categoryId));
        categoryFromDB.setName(saveCategory.getName());


        return categoryRepository.save(categoryFromDB);

    }

    @Override
    public Category disabledOneById(Long categoryId) {

        Category categoryFromDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("Category not found with id:"+ categoryId));
        categoryFromDB.setStatus(Category.CategoryStatus.DISABLED);

        return categoryRepository.save(categoryFromDB);

    }
}
