package com.application.library.vaadin;

import com.application.library.catalog.BookService;
import com.application.library.catalog.BookView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("filter_book")
public class CatalogView extends VerticalLayout {
    Grid<BookView> grid;
    private final BookService service;
    TextField titleText;
    Button filterButton;

    public CatalogView(BookService service){
        this.service = service;
        grid = new Grid<>();
        filterButton = new Button("Filter");
        titleText = new TextField("Filter by title");

        initGrid();

        filterButton.addClickListener(buttonClickEvent
                -> grid.setItems(service.byTitle(titleText.getValue())));

        add(titleText);
        add(filterButton);
        add(grid);

    }

    private void initGrid(){
        grid.addColumn(BookView::getId).setHeader("Id");
        grid.addColumn(BookView::getTitle).setHeader("Title");
        grid.addColumn(BookView::getYear).setHeader("Year");
        grid.addColumn(BookView::getAvailable).setHeader("Available");
        grid.setItems(service.getBooksS());
    }
}