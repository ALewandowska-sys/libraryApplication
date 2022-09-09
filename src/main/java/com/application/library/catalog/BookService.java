package com.application.library.catalog;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookService{
    @Autowired
    private BookRepo repo;

    public List<BookView> getBooksS() {
        List<BookView> available = repo.findByAvailable(true, BookView.class);
        available.sort(Comparator.comparing(BookView::getTitle));
        //repo.findAll(Sort.by("title"))
        return available;
    }
    public BookModel byId(Long id){
        return repo.findFirstById(id);
    }
    public List<BookView> byTitle(String title){
        return repo.findByTitle(title);
    }

    public BookModel addBook(BookModel book) {
        return repo.save(book);
    }

    public List<BookModel> getBorrowBooks(){
        return repo.findByAvailable(false, BookModel.class);
    }

    public BookModel replaceInfo(BookModel newBook, Long id){
        return repo.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setYear(newBook.getYear());
                    return repo.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return repo.save(newBook);
                });
    }
    public void removeBook(Long id){
        repo.deleteById(id);
    }
}
