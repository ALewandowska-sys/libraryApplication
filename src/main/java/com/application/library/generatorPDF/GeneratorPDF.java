package com.application.library.generatorPDF;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class GeneratorPDF{
    private final Font font;
    public GeneratorPDF(){
        font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    }

    private Paragraph createTitle(String info){
        font.setSize(25);
        Paragraph title = new Paragraph("Information about " + info, font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        return title;
    }
    private Paragraph createInfoAboutEmptyList(){
        font.setSize(35);
        return new Paragraph("Nothing to show", font);
    }
    private PdfPTable table(List<?> objects, int limit) throws IllegalAccessException {

        Field[] fields = objects.get(0).getClass().getDeclaredFields();

        PdfPTable table = new PdfPTable(fields.length);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.GRAY);
        cell.setPadding(5);

        font.setSize(10);
        for (Field field : fields) {
            cell.setPhrase(new Phrase(field.getName(), font));
            table.addCell(cell);
        }

        cell.setBackgroundColor(CMYKColor.LIGHT_GRAY);
        int counter = 0;
        for (Object object : objects) {
            counter ++;
            if(counter == limit){
                break;
            }
            for (Field field : fields) {
                field.setAccessible(true);
                cell.setPhrase(new Phrase(String.valueOf(field.get(object)), font));
                table.addCell(cell);
            }
        }
        return table;
    }

    private void generator(Document document, String info, int limit, List<?> objects)
            throws IllegalAccessException {
        document.open();

        document.add(createTitle(info));
        document.add( Chunk.NEWLINE );

        if(objects.isEmpty()){
            document.add(createInfoAboutEmptyList());
        }
        else{
            document.add(table(objects, limit));
            document.add( Chunk.NEWLINE );
        }

        font.setSize(10);
        Paragraph footer = new Paragraph("Thanks for using our e-library!", font);
        document.add(footer);

        document.close();
    }

    public void generatePDF(HttpServletResponse response, String info, List<?> objects)
            throws DocumentException, IOException, IllegalAccessException {
        Document document = new Document(PageSize.A4);
        int limit = 0;

        PdfWriter.getInstance(document, response.getOutputStream());

        generator(document, info, limit, objects);
    }

    public ByteArrayOutputStream generateByte(String info, List<?> objects, int limit)
            throws DocumentException, IOException, IllegalAccessException {
        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        generator(document, info, limit, objects);

        return byteArrayOutputStream;
    }
}
