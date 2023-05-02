package bll;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.*;
import java.io.IOException;

public class LogicManager {

    private static String title = "Project Documentation report";
    private static String description = "The project for the installation of the Microsoft Whiteboard has been completed. The Whiteboard was mounted and connected to the cables and power sources. Extensive testing was carried out to check all the functions were working properly.";
    private static String projectName = "Microsoft Whiteboard Implementation for Remote Collaboration";
    private static String taskName = "Task 1";

    public static void main(String[] args) throws IOException {
        exportDocumentation(title, description, projectName, taskName);
    }

    /**
     * method to export a pdf with text and images to create the documentation report**/
    public static void exportDocumentation(String title, String description, String projectName, String taskName) throws IOException {

        PDDocument document = new PDDocument(); //create pdf document
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        PDFont pdfFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        contentStream.setFont(pdfFont, 14);
        contentStream.setNonStrokingColor(Color.black);

        PDRectangle pageSize = page.getMediaBox();
        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();

        float margin = 20;
        float x = margin;
        float y = pageHeight - margin;

        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(title);
        contentStream.endText();

        y = y - 30;
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Project: " + projectName);
        contentStream.endText();

        y = y - 30;
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Task: " + taskName);
        contentStream.endText();


        contentStream.close();

        String exportFile = "Doc " + projectName + ".pdf";
        document.save(exportFile);
        document.close();
    }
}
