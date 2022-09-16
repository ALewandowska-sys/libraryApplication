package com.application.library.generatorPDF;

import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PDFService {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
    private final String headerKey = "Content-Disposition";

    public HttpServletResponse createHeaderPdf(HttpServletResponse response, String info){
        response.setContentType("application/pdf");
        String currentDateTime = dateFormat.format(new Date());
        String headerValue = "attachment; filename=pdf_" + info + "_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        return response;
    }

    public void createZipWithPDFs(HttpServletResponse response, String[] filesName, List<List<?>> listWithLists, int howMany, String info)
            throws IOException, InterruptedException, ExecutionException {

        HttpServletResponse finalResponse = createHeaderZip(response, info);
        ServletOutputStream out = finalResponse.getOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(out);

        zipBody(zipOut, filesName, listWithLists, howMany).close();
        out.close();
    }
    public ByteArrayOutputStream createZipBytes(String[] filesName, List<List<?>> listWithLists, int howMany) throws ExecutionException, InterruptedException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);

        zipBody(zipOut, filesName, listWithLists, howMany).close();
        return byteArrayOutputStream;
    }

    private ZipOutputStream zipBody(ZipOutputStream zipOut, String[] filesName, List<List<?>> listWithLists, int howMany) throws ExecutionException, InterruptedException, IOException {
        byte[][] byteName = createPDFsAsync(filesName, listWithLists, howMany);

        for(int i = 0; i < listWithLists.size() ; i++) {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteName[i]);
            ZipEntry zip = new ZipEntry(filesName[i] + ".pdf");
            zipOut.putNextEntry(zip);
            zipOut.write(byteName[i]);
            zipOut.closeEntry();
            bis.close();
        }
        return zipOut;
    }
    private HttpServletResponse createHeaderZip(HttpServletResponse response, String info){
        response.setContentType("application/zip");
        String currentDateTime = dateFormat.format(new Date());
        String headerValue = "attachment; filename=zip_" + info + "_" + currentDateTime + ".zip";
        response.setHeader(headerKey, headerValue);
        return response;
    }
    private byte[][] createPDFsAsync(String[] filesName, List<List<?>> listWithLists, int howMany)
            throws ExecutionException, InterruptedException {

        byte[][] bytesPDFs = new byte[listWithLists.size()][];
        List<CompletableFuture<byte[]>> completableFutures = new ArrayList<>();

        startAsyncGenerator(completableFutures, listWithLists, filesName, howMany, bytesPDFs);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures
                .toArray(new CompletableFuture[0]));
        allFutures.get();

        return bytesPDFs;
    }

    private void startAsyncGenerator(List<CompletableFuture<byte[]>> completableFutures, List<List<?>> listWithLists, String[] filesName, int howMany, byte[][] bytesPDFs) {
        GeneratorPDF generator = new GeneratorPDF();
        for(int i = 0; i < listWithLists.size(); i++){
            int finalI = i;
            CompletableFuture<byte[]> future = CompletableFuture.supplyAsync(() -> {
                byte[] bytes;
                try {
                    bytes = generator.generateByte(filesName[finalI], listWithLists.get(finalI), howMany).toByteArray();
                } catch (IOException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return bytes;
            }).thenApply(bytes -> bytesPDFs[finalI] = bytes);
            completableFutures.add(future);
        }
    }
}
