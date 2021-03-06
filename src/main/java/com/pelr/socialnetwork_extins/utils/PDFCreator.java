package com.pelr.socialnetwork_extins.utils;

import javafx.stage.DirectoryChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.List;


public class PDFCreator {
    String fileName;

    public PDFCreator() {

    }

    public PDDocument createFile(List<String> content) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        document.addPage(page);


        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.setLeading(14.5f);

            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);

            for(String line : content) {
                writeLineToFile(contentStream, line);
            }

            contentStream.endText();
            contentStream.close();

            //document.save("src/main/resources/com/pelr/socialnetwork_extins/reports/" + fileName + ".pdf");

            return document;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeLineToFile(PDPageContentStream contentStream, String line) throws IOException{
        int startIndex, endIndex;
        startIndex=0;
        endIndex=80;

        if(line.length() > 80) {
            while (line.length() > 80) {
                String subLine = line.substring(startIndex, endIndex);
                contentStream.showText(subLine);
                contentStream.newLine();
                line = line.substring(endIndex);
                System.out.println(line);
                endIndex = Math.min(80, line.length());
            }
            if(line.length()!=0) {
                contentStream.showText(line);
                contentStream.newLine();
            }
        }else {
            contentStream.showText(line);
            contentStream.newLine();
        }
    }
}
