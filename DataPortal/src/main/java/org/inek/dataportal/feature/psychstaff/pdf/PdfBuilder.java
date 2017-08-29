package org.inek.dataportal.feature.psychstaff.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author aitbellayo
 */
@Named(value = "pdfBuilder")
@SessionScoped
public class PdfBuilder implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Fonts">
    private static final Font FONT_TITLE = new Font(Font.getFamily("TIMES_ROMAN"), 20, Font.BOLD);
    private static final Font FONT_HEADER = new Font(Font.getFamily("Frutiger 55 Roman"), 10, Font.BOLD, BaseColor.BLUE);
    private static final Font FONT_TITLE_FOOTER = new Font(Font.getFamily("Calibri"), 8, Font.BOLD, BaseColor.BLUE);
    private static final Font SMALLBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.BOLD);
    private static final Font SMALL = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.NORMAL);
    private static final Font SSMALL = new Font(Font.FontFamily.HELVETICA, 6);
    //</editor-fold>

    private final String fout = "D:\\projects\\DataPortal\\DataPortal\\PsychStaffDocument__.pdf";
    private final String img = "D:\\projects\\DataPortal\\DataPortal\\InEK.gif";

    //<editor-fold defaultstate="collapsed" desc="PdfBuilder">
    public PdfBuilder() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createDocument">
    public void createDocument() throws DocumentException, FileNotFoundException, IOException, 
            BadElementException, MalformedURLException, NoSuchAlgorithmException {
        String fileout = fout;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileout));
        document.open();
        Paragraph p = new Paragraph();
        Chunk c;

        createMetadaten(document);        
        createLogo(document, img);        
        createTitle(document, p);
        createTableInfoandChecksum(document, fout);

        //<editor-fold defaultstate="collapsed" desc="Table PsyStaff">
        PdfPTable tb2 = new PdfPTable(6);
        tb2.setWidths(new int[]{3, 1, 4, 4, 3, 3});
        tb2.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        
        List<String>headers= Arrays.asList("Personalgruppen", "Lfd. Nr.", 
                                            "Berufsgruppen der Psych-PV", 
                                            "Stellenbesetzung für \neine vollständige Umsetzung der Psych-\nPV in VK",
                                            "Stellenbesetzung \nals \nBudgetgrundlage in VK","Durschnittskosten in EURO");
        addHeader(tb2, headers);
        headers= Arrays.asList("", "", "1", "2","3","4");
        addHeader(tb2, headers);
        
        addRow(tb2, "Ärztlicher Dienst", "1", "Ärzte","","","");
        
        ///////////////////////////////////////////////////////////////////////////////
        double gesamtStaffingComplete = 0.0, gesamtStaffingBudget = 0.0, gesamtAvgCost = 0.0;
        addCell(tb2, "Pflegedienst", SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb2, "2", SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        
        p = new Paragraph("Pflegepersonal", SMALL);
        c = new Chunk("2", SSMALL).setTextRise(2);
        p.add(c);
        tb2.addCell(p);
        
        addCell(tb2, "", SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tb2, "", SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tb2, "", SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        ////////////////////////////////////////////////////////////////////////////////
        addRowWithSpan(tb2, "Medizinisch technischer Dienst");
        addRow(tb2, "Funktionsdienst","7", "Ergotherapie","","","");
        headers= Arrays.asList("", "8", "Gesamt",String.valueOf(gesamtStaffingBudget),
                                    String.valueOf(gesamtStaffingBudget),
                                            String.valueOf(gesamtStaffingComplete));
        addHeader(tb2, headers);
        
        tb2.setSpacingAfter(40);
        document.add(tb2);
        //</editor-fold>
        
        createInfoText(document, "Diese Datei ist durch die Vertragspartner nach $11 BPflV zu unterschreiben "
                + "und als elektronische Kopie an das InEK zu senden");
        createSignatureArea(document);
        createFooter(document);
        document.close();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createMetadaten">
    private void createMetadaten(Document document) {
        document.addTitle("Title");
        document.addSubject("Subject");
        document.addKeywords("ort" + ", " + "nachName");
        document.addAuthor("Author: " + "getVorName()" + " " + "getNachName()");
        document.addCreator("nachName");
        document.addCreationDate();
        document.addHeader("ort", "vorName");
        document.addProducer();
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="createLogo">
    private void createLogo(Document document , String IMG) throws IOException, BadElementException, DocumentException {
        Image inekLogo= Image.getInstance(IMG);
        PdfPTable tb;
        PdfPCell cell;
        tb = new PdfPTable(1);
        cell = new PdfPCell(new PdfPCell(inekLogo, false));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb.addCell(cell);
        addLayoutCell(tb, "Institut für das Entgeltsystem im Krankenhaus GmbH", FONT_HEADER, Element.ALIGN_CENTER);
        addLayoutCell(tb, "Institutsträger: Deutsche Krankenhausgesellschaft • GKV-Spitzenverband • Verband der privaten Krankenversicherung", 
                            SSMALL, Element.ALIGN_CENTER);
        addLayoutCell(tb, "", SSMALL, Element.ALIGN_CENTER);
        addLayoutCell(tb, new Chunk("InEK GmbH", FONT_TITLE_FOOTER) + " • Auf dem Seidenberg 3 • 53721 Siegburg", SSMALL, Element.ALIGN_LEFT);
        document.add(tb);
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="createTitle">
    private void createTitle(Document document, Paragraph p) throws DocumentException {
        p = new Paragraph("Export: Anlage 1", FONT_TITLE);
        p.setSpacingAfter(50);
        p.setIndentationLeft(50);// Abstand von links
        document.add(p);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="addHeader">
    private void addHeader(PdfPTable tb2, List l) {
        l.stream().forEach(e->addCell(tb2, e.toString(), SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY));
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addRow">
    private void addRow(PdfPTable tb2, String personnelGroupName, String lfdNr, 
                            String occupationalCatagoryName, String staffingComplete, 
                                    String staffingBudget, String avgCost) {
        
        addCell(tb2, personnelGroupName, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb2, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb2, occupationalCatagoryName, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb2, staffingComplete, SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tb2, staffingBudget, SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tb2, avgCost, SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="addRowWithSpan">
    private void addRowWithSpan(PdfPTable tb2, String pg) {
        PdfPCell cell;
        // 3. row
        cell = new PdfPCell(new Paragraph(pg, SMALL));
        cell.setRowspan(4);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tb2.addCell(cell);
        addSpanedRow(tb2, "3", "Psychologen", "", "", "");
        addSpanedRow(tb2, "4", "Sozialarbeiter", "", "", "");
        addSpanedRow(tb2, "5", "Bewegungstherapeuten", "", "", "");
        addSpanedRow(tb2, "6", "Logopäden (nur KJP)", "", "", "");
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="addSpanedRow">
    private void addSpanedRow(PdfPTable tb2, String lfdNr, String bg, String stellenbesetzungPVinVK, String stellenbesetzungVK, String sum ) {
        addCell(tb2, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb2, bg, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb2, stellenbesetzungPVinVK, SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tb2, stellenbesetzungVK, SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
        addCell(tb2, sum, SMALL, Element.ALIGN_CENTER, BaseColor.WHITE);
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="addCell">
    void addCell(PdfPTable tb, String text, Font f, int align, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, f));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        tb.addCell(cell);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cellLayout">
    //@Override
    public void cellLayout(PdfPCell cell, Rectangle rectangle, PdfContentByte[] canvases) {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addLayoutCell">
    void addLayoutCell(PdfPTable tb, String text, Font f, int align) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, f));
        cell.setHorizontalAlignment(align);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb.addCell(cell);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="createInfoText">
    private void createInfoText(Document document, String text) throws DocumentException {
        Paragraph p;
        p = new Paragraph(text, SMALL);
        p.setIndentationLeft(50);
        p.setSpacingAfter(50);
        document.add(p);
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="createSignatureArea">
    private void createSignatureArea(Document document) throws DocumentException {
        PdfPTable unterschrift = new PdfPTable(2);
        unterschrift.setWidths(new int[]{2, 1});
        unterschrift.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(unterschrift, "für das Krankenhaus", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(unterschrift, "für die Sozialleistungsträger", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(unterschrift, "________________________________________________", SMALL, Element.ALIGN_LEFT);
        for (int i = 0; i < 6; i++) {
            addLayoutCell(unterschrift, "______________________________", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(unterschrift, "", SMALL, Element.ALIGN_LEFT);
        }
        document.add(unterschrift);
    }
    //</editor-fold>    
 
    //<editor-fold defaultstate="collapsed" desc="createFooter">
    private void createFooter(Document document) throws DocumentException {
        PdfPTable tb;
        tb = new PdfPTable(4);
        tb.setSpacingBefore(120);
        addLayoutCell(tb, "InEK", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Telefon", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Bankverbindung", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Geschäftsführer", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Institut für das Entgeltsystem", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "0 22 41.93 82-0", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Deutsche Apotheker- und", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Dr. Frank Heimig", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "im Krankenhaus GmbH", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Fax", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Ärztebank eG", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "USt-IDNR.", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "0 22 41.93 82-35", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "IBAN", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "DE223530796", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Auf dem Seidenberg 3", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "E-Mail", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "DE33 3006 0601 0005 2572 55", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Handelsregisternummer", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "53721 Siegburg", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "info@inek-drg.de", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "BIC", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "HRB 7395", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Internet", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "DAAEDEDD", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Gerichtsstand", FONT_TITLE_FOOTER, Element.ALIGN_LEFT);
        addLayoutCell(tb, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "www.g-drg.de", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Amtsgericht Siegburg", SMALL, Element.ALIGN_LEFT);
        document.add(tb);
    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="createTableInfoandChecksum">
    private void createTableInfoandChecksum(Document document, String fileName) throws NoSuchAlgorithmException, IOException, DocumentException {
        //String checkSum = "Checksumme: " + toHex(Hash.MD5.checksum(file));
        PdfPTable tb1 = new PdfPTable(new float[]{1, 1});
        tb1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(tb1, "Anlage 1 zur Psych-Personalnachweisvereinbarung", SMALLBOLD, Element.ALIGN_LEFT);
        addLayoutCell(tb1, "Checksumme: " + getFileChecksum(MessageDigest.getInstance("MD5"), 
                        new File(fileName)), SMALLBOLD, Element.ALIGN_LEFT);//SHA-1, MD5,SHA-512
        document.add(tb1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="toHex">
    private static String toHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getFileChecksum">
    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);
        
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        
        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        
        //close the stream; We don't need it now.
        fis.close();
        
        //Get the hash's bytes
        byte[] bytes = digest.digest();
        
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        //return complete hash
        return sb.toString();
    }
    //</editor-fold>

}
