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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.inek.dataportal.feature.psychstaff.backingbean.EditPsyStaff;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofEffective;
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
    private static final Font FONT_TITLE = new Font(Font.getFamily("TIMES_ROMAN"), 16, Font.BOLD);
    private static final Font NORMALBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.BOLD);
    private static final Font SMALLBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 7, Font.BOLD);
    private static final Font SMALL = new Font(Font.getFamily("TIMES_ROMAN"), 6, Font.NORMAL);
    private static final Font SSMALL = new Font(Font.FontFamily.HELVETICA, 6);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Texts">
    private final String img = "D:\\projects\\DataPortal\\DataPortal\\InEK.gif";
    private final String infoText1 = "1. Die vereinbarten Berechnungstage in Anlage 1 und die tatsächlichen "
            + "Berechnungstage in Anlage 2 sind in einer einheitlichen Zählweise entweder nach LKA \noder nach PEPPV anzugeben.";
    private final String anlage1 = "Anlage 1 zur Psych-Personalnachweisvereinbarung";
    private final String anlage2 = "Anlage 2 zur Psych-Personalnachweisvereinbarung";
    private final String infoText2 = "2. Bei Kinder- und Jugendpsychiatrie einschließlich Erziehungsdienst";
    private final String infoText3 = "Diese Datei ist durch die Vertragspartner nach $11 BPflV zu unterschreiben "
            + "und als elektronische Kopie an das InEK zu senden";
    private final List<String> header1A1 = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "Stellenbesetzung für \neine vollständige Umsetzung der Psych-\nPV in VK",
            "Stellenbesetzung \nals \nBudgetgrundlage in VK",
            "\nDurschnittskosten \nin EURO");
    private final List<String> header2A1 = Arrays.asList("", "", "1", "2", "3", "4");
    private final List<String> header1A2 = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "Psych-PV-Personal in VK (jeweils in Summe)",
            "Anrechnung Fachkräfte anderer Berufsgruppen der Psych-PV in VK (§4 Abs. 4 Vereinb.) ",
            "Anrechnung Fachkräfte Nicht-Psych-PV Berufsgruppen in VK (§4 Abs. 5 Vereinb.) ",
            "Anrechnung Fachkräfte ohne direktes Beschäftigungsverh. in VK (§4 Abs. 6 Vereinb.)",
            "Psych-PV-Personal in VK (jeweils in Summe) ");
    private final List<String> header2A2 = Arrays.asList("", "", "1", "2", "3", "4", "5", "6");
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PdfBuilder">
    public PdfBuilder() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createDocument">
    public void createDocument() throws DocumentException, FileNotFoundException, IOException,
            BadElementException, MalformedURLException, NoSuchAlgorithmException {

        //List<String> header3;
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        createMetadata(document);

        if (("Anlage 1 - Erw").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle())) {
            createPageForAdultAn1(document);
        } else if (("Anlage 1 - KJP").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle())) {
            createPageForKidsAn1(document);
        } else if (("Anlage 2 - Erw").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle())) {
            createPageForAdultAn2(document);
        } else if (("Anlage 2 - KJP").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle())) {
            createPageForKidsAn2(document);
        }
        document.close();

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext externalContext = fc.getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType("psychdocument.pdf");

        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=\"psychdocument.pdf\"");
        response.setContentLength(byteArrayOutputStream.toByteArray().length);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(byteArrayOutputStream.toByteArray());
        servletOutputStream.close();

        fc.responseComplete();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForKidsAn1">
    private void createPageForKidsAn1(Document document) throws DocumentException, NoSuchAlgorithmException, IOException {

        addLogo(document, img,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage1,
                _editPsyStaff.getStaffProof().getSignatureAgreement(PsychType.Kids));
        PdfPTable tb_JK = new PdfPTable(6);
        tb_JK.setWidths(new int[]{3, 1, 3, 3, 3, 3});
        tb_JK.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        addHeader(tb_JK, header1A1);
        addHeader(tb_JK, header2A1);
        loadDataForKidA1(tb_JK);
        tb_JK.setSpacingBefore(30);
        tb_JK.setSpacingAfter(10);
        document.add(tb_JK);

        Paragraph p = new Paragraph("Vereinbarte Berechnungstage: "
                + String.valueOf(_editPsyStaff.getStaffProof().getKidsAgreedDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(30);
        document.add(p);

        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForKidsAn2">
    private void createPageForKidsAn2(Document document) throws DocumentException, NoSuchAlgorithmException, IOException {

        addLogo(document, img,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective(PsychType.Kids));
        PdfPTable tb_JK = new PdfPTable(8);
        tb_JK.setWidths(new int[]{3, 1, 3, 3, 3, 3, 3, 3});
        tb_JK.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        addHeader(tb_JK, header1A2);
        addHeader(tb_JK, header2A2);
        loadDataForKidA2(tb_JK);
        tb_JK.setSpacingBefore(30);
        tb_JK.setSpacingAfter(10);
        document.add(tb_JK);

        Paragraph p = new Paragraph("Tatsächliche Berechnungstage: "
                + String.valueOf(_editPsyStaff.getStaffProof().getKidsEffectiveDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(5);
        document.add(p);

        Paragraph p1 = new Paragraph("Tatsächliche Kosten für das Psych-PV-Personal in Summe nach § 5 in Euro: "
                + String.valueOf(_editPsyStaff.getStaffProof().getKidsEffectiveCosts()), SMALLBOLD);
        p1.setIndentationLeft(50);
        p1.setSpacingAfter(30);
        document.add(p1);

        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForAdultAn1">
    private void createPageForAdultAn1(Document document) throws DocumentException, IOException, NoSuchAlgorithmException {

        addLogo(document, img,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Erwachsene",
                anlage1,
                _editPsyStaff.getStaffProof().getSignatureAgreement(PsychType.Adults));
        PdfPTable tb = new PdfPTable(6);
        tb.setWidths(new int[]{3, 1, 3, 3, 3, 3});

        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        addHeader(tb, header1A1);
        addHeader(tb, header2A1);
        loadDataForAdultA1(tb);
        tb.setSpacingBefore(30);
        tb.setSpacingAfter(10);
        document.add(tb);

        Paragraph p = new Paragraph("Vereinbarte Berechnungstage: "
                + String.valueOf(_editPsyStaff.getStaffProof().getAdultsAgreedDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(30);
        document.add(p);

        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForAdultAn2">
    private void createPageForAdultAn2(Document document) throws DocumentException, IOException, NoSuchAlgorithmException {

        addLogo(document, img,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Erwachsene",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective(PsychType.Adults));
        PdfPTable tb = new PdfPTable(8);
        tb.setWidths(new int[]{3, 1, 3, 3, 3, 3, 3, 3});
        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        addHeader(tb, header1A2);
        addHeader(tb, header2A2);
        loadDataForAdultA2(tb);
        tb.setSpacingBefore(30);
        tb.setSpacingAfter(10);
        document.add(tb);

        Paragraph p = new Paragraph("Tatsächliche Berechnungstage : "
                + String.valueOf(_editPsyStaff.getStaffProof().getAdultsEffectiveDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(5);
        document.add(p);

        Paragraph p1 = new Paragraph("Tatsächliche Kosten für das Psych-PV-Personal in Summe nach § 5 in Euro: "
                + String.valueOf(_editPsyStaff.getStaffProof().getAdultsEffectiveCosts()), SMALLBOLD);
        p1.setIndentationLeft(50);
        p1.setSpacingAfter(30);
        document.add(p1);

        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForAdultA1">
    private void loadDataForAdultA1(PdfPTable tb) {

        int index = 1;
        for (StaffProofAgreed staffProofAgreed : _editPsyStaff.getStaffProof().getStaffProofsAgreed(PsychType.Adults)) {
            addRow(tb, String.valueOf(index), staffProofAgreed);
            index++;
        }
        addCell(tb, "", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumAgreedStaffingComplete(PsychType.Adults), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumAgreedStaffingBudget(PsychType.Adults), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForAdultA2">
    private void loadDataForAdultA2(PdfPTable tb) {

        int index = 1;
        for (StaffProofEffective staffProofEffective : _editPsyStaff.getStaffProof().getStaffProofsEffective(PsychType.Adults)) {
            addRow2(tb, String.valueOf(index), staffProofEffective);
            index++;
        }
        addCell(tb, "", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingComplete(PsychType.Adults), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionPsych(PsychType.Adults), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionNonPsych(PsychType.Adults), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionOther(PsychType.Adults), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        
    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForKidA1">
    private void loadDataForKidA1(PdfPTable tb) {

        int index = 1;
        for (StaffProofAgreed staffProofAgreed : _editPsyStaff.getStaffProof().getStaffProofsAgreed(PsychType.Kids)) {
            addRow(tb, String.valueOf(index), staffProofAgreed);
            index++;
        }
        addCell(tb, "", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumAgreedStaffingComplete(PsychType.Kids), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumAgreedStaffingBudget(PsychType.Kids), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
    }
    //    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForKidA2">
    private void loadDataForKidA2(PdfPTable tb) {

        int index = 1;
        for (StaffProofEffective staffProofEffective : _editPsyStaff.getStaffProof().getStaffProofsEffective(PsychType.Kids)) {
            addRow2(tb, String.valueOf(index), staffProofEffective);
            index++;
        }
        addCell(tb, "", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingComplete(PsychType.Kids), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionPsych(PsychType.Kids), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionNonPsych(PsychType.Kids), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionOther(PsychType.Kids), SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
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
    private void addLogo(Document document, String IMG, String bereich, String anlage, String sig)
            throws IOException, BadElementException, DocumentException {
        Image inekLogo = Image.getInstance(IMG);
        PdfPTable tb;
        PdfPCell cell;
        tb = new PdfPTable(2);
        tb.setWidths(new int[]{1, 4});
        cell = new PdfPCell(new PdfPCell(inekLogo, false));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb.addCell(cell);

        PdfPTable tb1 = new PdfPTable(1);

        cell = new PdfPCell(new Paragraph(anlage, NORMALBOLD));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);

        cell = new PdfPCell(new Paragraph(bereich, FONT_TITLE));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);
        // todo: replace psychType by variable 
        cell = new PdfPCell(new Paragraph("Signatur: " + sig, SMALLBOLD));
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
    private void addRow(PdfPTable tb, String lfdNr, StaffProofAgreed staffProofAgreed) {
        addCell(tb, staffProofAgreed.getOccupationalCatagory().getPersonnelGroup().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, staffProofAgreed.getOccupationalCatagory().getName(), SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(staffProofAgreed.getStaffingComplete()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofAgreed.getStaffingBudget()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofAgreed.getAvgCost()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="addRow">
    private void addRow2(PdfPTable tb, String lfdNr, StaffProofEffective staffProofEffective) {
        addCell(tb, staffProofEffective.getOccupationalCatagory().getPersonnelGroup().getName(), SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, staffProofEffective.getOccupationalCatagory().getName(), SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingComplete()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingDeductionPsych()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingDeductionNonPsych()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingDeductionOhter()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, _editPsyStaff.determineFactor(staffProofEffective), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
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
