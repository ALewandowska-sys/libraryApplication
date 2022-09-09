package com.application.library.vaadin;

import com.application.library.account.ReaderService;
import com.application.library.catalog.BookModel;
import com.application.library.catalog.BookService;
import com.application.library.catalog.BookView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

@Route("borrow_or_return_book")
@RolesAllowed("Reader")
public class ReaderView extends VerticalLayout {
    private final ReaderService service;
    private final BookService bookService;
    Button returnButton;
    Button borrowButton;
    TextField bookIdText;
    TextField readerNameText;
    Grid<BookView> gridBorrow;
    Grid<BookModel> gridReturn;
    Accordion accordionBorrow;
    Accordion accordionReturn;

    public ReaderView(ReaderService service, BookService bookService){
        this.service = service;
        this.bookService = bookService;

        initView();
        addView();
    }

    private void initView() {
        readerNameText = new TextField("Your nickname");
        bookIdText = new TextField("Book id");
        returnButton = new Button("Return");
        borrowButton = new Button("Borrow");
        gridBorrow = new Grid<>();
        gridReturn = new Grid<>();
        accordionReturn = new Accordion();
        accordionBorrow = new Accordion();
        accordionBorrow.setWidthFull();
        accordionReturn.setWidthFull();
        accordionReturn.add("Return book", new VerticalLayout(new Span("This book you can return"), gridReturn, returnButton));
        accordionBorrow.add("Borrow book", new VerticalLayout(new Span("Available book to borrow"), gridBorrow, borrowButton));

        initGridBorrow();
        initGridReturn();
    }
    private void initGridBorrow(){
        gridBorrow.addColumn(BookView::getId).setHeader("Id");
        gridBorrow.addColumn(BookView::getTitle).setHeader("Title");
        gridBorrow.addColumn(BookView::getYear).setHeader("Year");
        gridBorrow.setItems(bookService.getBooksS());
    }
    private void initGridReturn(){
        gridReturn.addColumn(BookModel::getId).setHeader("Id");
        gridReturn.addColumn(BookModel::getTitle).setHeader("Title");
        gridReturn.addColumn(BookModel::getYear).setHeader("Year");
    }
    private void borrowBook() {
        BookModel borrow = new BookModel();
        BookModel bookInfo = bookService.byId(Long.valueOf(bookIdText.getValue()));
        borrow.setWhoBorrow(service.getReader(readerNameText.getValue()).getId());
        borrow.setTitle(bookInfo.getTitle());
        borrow.setYear(bookInfo.getYear());
        borrow.setId(Long.valueOf(bookIdText.getValue()));
        service.borrowBook(borrow, null, readerNameText.getValue());
        Notification.show("You borrow " + borrow.getTitle() + "! Enjoy reading");
        gridReturn.setItems(service.getBookByWhoBorrow(readerNameText.getValue()));
    }

    private void addView() {
        add(readerNameText);
        add(bookIdText);

        borrowButton.addClickListener(ClickEvent -> borrowBook());
        returnButton.addClickListener(ClickEvent -> {
            service.returnBook(Long.valueOf(bookIdText.getValue()));
            Notification.show("You return book. Thanks!");
            gridReturn.setItems(service.getBookByWhoBorrow(readerNameText.getValue()));
        });
        readerNameText.addValueChangeListener(event -> addOption());
    }
    private void addOption() {
        add(accordionBorrow);
        add(accordionReturn);
        gridReturn.setItems(service.getBookByWhoBorrow(readerNameText.getValue()));
    }
}
