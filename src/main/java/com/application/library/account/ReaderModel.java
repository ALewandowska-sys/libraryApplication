package com.application.library.account;

import com.application.library.catalog.BookModel;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@ApiModel
@Entity(name = "reader")
@Table(name = "reader")
@Data
public class ReaderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String email;

    @OneToMany(targetEntity = BookModel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "who_borrow", referencedColumnName = "id")
    private List<BookModel> borrow;
}
