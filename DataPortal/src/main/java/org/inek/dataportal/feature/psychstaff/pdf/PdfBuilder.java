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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.inek.dataportal.feature.psychstaff.backingbean.EditPsyStaff;
import org.inek.dataportal.feature.psychstaff.entity.OccupationalCatagory;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;

/**
 *
 * @author aitbellayo
 */
@Named(value = "pdfBuilder")
@SessionScoped
public class PdfBuilder implements Serializable {

    @Inject
    private EditPsyStaff _editPsyStaff;

    //<editor-fold defaultstate="collapsed" desc="Fonts">
    private static final Font FONT_TITLE = new Font(Font.getFamily("TIMES_ROMAN"), 20, Font.BOLD);
    private static final Font FONT_HEADER = new Font(Font.getFamily("Frutiger 55 Roman"), 10, Font.BOLD, BaseColor.BLUE);
    private static final Font FONT_TITLE_FOOTER = new Font(Font.getFamily("Calibri"), 8, Font.BOLD, BaseColor.BLUE);
    private static final Font SMALLBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.BOLD);
    private static final Font SMALL = new Font(Font.getFamily("TIMES_ROMAN"), 6, Font.NORMAL);
    private static final Font SSMALL = new Font(Font.FontFamily.HELVETICA, 6);
    //</editor-fold>
    private final String img = "D:\\projects\\DataPortal\\DataPortal\\InEK.gif";

    //<editor-fold defaultstate="collapsed" desc="PdfBuilder">
    public PdfBuilder() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createDocument">
    public void createDocument() throws DocumentException, FileNotFoundException, IOException,
            BadElementException, MalformedURLException, NoSuchAlgorithmException {

        String fileout = "D:\\projects\\DataPortal\\DataPortal\\"+_editPsyStaff.getActiveTopic().getTitle().trim()+".pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileout));
        document.open();

        createMetadaten(document);
        addLogo(document, img);
        addTitle(document, _editPsyStaff.getActiveTopic().getTitle());
        createTableInfoandChecksum(document, _editPsyStaff.getActiveTopic().getTitle());

        //<editor-fold defaultstate="collapsed" desc="Table PsyStaff">
        PdfPTable tb = new PdfPTable(6);
        tb.setWidths(new int[]{3, 1, 3, 3, 3, 3});
        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        List<String> headers = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
                "\nBerufsgruppen der Psych-PV",
                "Stellenbesetzung für \neine vollständige Umsetzung der Psych-\nPV in VK",
                "Stellenbesetzung \nals \nBudgetgrundlage in VK",
                "\nDurschnittskosten \nin EURO");
        addHeader(tb, headers);
        headers = Arrays.asList("", "", "1", "2", "3", "4");
        addHeader(tb, headers);

        int index = 1;
        double sumStaffingComplete = 0.0, sumStaffingBudget = 0.0, AvgCosts = 0.0;
        for (OccupationalCatagory occupationalCatagory : _editPsyStaff.getOccupationalCategories()) {
            addRow(tb, occupationalCatagory.getPersonnelGroup().getName(),
                    String.valueOf(index),
                    occupationalCatagory.getName(),
                    _editPsyStaff
                            .getStaffProof()
                            .getStaffProofsAgreed(PsychType.Adults)
                            .get(occupationalCatagory.getPersonnelGroup()
                                    .getId())
                            .getStaffingComplete(),
                    _editPsyStaff.getStaffProof()
                            .getStaffProofsAgreed(PsychType.Adults)
                            .get(occupationalCatagory.getPersonnelGroup()
                                    .getId())
                            .getStaffingBudget(),
                    _editPsyStaff
                            .getStaffProof()
                            .getStaffProofsAgreed(PsychType.Adults)
                            .get(occupationalCatagory.getPersonnelGroup()
                                    .getId())
                            .getAvgCost());
            index++;
            sumStaffingComplete += _editPsyStaff
                    .getStaffProof()
                    .getStaffProofsAgreed(PsychType.Adults)
                    .get(occupationalCatagory.getPersonnelGroup()
                            .getId())
                    .getStaffingComplete();
            sumStaffingBudget += _editPsyStaff.getStaffProof()
                    .getStaffProofsAgreed(PsychType.Adults)
                    .get(occupationalCatagory.getPersonnelGroup()
                            .getId())
                    .getStaffingBudget();
            AvgCosts += _editPsyStaff
                            .getStaffProof()
                            .getStaffProofsAgreed(PsychType.Adults)
                            .get(occupationalCatagory.getPersonnelGroup()
                                    .getId())
                            .getAvgCost();
        }
        AvgCosts /= _editPsyStaff.getOccupationalCategories().size();
        headers = Arrays.asList("", "8", "Gesamt", String.valueOf(sumStaffingComplete),
                String.valueOf(sumStaffingBudget),
                String.valueOf(AvgCosts));
        addHeader(tb, headers);

        tb.setSpacingAfter(20);
        document.add(tb);
        //</editor-fold>

        addInfoText(document, "1. Die vereinbarten Berechnungstage in Anlage 1 und die tatsächlichen "
                + "Berechnungstage in Anlage 2 sind in einer einheitlichen Zählweise entweder nach LKA \noder nach PEPPV anzugeben.", 0);
        addInfoText(document, "2. Bei Kinder- und Jugendpsychiatrie einschließlich Erziehungsdienst", 20);
        addInfoText(document, "Diese Datei ist durch die Vertragspartner nach $11 BPflV zu unterschreiben "
                + "und als elektronische Kopie an das InEK zu senden", 50);
        addSignaturArea(document);
        addFooter(document);
        document.close();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createMetadaten">
    private void createMetadaten(Document document) {
        document.addTitle("");
        document.addSubject("");
        document.addKeywords("" + ", " + "");
        document.addAuthor("" + "" + " " + "");
        document.addCreator("");
        document.addCreationDate();
        document.addHeader("", "");
        document.addProducer();
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addLogo">
    private void addLogo(Document document, String IMG) throws IOException, BadElementException, DocumentException {
        Image inekLogo = Image.getInstance(IMG);
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

    //<editor-fold defaultstate="collapsed" desc="addTitle">
    private void addTitle(Document document, String s) throws DocumentException {
        Paragraph p = new Paragraph("Export: " + s , FONT_TITLE);
        p.setSpacingAfter(50);
        p.setIndentationLeft(30);
        document.add(p);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addHeader">
    private void addHeader(PdfPTable tb, List <String> l) {
        l.stream().forEach(e -> addCell(tb, e, SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY));
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addRow">
    private void addRow(PdfPTable tb2, String personnelGroupName, String lfdNr,
            String occupationalCatagoryName, double staffingComplete,
            double staffingBudget, double avgCost) {

        addCell(tb2, personnelGroupName, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb2, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb2, occupationalCatagoryName, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb2, String.valueOf(staffingComplete), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tb2, String.valueOf(staffingBudget), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tb2, String.valueOf(avgCost), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="addCell">
    void addCell(PdfPTable tb, String text, Font f, int align, BaseColor bg) {
        if (("Pflegepersonal").equalsIgnoreCase(text)) {
            Paragraph p = new Paragraph("Pflegepersonal", SMALL);
            Chunk c = new Chunk("2", SSMALL).setTextRise(2);
            p.add(c);
            tb.addCell(p);
        } else {
            PdfPCell cell = new PdfPCell(new Paragraph(text, f));
            cell.setBackgroundColor(bg);
            cell.setHorizontalAlignment(align);
            tb.addCell(cell);
        }
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

    //<editor-fold defaultstate="collapsed" desc="addInfoText">
    private void addInfoText(Document document, String text, int spacing) throws DocumentException {
        Paragraph p;
        p = new Paragraph(text, SMALL);
        p.setIndentationLeft(50);
        p.setSpacingAfter(spacing);
        document.add(p);
    }
    //</editor-fold>     

    //<editor-fold defaultstate="collapsed" desc="addSignaturArea">
    private void addSignaturArea(Document document) throws DocumentException {
        PdfPTable signaturArea = new PdfPTable(2);
        signaturArea.setWidths(new int[]{2, 1});
        signaturArea.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(signaturArea, "für das Krankenhaus", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "für die Sozialleistungsträger", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "________________________________________________", SMALL, Element.ALIGN_LEFT);
        for (int i = 0; i < 6; i++) {
            addLayoutCell(signaturArea, "______________________________", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        }
        document.add(signaturArea);
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addFooter">
    private void addFooter(Document document) throws DocumentException {
        PdfPTable tb;
        tb = new PdfPTable(4);
        tb.setSpacingBefore(100);
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

    //<editor-fold defaultstate="collapsed" desc="addTableInfoandChecksum">
    private void createTableInfoandChecksum(Document document , String s) throws NoSuchAlgorithmException, IOException, DocumentException {
        PdfPTable tb = new PdfPTable(new float[]{1, 1});
        tb.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(tb, s + " zur Psych-Personalnachweisvereinbarung", SMALLBOLD, Element.ALIGN_LEFT);
        addLayoutCell(tb, "Signatur: " + _editPsyStaff.getStaffProof().getChecksumAgreement(), SMALLBOLD, Element.ALIGN_LEFT);
        tb.setSpacingAfter(5);
        document.add(tb);
    }
    //</editor-fold>
}
