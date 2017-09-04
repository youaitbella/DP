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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.inek.dataportal.feature.psychstaff.backingbean.EditPsyStaff;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
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
    private static final Font FONT_TITLE = new Font(Font.getFamily("TIMES_ROMAN"), 18, Font.BOLD);
    private static final Font NORMALBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.BOLD);
    private static final Font SMALLBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.BOLD);
    private static final Font SMALL = new Font(Font.getFamily("TIMES_ROMAN"), 6, Font.NORMAL);
    private static final Font SSMALL = new Font(Font.FontFamily.HELVETICA, 6);
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Texts">
    private final String img = "D:\\projects\\DataPortal\\DataPortal\\InEK.gif";
    private final String infoText1 = "1. Die vereinbarten Berechnungstage in Anlage 1 und die tatsächlichen "
            + "Berechnungstage in Anlage 2 sind in einer einheitlichen Zählweise entweder nach LKA \noder nach PEPPV anzugeben.";
    private final String infoText2 = "2. Bei Kinder- und Jugendpsychiatrie einschließlich Erziehungsdienst";
    private final String infoText3 = "Diese Datei ist durch die Vertragspartner nach $11 BPflV zu unterschreiben "
            + "und als elektronische Kopie an das InEK zu senden";
    private final List<String> header1 = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "Stellenbesetzung für \neine vollständige Umsetzung der Psych-\nPV in VK",
            "Stellenbesetzung \nals \nBudgetgrundlage in VK",
            "\nDurschnittskosten \nin EURO");
    private final List<String> header2 = Arrays.asList("", "", "1", "2", "3", "4");
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PdfBuilder">
    public PdfBuilder() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createDocument">
    public void createDocument() throws DocumentException, FileNotFoundException, IOException,
            BadElementException, MalformedURLException, NoSuchAlgorithmException {

        
        String fileout = "D:\\projects\\DataPortal\\DataPortal\\Psychdokument.pdf";
        List<String> header3;

        Document document = new Document();        
        PdfWriter.getInstance(document, new FileOutputStream(fileout));        
        document.open();
        createMetadata(document);
        if (_editPsyStaff.getStaffProof().isForAdults()) {
            createPageForAdult(document);
        }
        if (_editPsyStaff.getStaffProof().isForKids()) {
            createPageForKids(document);
        }
        document.close();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext externalContext = fc.getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType(fileout);
        externalContext.setResponseHeader("Content-Disposition", fileout);
        //externalContext.setResponseHeader("Content-Type", fileout);

        FileInputStream inputStream = new FileInputStream(new File(fileout));
        OutputStream outputStream = externalContext.getResponseOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        fc.responseComplete();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForKids">
    private void createPageForKids(Document document) throws DocumentException, NoSuchAlgorithmException, IOException {
        List<String> header3;
        
        if (_editPsyStaff.getStaffProof().isForAdults()) {
            document.newPage();
        }
        addLogo(document, img, "Bereich Kinder und Jugendliche");
        PdfPTable tb_JK = new PdfPTable(6);
        tb_JK.setWidths(new int[]{3, 1, 3, 3, 3, 3});
        header3 = Arrays.asList("", "8", "Gesamt",
                _editPsyStaff.sumAgreedStaffingComplete(PsychType.Kids),
                _editPsyStaff.sumAgreedStaffingBudget(PsychType.Kids),
                _editPsyStaff.sumEffectiveStaffingComplete(PsychType.Kids));
        
        tb_JK.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        addHeader(tb_JK, header1);
        addHeader(tb_JK, header2);
        loadDataForKid(tb_JK);
        addHeader(tb_JK, header3);
        tb_JK.setSpacingBefore(30);
        tb_JK.setSpacingAfter(10);
        document.add(tb_JK);
        
        Paragraph  p = new Paragraph("Vereinbarte Berechnungstage: " 
                                        + String.valueOf(_editPsyStaff.getStaffProof().getKidsEffectiveDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(30);
        document.add(p);
        
        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForAdult">
    private void createPageForAdult(Document document) throws DocumentException, IOException, NoSuchAlgorithmException {
        List<String> header3;
        
        addLogo(document, img, "Bereich Erwachsene");
        PdfPTable tb = new PdfPTable(6);
        tb.setWidths(new int[]{3, 1, 3, 3, 3, 3});
        
        header3 = Arrays.asList("", "8", "Gesamt",_editPsyStaff.sumAgreedStaffingComplete(PsychType.Adults),
                _editPsyStaff.sumAgreedStaffingBudget(PsychType.Adults),
                _editPsyStaff.sumEffectiveStaffingComplete(PsychType.Adults));        
        
        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        addHeader(tb, header1);
        addHeader(tb, header2);
        loadDataForAdult(tb);
        addHeader(tb, header3);
        tb.setSpacingBefore(30);
        tb.setSpacingAfter(10);
        document.add(tb);
        
        Paragraph  p = new Paragraph("Vereinbarte Berechnungstage: " 
                                        + String.valueOf(_editPsyStaff.getStaffProof().getAdultsEffectiveDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(30);
        document.add(p);
        
        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForAdult">
    private void loadDataForAdult(PdfPTable tb) {

        int index = 1;
        for (StaffProofAgreed staffProofAgreed : _editPsyStaff.getStaffProof().getStaffProofsAgreed(PsychType.Adults)) {
            addRow(tb, staffProofAgreed.getOccupationalCatagory().getPersonnelGroup().getName(),
                    String.valueOf(index),
                    staffProofAgreed.getOccupationalCatagory().getName(),
                    staffProofAgreed.getStaffingComplete(),
                    staffProofAgreed.getStaffingBudget(),
                    staffProofAgreed.getAvgCost());
            index++;            
        }
    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForKid">
    private void loadDataForKid(PdfPTable tb) {

        int index = 1;
        for (StaffProofAgreed staffProofAgreed : _editPsyStaff.getStaffProof().getStaffProofsAgreed(PsychType.Kids)) {
            addRow(tb, staffProofAgreed.getOccupationalCatagory().getPersonnelGroup().getName(),
                    String.valueOf(index),
                    staffProofAgreed.getOccupationalCatagory().getName(),
                    staffProofAgreed.getStaffingComplete(),
                    staffProofAgreed.getStaffingBudget(),
                    staffProofAgreed.getAvgCost());
            index++;            
        }
    }
    //    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createMetadata">
    private void createMetadata(Document document) {
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
    private void addLogo(Document document, String IMG, String bereich) throws IOException, BadElementException, DocumentException {
        Image inekLogo = Image.getInstance(IMG);
        PdfPTable tb;
        PdfPCell cell;
        tb = new PdfPTable(2);
        tb.setWidths(new int[]{1, 3});
        cell = new PdfPCell(new PdfPCell(inekLogo, false));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb.addCell(cell);

        PdfPTable tb1 = new PdfPTable(1);
        
        cell = new PdfPCell(new Paragraph(bereich, FONT_TITLE));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        //cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("Anlage 1 zur Psych-Personalnachweisvereinbarung", NORMALBOLD));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);
        
        cell = new PdfPCell(new Paragraph("Signatur: " + _editPsyStaff.getStaffProof().getChecksumAgreement(), SMALLBOLD));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);
        
        cell = new PdfPCell(tb1);
        cell.setBorder(PdfPCell.NO_BORDER);
        
        tb.addCell(cell);

        document.add(tb);
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addHeader">
    private void addHeader(PdfPTable tb, List<String> l) {
        l.stream().forEach(e -> addCell(tb, e, SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY));
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addRow">
    private void addRow(PdfPTable tb, String personnelGroupName, String lfdNr,
            String occupationalCatagoryName, double staffingComplete,
            double staffingBudget, double avgCost) {

        addCell(tb, personnelGroupName, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, occupationalCatagoryName, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(staffingComplete), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffingBudget), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
        addCell(tb, String.valueOf(avgCost), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
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
        signaturArea.setWidths(new int[]{2, 2});
        signaturArea.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(signaturArea, "für das Krankenhaus", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "für die Sozialleistungsträger", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "________________________________________________", SMALL, Element.ALIGN_LEFT);
        for (int i = 0; i < 6; i++) {
            addLayoutCell(signaturArea, "_____________________________________________________", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        }
        document.add(signaturArea);
    }
    //</editor-fold>    

}
