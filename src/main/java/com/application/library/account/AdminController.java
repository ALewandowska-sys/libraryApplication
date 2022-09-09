package com.application.library.account;

import com.application.library.catalog.BookModel;
import com.application.library.catalog.BookService;
import com.application.library.generatorPDF.PDFService;
import com.application.library.history.HistoryModel;
import com.application.library.history.HistoryRepo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/admin")
@AllArgsConstructor
public class AdminController {
    private final ReaderService readerService;
    private final HistoryRepo historyRepo;
    private final BookService bookService;
    private final PDFService pdfService;

    @ApiOperation(value = "Show history of borrows")
    @GetMapping("/history")
    public List<HistoryModel> getHistory(){
        return historyRepo.findAll();
    }

    @ApiOperation(value = "Show history of borrows at last 7 days")
    @GetMapping("/historyLast")
    public List<HistoryModel> getHistoryLast(){
        LocalDate date = LocalDate.now().minusDays(7);
        return historyRepo.findByCreatedAtGreaterThan(date.atStartOfDay());
    }

    @PostMapping
    public BookModel addBook(@RequestBody BookModel book) {
        return bookService.addBook(book);
    }

    @ApiOperation(value = "Change information of book")
    @PutMapping("/info/{id}")
    public BookModel replaceInfo(@RequestBody BookModel newBook, @PathVariable("id") Long id){
        return bookService.replaceInfo(newBook, id);
    }

    @DeleteMapping("/{id}")
    public void removeBook(@PathVariable("id") Long id){
        bookService.removeBook(id);
    }

    @ApiOperation(value = "Create zip attachment with latest changes")
    @GetMapping("/sendZip/{howMany}")
    public void sendZip(HttpServletResponse response, @PathVariable("howMany") int howMany)
            throws ExecutionException, InterruptedException, IOException {
        String info = "latest_changes";

        List<HistoryModel> history = getHistory();
        List<ReaderModel> readers = readerService.getReaders();
        List<BookModel> borrowBook = getBorrowBook();

        List<List<?>> listWithLists = List.of(history, readers, borrowBook);
        String[] filesName = {"history", "readers", "borrow"};

        pdfService.createZipWithPDFs(response, filesName, listWithLists, howMany, info);
    }

    @ApiOperation(value = "Show all borrowed books for every readers")
    @GetMapping("/borrows")
    public List<BookModel> getBorrowBook() {
        return bookService.getBorrowBooks();
    }

}
