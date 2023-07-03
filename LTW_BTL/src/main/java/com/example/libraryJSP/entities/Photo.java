package com.example.libraryJSP.entities;
import javax.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    
}
