package com.application.library.account;

import com.application.library.mail.MailService;
import com.application.library.catalog.BookModel;
import com.application.library.catalog.BookRepo;
import com.application.library.generatorPDF.PDFService;
import com.application.library.history.HistoryModel;
import com.application.library.history.HistoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.*;

@Service
@AllArgsConstructor
public class ReaderService {
    private final ReaderRepo repo;
    private final HistoryRepo historyRepo;
    private final BookRepo bookRepo;
    private final MailService mailSender;
    private final PDFService pdfService;

    public ReaderModel getReader(String readerName){
        return repo.findByName(readerName);
    }

    public void sendMail(String name) throws IOException, ExecutionException, InterruptedException, MessagingException {
        ReaderModel reader = repo.findByName(name);
        String informationToSend = reader.getEmail();
        String sendTo = reader.getEmail();
        String subjectMail = "Pdf from library";

        LocalDate date = LocalDate.now().minusDays(7);
        List<HistoryModel> history = historyRepo.findByCreatedAtGreaterThan(date.atStartOfDay());
        List<BookModel> borrowBook = getBookByWhoBorrow(reader.getName());

        List<List<?>> listWithLists = List.of(history, borrowBook);
        String[] filesName = {"history", "borrow"};
        byte[] attachmentBytes = pdfService.createZipBytes(filesName, listWithLists, 0).toByteArray();
        mailSender.sendMail(sendTo, subjectMail, informationToSend, attachmentBytes);
    }
    public List<BookModel> getBookByWhoBorrow(String name){
        return bookRepo.findByWhoBorrow(repo.findByName(name).getId());
    }
    public BookModel borrowBook(BookModel book, Authentication authentication, String forVaadin){
        int readerId;

        if(forVaadin.isEmpty()){
            if(Objects.equals(authentication.getName(), "admin")){
                readerId = 0;
            }else {
                readerId = repo.findByName(authentication.getName()).getId();
            }
        }else {
            readerId = getReader(forVaadin).getId();
        }

        bookRepo.findById(book.getId()).map(borrow -> {
            if(borrow.isAvailable()){
                borrow.changeAvailable();
                borrow.setWhoBorrow(readerId);

                HistoryModel history = new HistoryModel();
                history.setTitle(book.getTitle());
                history.setBookId(book.getId());
                history.setStatus("borrow");
                historyRepo.save(history);

                return bookRepo.save(borrow);
            }
            else{
                System.out.println("You try to borrow a not available book");
                return borrow;
            }
        }).orElseThrow();
        return book;
    }

    public void returnBook(Long id){
        HistoryModel inDB = historyRepo.findFirstByBookIdAndStatus(id, "borrow");
        if(inDB.getCreatedAt().getMonthValue() - MonthDay.now().getMonthValue() > 1 && inDB.getCreatedAt().getDayOfMonth() - MonthDay.now().getDayOfMonth() < 0){
            inDB.setStatus("return after the deadline");
        }else {
            inDB.setStatus("on time");
        }
        historyRepo.save(inDB);

        bookRepo.findById(id).map(borrow -> {
            borrow.changeAvailable();
            borrow.setWhoBorrow(null);
            return bookRepo.save(borrow);
        });
    }

    public List<ReaderModel> getReaders() {
        return repo.findAll();
    }
    public ReaderModel addAccount(ReaderModel reader) throws IllegalStateException {
        String regexPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*"
                + "@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if(Pattern.compile(regexPattern).matcher(reader.getEmail()).matches()){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            reader.setPassword(encoder.encode(reader.getPassword()));
            return repo.save(reader);
        }
        else{
            throw new IllegalStateException("Wrong email");
        }
    }
    public void removeAccount(int id){
        repo.deleteById(id);
    }
}
