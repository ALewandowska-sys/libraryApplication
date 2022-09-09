package com.application.library.catalog;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Data
@ApiModel
@Entity(name = "book")
@Table(name = "book")
public class BookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int year;
    private boolean available = true;
    @Column(name = "who_borrow")
    private Integer whoBorrow;


    public void changeAvailable(){
        this.available = !available;
    }

}
