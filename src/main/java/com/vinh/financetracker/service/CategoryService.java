package com.vinh.financetracker.service;

import com.vinh.financetracker.domain.entity.Category;
import com.vinh.financetracker.domain.entity.User;
import com.vinh.financetracker.domain.repository.CategoryRepository;
import com.vinh.financetracker.domain.repository.UserRepository;
import com.vinh.financetracker.dto.request.CategoryRequest;
import com.vinh.financetracker.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser(){
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public CategoryResponse createCategory(CategoryRequest request){
        User currenUser = getCurrentUser();
        if(categoryRepository.existsByUserIdAndName(currenUser.getId(), request.name())){
            throw new IllegalArgumentException("Category name already exists");
        }

        Category category = new Category();
        category.setName(request.name());
        category.setType(request.type());
        category.setIcon(request.icon());
        category.setColor(request.color());
        category.setUser(currenUser);

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByUserId(getCurrentUser().getId())
                .stream().map(this::mapToResponse).toList();
    }

    private CategoryResponse mapToResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getType(), c.getIcon(), c.getColor());
    }

    public CategoryResponse getCategoryById(UUID id) {
        User currentUser = getCurrentUser();
        return categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void deleteCategory(UUID id){
        User currentUser = getCurrentUser();
        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    public CategoryResponse updateCategory(UUID id, CategoryRequest request){
        User currentUser = getCurrentUser();
        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if(!category.getName().equals(request.name())
                && categoryRepository.existsByUserIdAndName(currentUser.getId(), request.name())){
            throw new IllegalArgumentException("Category name already exists");
        }
        category.setName(request.name());
        category.setType(request.type());
        category.setIcon(request.icon());
        category.setColor(request.color());
        return mapToResponse(categoryRepository.save(category));
    }
}
