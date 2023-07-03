package com.example.libraryJSP.entities;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotBlank(message = "Title is required")
    private String title;
    @Column(name = "author")
    @NotBlank(message = "Author is required")
    private String author ;
    @Column(name = "category")

    private String category;
    @Column(name = "description")

    private String description;
    @Column(name = "photo")

    private String photo;
    @Column(name = "pagenumber")

    private int pagenumber;
    @Column(name = "datepublic")
    @NotNull(message = "Publish date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datepublic;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private double price;
//    @Column(name = "idPhoto")
//    private int idPhoto;
    public Book() {
    }

    public Book(int id, String title, String author, String category, String description, String photo, int pagenumber, Date datepublic,int quantity,double price) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.photo = photo;
        this.pagenumber = pagenumber;
        this.datepublic = datepublic;
        this.quantity=quantity;
        this.price=price;
//        this.idPhoto=idPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }

    public Date getDatepublic() {
        return datepublic;
    }

    public void setDatepublic(Date datepublic) {
        this.datepublic = datepublic;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

//	public int getIdPhoto() {
//		return idPhoto;
//	}
//
//	public void setIdPhoto(int idPhoto) {
//		this.idPhoto = idPhoto;
//	}
//    
}
