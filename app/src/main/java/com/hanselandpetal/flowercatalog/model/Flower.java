package com.hanselandpetal.flowercatalog.model;

import android.graphics.Bitmap;

/**
 * Created by Teste2 on 20/01/2017.
 */

public class Flower  {

    //Para o retrofit, os nomes dos campos devem ser iguais aos das propriedades do json, ou devemos anota-los

    private int productId;
    private String category;
    private String name;
    private String photo;
    private String instructions;
    private Double price;
    private Bitmap  bitmap;





    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
