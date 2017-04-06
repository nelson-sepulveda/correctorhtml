package ufps.negocio;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class pdf2 {

    /**
     * The resulting PDF file.
     */
    public pdf2() {
    }

    public pdf2(String dir, String[][] d) throws DocumentException {
        try {
            this.createPdf(dir, d);
        } catch (IOException ex) {
            Logger.getLogger(pdf2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inner class to add a table as header.
     */
    class TableHeader extends PdfPageEventHelper {

        /**
         * The header text.
         */
        String header;
        /**
         * The template with the total number of pages.
         */
        PdfTemplate total;

        /**
         * Adds a header to every page
         *
         * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
         * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
         */
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable table = new PdfPTable(3);
            try {
                table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(527);
                table.getDefaultCell().setFixedHeight(20);
                table.addCell(header);
                addImage(document, "src/ufps/imagen/banner-pdf.png", 0, 0, 800, 109);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
    }

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws DocumentException
     * @throws IOException
     * @throws SQLException
     */
    public void createPdf(String filename, String[][] datos)
            throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 36, 36, 120, 36);
        System.out.println(filename);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        TableHeader event = new TableHeader();
        writer.setPageEvent(event);
        document.open();
        createTable(document, datos);
        document.close();
    }

    private static void createTable(Document document, String[][] datos)
            throws BadElementException {
        PdfPTable table = new PdfPTable(2);
        PdfPCell c1 = new PdfPCell(new Phrase("Etiqueta"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Descripción del error"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        for (int i = 0; i <= datos.length - 1; i++) {
            table.addCell(datos[i][0]);
            table.addCell(datos[i][1]);
        }
        try {
            document.add(table);
        } catch (DocumentException ex) {
            Logger.getLogger(pdf2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Image addImage(Document document, String dirImage, int x, int y, int ancho, int largo) {
        try {
            Image imagen = Image.getInstance(dirImage);
            imagen.scaleToFit(575, 100);
            imagen.setAbsolutePosition(10f, 740f);
            imagen.setAlignment(Chunk.ALIGN_MIDDLE);

            document.add(imagen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
