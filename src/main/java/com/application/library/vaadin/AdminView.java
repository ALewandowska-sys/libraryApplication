package com.application.library.vaadin;

import com.application.library.catalog.BookModel;
import com.application.library.catalog.BookService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route("add_new_book")
public class AdminView extends VerticalLayout {
    TextField titleText;
    TextField yearText;
    Button button;
    private final BookService service;

    public AdminView(BookService service) {
        this.service = service;
        titleText = new TextField("title");
        yearText = new TextField("year");
        button = new Button("Add new book");
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);

        button.addClickListener(e -> Notification.show(addNewBook()));

        layout.add(titleText);
        layout.add(yearText);
        layout.add(button);
        add(layout);
    }

    private String addNewBook(){
        BookModel book = new BookModel();
        book.setTitle(titleText.getValue());
        book.setYear(Integer.parseInt(yearText.getValue()));

        service.addBook(book);
        return "Book was added";
    }


}
