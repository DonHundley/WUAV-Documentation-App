package logic.documentPDF;

import be.Customer;
import be.Project;
import be.Task;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class PDFHandler {


    private static String description = "After completing the installation of the Microsoft Whiteboard, we proceeded to configure the software and test its various features to ensure they were functioning as intended. The process involved checking the connectivity to the network and calibrating the display for optimal performance. Additionally, we ensured that the Whiteboard was compatible with all the necessary software applications and devices to enable seamless collaboration among team members. Finally, we provided comprehensive training on how to use the Whiteboard to maximize its potential. Overall, the project was a success, and we look forward to the benefits it will bring to our team.";
    private static String comment1 = "comment 1";
    private static String comment2 = "comment 2";


    public static void main(String[] args) throws IOException {
        Platform.startup(() -> {
            Image image1 = new Image("images/beforeImage1.jpg");
            Image image2 = new Image("images/afterImage1.jpg");
            Image layout = new Image("images/layout.png");
            Customer customer = new Customer("Vestas", "project@vestas.dk", "Siriusvej 6, Esbjerg");
            Task task = new Task(2, 1, "Whiteboard installation", layout, description, "in progress");
            Project project = new Project(1, "Implementing Microsoft Whiteboard for Collaboration", Date.valueOf("2023-08-12"), 3);
            try {
                exportDocumentation(customer, project, task, image1, image2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * method to export a pdf with text and images to create the documentation report
     **/
    //
    public static void exportDocumentation(Customer customer, Project project, Task task, Image image1, Image image2) throws IOException {

        PDDocument document = Loader.loadPDF(new File("src/main/java/resources/report-input.pdf"));
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
        PDFont pdfFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        contentStream.setFont(pdfFont, 11);
        float[] color = new float[]{0, 0, 0};
        contentStream.setNonStrokingColor(color[0], color[1], color[2]);

        PDRectangle pageSize = page.getMediaBox();
        float pageHeight = pageSize.getHeight();
        float pageWidth = pageSize.getWidth();
        float margin = 20;
        float spacing = 50;
        float lineSpacing = 30;
        float x = spacing;
        float custY = pageHeight - 200;

        float projY = custY;
        float projX = (pageWidth - 2 * margin) / 2;
        float taskY = projY - spacing * 4;
        float deviceY=projY-lineSpacing*3;

        int imageWidth = 200;
        int imageHeight = 166;
        float imgY=300-imageHeight-spacing*2;


        PDFont boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDFont regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        writeCustomerInfo(contentStream, customer, x, custY, lineSpacing);

        writeProjectInfo(contentStream, project, projX, projY, lineSpacing);
        writeDeviceInfo(contentStream, task, x, deviceY, boldFont,regularFont,lineSpacing);

        writeTaskInfo(contentStream, task, boldFont, regularFont, 12, page.getMediaBox().getWidth() - margin * 4, x, taskY, lineSpacing);








        insertImage(contentStream, document, image1, x, imgY, spacing, imageWidth, imageHeight);

        float x2 = x + imageWidth + spacing;

        insertImage(contentStream, document, image2, x2, imgY, spacing, imageWidth, imageHeight);

        contentStream.close();


        String exportFile = "Report " + project.getProjName() + ".pdf";
        document.save(exportFile);
        document.close();
    }

    private static void writeDeviceInfo(PDPageContentStream contentStream, Task task, float x, float deviceY, PDFont boldFont, PDFont regularFont, float lineSpacing) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, deviceY);
        contentStream.setFont(boldFont, 15);
        contentStream.showText("Device name: ");
        contentStream.setFont(regularFont, 15);
        //contentStream.showText(task.getDeviceName());
        contentStream.endText();

        deviceY -= lineSpacing;
        contentStream.beginText();
        contentStream.newLineAtOffset(x, deviceY);
        contentStream.setFont(boldFont, 15);
        contentStream.showText("Login credential: ");
        contentStream.setFont(regularFont, 15);
        //contentStream.showText(task.getLoginCred());
        contentStream.endText();

    }

    /**
     * method to write the customer info on a pdf document
     */
    private static void writeCustomerInfo(PDPageContentStream contentStream, Customer customer, float x, float custY, float linespacing) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.newLineAtOffset(x, custY);
        contentStream.showText("Customer: ");
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.showText(customer.getCustName());
        contentStream.newLineAtOffset(0, -linespacing);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.showText("Address: ");
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.showText(customer.getCustAddress());
        contentStream.endText();
    }


    /**
     * method to insert an image in a pdf
     */
    private static void insertImage(PDPageContentStream contentStream, PDDocument document, Image image, float x, float y, float spacing, float imageWidth, float imageHeight) throws IOException {
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "jpg", imageStream);
        PDImageXObject pdImageXObjectImg1 = PDImageXObject.createFromByteArray(document, imageStream.toByteArray(), "");
        contentStream.drawImage(pdImageXObjectImg1, x, y, imageWidth, imageHeight);

    }

    /**
     * method to write the task name and description on a pdf document
     */
    private static void writeTaskInfo(PDPageContentStream contentStream, Task task, PDFont boldFont, PDFont regularFont, int fontSize, float maxLineWidth, float x, float taskY, float lineSpacing) throws IOException {

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
    private static void writeProjectInfo(PDPageContentStream contentStream, Project project, float x, float projY, float linespacing) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.newLineAtOffset(x, projY);
        contentStream.showText("Project: ");
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);

        contentStream.showText(project.getProjName());
        contentStream.newLineAtOffset(0, -linespacing);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        contentStream.showText("Date: ");
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
        contentStream.showText(project.getProjDate().toString());
        contentStream.endText();
    }

    /**
     * method to wrap the text so that longer text are displayed properly on the pdf
     */
    public static List<String> wrapText(String text, PDFont font, int fontSize, float width) throws IOException {
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

