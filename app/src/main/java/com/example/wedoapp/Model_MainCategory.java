package com.example.wedoapp;

public class Model_MainCategory {
    private int CategoryImage;
    private String CategoryName, CategoryDesc ;

    public Model_MainCategory() {
    }

    public Model_MainCategory(String CategoryName, String CategoryDesc, int CategoryImage) {
        this.CategoryName = CategoryName;
        this.CategoryDesc = CategoryDesc;
        this.CategoryImage = CategoryImage;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryDesc() {
        return CategoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        CategoryDesc = categoryDesc;
    }

    public int getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        CategoryImage = categoryImage;
    }
}
