package com.vinh.financetracker.controller;

import com.vinh.financetracker.common.ApiResponse;
import com.vinh.financetracker.dto.request.CategoryRequest;
import com.vinh.financetracker.dto.response.CategoryResponse;
import com.vinh.financetracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    // 3. Lấy chi tiết một danh mục theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable UUID id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 4. Xóa một danh mục
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable UUID id
            , @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}