package com.application.library.catalog;

import com.application.library.generatorPDF.GeneratorPDF;
import com.application.library.generatorPDF.PDFService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/catalog")
@AllArgsConstructor
public class BookController {
    private final BookService service;
    private final PDFService pdfService;

    @ApiOperation(value = "Find by title")
    @GetMapping("/showAll/{title}")
    public List<BookView> getBooks(@PathVariable String title) {
        return service.byTitle(title);
    }

    @ApiOperation(value = "Generate pdf with available books")
    @GetMapping("/pdf")
    public void generatePdf(HttpServletResponse response) throws IOException, IllegalAccessException {
        String info = "AvailableBooks";
        List<BookView> listOfObject = service.getBooksS();
        response = pdfService.createHeaderPdf(response, info);

        GeneratorPDF generator = new GeneratorPDF();
        generator.generatePDF(response, info, listOfObject);
    }

    @GetMapping
    public List<BookView> showS(){
        return service.getBooksS();
    }
}