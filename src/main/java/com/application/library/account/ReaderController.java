package com.application.library.account;

import com.application.library.catalog.BookModel;
import com.application.library.history.HistoryModel;
import com.application.library.history.HistoryRepo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class ReaderController {
    private final ReaderService readerService;
    private final HistoryRepo historyRepo;

    @ApiOperation(value = "Show your history of borrow")
    @GetMapping("/history")
    public List<HistoryModel> getHistory(Authentication authentication){
        return historyRepo.findByCreatedBy(authentication.getName());
    }

    @ApiOperation(value = "Send email with information about reader")
    @GetMapping("/sendMail")
    public String sendMail(Authentication authentication){
        try{
            readerService.sendMail(authentication.getName());
            return "We send mail for you";
        }catch (MessagingException | IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException("Error, we can't send a mail");
        }
    }

    @ApiOperation(value = "Show your borrowed books")
    @GetMapping
    public List<BookModel> getBorrowBookById(Authentication authentication) {
        return readerService.getBookByWhoBorrow(authentication.getName());
    }

    @ApiOperation(value = "Borrow book")
    @PostMapping
    public BookModel addBook(@RequestBody BookModel book, Authentication authentication) {
        return readerService.borrowBook(book, authentication, "");
    }

    @ApiOperation(value = "Return book")
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id){
        readerService.returnBook(id);
    }

    @ApiOperation(value = "Show readers")
    @GetMapping("/accountsShow")
    public List<ReaderModel> getReader() {
        return readerService.getReaders();
    }

    @ApiOperation(value = "Add new account")
    @PostMapping("/accountAdd")
    public ReaderModel addAccount(@RequestBody ReaderModel reader) throws IllegalStateException {
        return readerService.addAccount(reader);
    }

    @ApiOperation(value = "Delete account")
    @DeleteMapping("/accountDelete/{id}")
    public void removeAccount(@PathVariable int id){
        readerService.removeAccount(id);
    }

}
