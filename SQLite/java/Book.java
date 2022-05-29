package com.example.sqlite;

//Book对象
public class Book {

    private String author;
    private String name;
    private int pages;
    private double price;

    public Book(String author, String name , double price, int pages){
        this.author = author;
        this.name = name;
        this.pages = pages;
        this.price = price;
    }
    
    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public int getPages() {
        return pages;
    }

    public double getPrice() {
        return price;
    }
}
