package com.asif.manageurexpense.service;

import com.asif.manageurexpense.dto.CategoryDto;
import com.asif.manageurexpense.entity.CategoryEntity;
import com.asif.manageurexpense.entity.ProfileEntity;
import com.asif.manageurexpense.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    // save category
    public CategoryDto saveCategory(CategoryDto categoryDto){
        ProfileEntity profile = profileService.getCurrentProfile();
        if (categoryRepository.existsByNameAndProfileId(categoryDto.getName(),profile.getId())){
            throw new RuntimeException("Category with this name is already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDto,profile);
        //newCategory = categoryRepository.save(newCategory);
        return toDto(categoryRepository.save(newCategory));
    }

     // get categories for current user
    public List<CategoryDto> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDto).toList();
    }

    // get categories by type for current user
    public List<CategoryDto> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return entities.stream().map(this::toDto).toList();
    }

     // update
//    public CategoryDto updateCategory(Long categoryId , CategoryDto dto){
//        ProfileEntity profile = profileService.getCurrentProfile();
//        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
//                .orElseThrow(()-> new RuntimeException("Category not found "));
//
//        existingCategory.setName(dto.getName());
//        existingCategory.setIcon(dto.getIcon());
//        return toDto(existingCategory);
//    }

    public CategoryDto updateCategory(Long categoryId, CategoryDto dto) {

        ProfileEntity profile = profileService.getCurrentProfile();

        CategoryEntity existingCategory =
                categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));

        existingCategory.setName(dto.getName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory.setType(dto.getType());

        CategoryEntity updatedCategory =
                categoryRepository.save(existingCategory);

        return toDto(updatedCategory);
    }

    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profile){
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profile)
                .type(categoryDto.getType())
                .build();
    }

    private CategoryDto toDto(CategoryEntity entity){
        return CategoryDto.builder()
                .id(entity.getId())
                .profileId(entity.getProfile() !=null ? entity.getProfile().getId() : null)
                .name(entity.getName())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .type(entity.getType())
                .build();
    }
}
