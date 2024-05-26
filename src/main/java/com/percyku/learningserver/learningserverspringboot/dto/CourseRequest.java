package com.percyku.learningserver.learningserverspringboot.dto;


import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

public class CourseRequest {
    @NotBlank(message = "title cannot empty")
    @Size(max=128,message = "your description cannot exceed 128")
    private String title;

    @NotNull
    @Size(max=256,message = "your description cannot exceed 256")
    private String description;

    @NotNull
    @Range(min=1,max=10000,message = "your price range between 1 and 10000")
    private Integer price;



    public CourseRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }




}
