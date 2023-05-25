package logic.documentPDF;


import be.Customer;
import be.Project;
import be.Task;
import javafx.embed.swing.SwingFXUtils;

import javafx.scene.image.Image;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;


public class PDFHandler {
    private static final Logger logger = LogManager.getLogger("debugLogger");

    /**
     * method to export a pdf with text and images to create the documentation report
     **/
    //
    public void exportReport(Customer customer, Project project, Task task, Image image1, Image image2, String deviceNames, String devicePasswords) {
        try {

            PDDocument document = Loader.loadPDF(new File(Objects.requireNonNull(this.getClass().getResource("/report/reportBackground-input.pdf")).toURI()));
            PDPage page = document.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            PDFont boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDFont regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            setUpContentStream(contentStream, page, regularFont);
            PDRectangle pageSize = page.getMediaBox();
            float pageHeight = pageSize.getHeight();

            final float MARGIN = 20;
            final float SPACING = 50;
            final float LINE_SPACING = 30;
            final int IMAGE_WIDTH = 200;
            final int IMAGE_HEIGHT = 166;

            float x = SPACING;
            float custY = pageHeight - 170;
            float projY = custY - LINE_SPACING * 2;
            float taskY = projY - SPACING * 3;
            float deviceY = projY - LINE_SPACING * 2;
            float imgY = 300 - IMAGE_HEIGHT - SPACING * 2;

            writeCustomerInfo(contentStream, customer, x, custY, LINE_SPACING, boldFont, regularFont);
            writeProjectInfo(contentStream, project, x, projY, LINE_SPACING, boldFont, regularFont);
            writeDeviceInfo(contentStream, x, deviceY, boldFont, regularFont, LINE_SPACING, deviceNames, devicePasswords);
            writeTaskInfo(contentStream, task, boldFont, regularFont, 12, page.getMediaBox().getWidth() - MARGIN * 4, x, taskY, LINE_SPACING);
            insertImage(contentStream, document, image1, x, imgY, SPACING, IMAGE_WIDTH, IMAGE_HEIGHT);
            float x2 = x + IMAGE_WIDTH + SPACING;
            insertImage(contentStream, document, image2, x2, imgY, SPACING, IMAGE_WIDTH, IMAGE_HEIGHT);

            contentStream.close();

            String folderPath = "src/main/resources/report/printedReports/";
            String exportFile = folderPath + "Report " + project.getProjName() + "-" + task.getTaskName() + ".pdf";
            document.save(exportFile);
            document.close();

        } catch (IOException | URISyntaxException e) {

            logger.error("There was an issue with the content stream in the pdf creation", e);

        }

    }

    /**
     * method to set the font and font color for  the content stream
     **/
    private void setUpContentStream(PDPageContentStream contentStream, PDPage page, PDFont regularFont) throws IOException {

        contentStream.setFont(regularFont, 13);
        float[] color = new float[]{0, 0, 0};
        contentStream.setNonStrokingColor(color[0], color[1], color[2]);
    }


    /**
     * method to write the device info on a pdf document
     */
    private void writeDeviceInfo(PDPageContentStream contentStream, float x, float deviceY, PDFont boldFont, PDFont regularFont, float lineSpacing, String deviceNames, String devicePasswords) throws IOException {

        if (deviceNames != null) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, deviceY);
            contentStream.setFont(boldFont, 13);
            contentStream.showText("Device name: ");
            contentStream.setFont(regularFont, 13);
            contentStream.showText(deviceNames);
            contentStream.endText();
        }
        if (devicePasswords != null) {
            deviceY -= lineSpacing;
            contentStream.beginText();
            contentStream.newLineAtOffset(x, deviceY);
            contentStream.setFont(boldFont, 13);
            contentStream.showText("Login credential: ");
            contentStream.setFont(regularFont, 13);
            contentStream.showText(devicePasswords);
            contentStream.endText();
        }

    }

    /**
     * method to write the customer info on a pdf document
     */
    private void writeCustomerInfo(PDPageContentStream contentStream, Customer customer, float x, float custY, float linespacing, PDFont bold, PDFont regular) throws IOException {
        contentStream.beginText();
        contentStream.setFont(bold, 12);
        contentStream.newLineAtOffset(x, custY);
        contentStream.showText("Customer: ");
        contentStream.setFont(regular, 12);
        contentStream.showText(customer.getCustName());
        contentStream.newLineAtOffset(0, -linespacing);
        contentStream.setFont(bold, 12);
        contentStream.showText("Address: ");
        contentStream.setFont(regular, 12);
        contentStream.showText(customer.getCustAddress() + ", " + customer.getPostalCode() + " " + customer.getCity());
        contentStream.endText();
    }


    /**
     * method to insert an image in a pdf
     */
    private void insertImage(PDPageContentStream contentStream, PDDocument document, Image image, float x, float y, float spacing, float imageWidth, float imageHeight) throws IOException {
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        if (image != null) {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageStream);
            PDImageXObject pdImageXObjectImg1 = PDImageXObject.createFromByteArray(document, imageStream.toByteArray(), "");
            contentStream.drawImage(pdImageXObjectImg1, x, y, imageWidth, imageHeight);
        }
    }

    /**
     * method to write the task name and description on a pdf document
     */
    private void writeTaskInfo(PDPageContentStream contentStream, Task task, PDFont boldFont, PDFont regularFont, int fontSize, float maxLineWidth, float x, float taskY, float lineSpacing) throws IOException {

        contentStream.beginText();
        contentStream.newLineAtOffset(x, taskY);
        contentStream.setFont(boldFont, 15);
        contentStream.showText("Task: ");
        contentStream.setFont(regularFont, 15);
        contentStream.showText(task.getTaskName());
        contentStream.endText();

        taskY -= lineSpacing;

        contentStream.beginText();
        contentStream.newLineAtOffset(x, taskY);
        contentStream.setFont(boldFont, 12);
        contentStream.showText("Description: ");
        contentStream.endText();

        taskY -= lineSpacing;

        List<String> lines = wrapText(task.getTaskDesc(), regularFont, fontSize, maxLineWidth);
        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, taskY);
            contentStream.setFont(regularFont, 12);
            contentStream.showText(line);
            contentStream.endText();
            taskY -= 20;
        }
    }

    /**
     * method to write the project info on a pdf document
     */
    private void writeProjectInfo(PDPageContentStream contentStream, Project project, float x, float projY, float linespacing, PDFont bold, PDFont regular) throws IOException {
        contentStream.beginText();
        contentStream.setFont(bold, 12);
        contentStream.newLineAtOffset(x, projY);
        contentStream.showText("Project: ");
        contentStream.setFont(regular, 12);

        contentStream.showText(project.getProjName());
        contentStream.newLineAtOffset(0, -linespacing);
        contentStream.setFont(bold, 12);
        contentStream.showText("Date: ");
        contentStream.setFont(regular, 12);
        contentStream.showText(project.getProjDate().toString());
        contentStream.endText();
    }

    /**
     * method to wrap the text so that longer text are displayed properly on the pdf
     */
    private List<String> wrapText(String text, PDFont font, int fontSize, float width) throws IOException {
        List<String> result = new ArrayList<>();
        String[] split = text.split("(?<=\\W)");
        int[] possibleWrapPoints = new int[split.length];
        possibleWrapPoints[0] = split[0].length();
        for (int i = 1; i < split.length; i++) {
            possibleWrapPoints[i] = possibleWrapPoints[i - 1] + split[i].length();
        }
        int start = 0;
        int end = 0;
        for (int i : possibleWrapPoints) {
            float lineWidth = font.getStringWidth(text.substring(start, i)) / 1000 * fontSize;
            if (start < end && lineWidth > width) {
                result.add(text.substring(start, end));
                start = end;
            }
            end = i;
        }
        result.add(text.substring(start));
        return result;
    }


}




