package com.example.review_material;

public class Picture {

    private final String name;

    private final int imageId;

    public Picture(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}
