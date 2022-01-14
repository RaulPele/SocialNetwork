package com.pelr.socialnetwork_extins.utils;
import com.pelr.socialnetwork_extins.MainApplication;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class PDFWriter {
    String fileName;

    public PDFWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeFile(List<String> content) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        document.addPage(page);

        PDRectangle mediaBox = page.getMediaBox();
        float margin = 25;
        float width = mediaBox.getWidth() - 2*margin;
        float startX = mediaBox.getLowerLeftX() + margin;
        float startY = mediaBox.getUpperRightY() - margin;

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.setLeading(14.5f);

            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);

            for(String line : content) {
                contentStream.showText(line);
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();

            document.save("src/main/resources/com/pelr/socialnetwork_extins/reports/" + fileName + ".pdf");

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
