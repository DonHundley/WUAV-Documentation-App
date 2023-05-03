package bll;

import be.Project;
import be.Task;
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
import java.util.ArrayList;
import java.util.List;


public class PDFHandler {


    /**
     * method to export a pdf with text and images to create the documentation report
     **/
    //
    public static void exportDocumentation(Image image1, Image image2, String comment1, String comment2, Task task, Project project) throws IOException {

        PDDocument document = Loader.loadPDF(new File("src/main/java/resources/projectDocumentation.pdf"));
        PDPage page = document.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
        PDFont pdfFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        contentStream.setFont(pdfFont, 12);
        float[] color = new float[]{0, 0, 0};
        contentStream.setNonStrokingColor(color[0], color[1], color[2]);

        PDRectangle pageSize = page.getMediaBox();
        float pageHeight = pageSize.getHeight();
        float margin = 20;
        float spacing = 30;
        float x = spacing;
        float y = pageHeight - 80;

        writeProjectInfo(contentStream, project, x, y);
        y = y - spacing;
        writeTaskDescription(contentStream, task, pdfFont, 12, page.getMediaBox().getWidth() - margin * 2, x, y, spacing);

        /*IMAGES*/
        int imageWidth = 200;
        int imageHeight = 166;
        y = y - spacing * 3 - imageHeight;
        x = (page.getMediaBox().getWidth() - 300) / 2;

        insertLayout(contentStream, document, task.getTaskLayout(), x, y, imageWidth, imageHeight);

        y = y - spacing - imageHeight;

        insertImageWithComment(contentStream, document, image1, comment1, x, y, spacing, imageWidth, imageHeight);

        y = y - imageHeight - spacing * 2;
        insertImageWithComment(contentStream, document, image2, comment2, x, y, spacing, imageWidth, imageHeight);

        contentStream.close();
        String exportFile = "Doc " + project.getProjName() + ".pdf";
        document.save(exportFile);
        document.close();
    }

    /**
     * method to insert an image of the project's layout
     */
    private static void insertLayout(PDPageContentStream contentStream, PDDocument doc, Image layout, float x, float y, float imageWidth, float imageHeight) throws IOException {

        ByteArrayOutputStream layoutStream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(layout, null), "png", layoutStream);
        PDImageXObject pdImageXObjectLayout = PDImageXObject.createFromByteArray(doc, layoutStream.toByteArray(), "layout");
        contentStream.drawImage(pdImageXObjectLayout, x, y, imageWidth, imageHeight);
    }

    /**
     * method to insert an image with a comment under
     */
    private static void insertImageWithComment(PDPageContentStream contentStream, PDDocument document, Image image, String comment, float x, float y, float spacing, float imageWidth, float imageHeight) throws IOException {
        ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "jpg", imageStream);
        PDImageXObject pdImageXObjectImg1 = PDImageXObject.createFromByteArray(document, imageStream.toByteArray(), "");
        contentStream.drawImage(pdImageXObjectImg1, x, y, imageWidth, imageHeight);
        y = y - spacing;
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(comment);
        contentStream.endText();
    }

    /**
     * method to write the description on a pdf document
     */
    private static void writeTaskDescription(PDPageContentStream contentStream, Task task, PDFont pdfFont, int fontSize, float maxLineWidth, float x, float y, float spacing) throws IOException {

        List<String> lines = wrapText("Description: " + task.getTaskDesc(), pdfFont, fontSize, maxLineWidth);
        for (String line : lines) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(line);
            contentStream.endText();
            y -= 15;
        }
    }

    /**
     * method to write the project info on a pdf document
     */
    private static void writeProjectInfo(PDPageContentStream contentStream, Project project, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText("Project: " + project.getProjName() + "   " + "Date: " + project.getProjDate());
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

