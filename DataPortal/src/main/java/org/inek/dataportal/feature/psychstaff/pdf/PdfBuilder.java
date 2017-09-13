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
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.inek.dataportal.feature.psychstaff.backingbean.EditPsyStaff;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofExplanation;
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
    private final String fs = System.getProperty("file.separator");
    private final String pu = ".." + fs;
    //private final String path = pu + "resources" + fs + "img" + fs + "InEK.gif";

    private final String path = "D:\\projects\\DataPortal\\DataPortal\\InEK.gif";

    private final String infoText1 = "1. Die vereinbarten Berechnungstage in Anlage 1 und die tatsächlichen "
            + "Berechnungstage in Anlage 2 sind in einer einheitlichen Zählweise entweder nach LKA "
            + "\noder nach PEPPV anzugeben.";
    private final String anlage1 = "Anlage 1 zur Psych-Personalnachweisvereinbarung";
    private final String anlage2 = "Anlage 2 zur Psych-Personalnachweisvereinbarung";
    private final String infoText2 = "2. Bei Kinder- und Jugendpsychiatrie einschließlich Erziehungsdienst";
    private final String infoText3 = "Diese Datei ist durch die Vertragspartner nach $11 BPflV zu unterschreiben "
            + "und als elektronische Kopie an das InEK zu senden";
    private final String infoText3A2 = "Dieser Ausdruck ist durch den Jahresabschlussprüfer nach $7 der Psych-Personalnachweis-Vereinbarung"
            + " zu unterschreiben"
            + "und als elektronische Kopie an das InEK zu senden";
    private final List<String> header1A1 = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "Stellenbesetzung für \neine vollständige Umsetzung der Psych-\nPV in VK",
            "Stellenbesetzung \nals \nBudgetgrundlage in VK",
            "\nDurschnittskosten \nin EURO");
    private final List<String> header2A1 = Arrays.asList("", "", "1", "2", "3", "4");
    private final List<String> header1A2 = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "\nPsych-PV-Personal in VK (jeweils in Summe)",
            "Anrechnung Fachkräfte anderer Berufsgruppen der Psych-PV in VK (§4 Abs. 4 Vereinb.) ",
            "Anrechnung Fachkräfte Nicht-Psych-PV Berufsgruppen in VK (§4 Abs. 5 Vereinb.) ",
            "Anrechnung Fachkräfte ohne direktes Beschäftigungsverh. in VK (§4 Abs. 6 Vereinb.)",
            "\nPsych-PV-Personal in VK (jeweils in Summe) ");
    private final List<String> header2A2 = Arrays.asList("", "", "1", "2", "3", "4", "5", "6");
    private final List<String> headerExp = Arrays.asList("Psych-PV-Berufsgruppe, bei der die Anrechnung erfolgt",
            "Anrechnungstatbestand",
            "Tatsächlich Berufsgruppe der angerechneten Fachkraft ",
            "Angerechnete Stellenbesetzung in VK ",
            "Erläuterung");
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
        externalContext.setResponseContentType("Psychpersonalnachweis_" + _editPsyStaff.getActiveTopic().getTitle().substring(11) + ".pdf");

        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        response.reset();
        String fileName = "\"Psychpersonalnachweis_" + _editPsyStaff.getActiveTopic().getTitle().substring(11) + ".pdf\"";
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentLength(byteArrayOutputStream.toByteArray().length);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(byteArrayOutputStream.toByteArray());
        servletOutputStream.close();

        fc.responseComplete();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForKidsAn1">
    private void createPageForKidsAn1(Document document) throws DocumentException, NoSuchAlgorithmException, IOException {

        addLogo(document, 
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage1,
                _editPsyStaff.getStaffProof().getSignatureAgreement(PsychType.Kids));
        PdfPTable tb_JK = new PdfPTable(6);
        tb_JK.setWidths(new int[]{3, 1, 3, 3, 3, 3});
        tb_JK.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        loadDataForAnlage1(tb_JK);
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
    private void createPageForKidsAn2(Document document) throws DocumentException,
            NoSuchAlgorithmException, IOException {

        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective(PsychType.Kids));
        PdfPTable tb_JK = new PdfPTable(8);
        tb_JK.setWidths(new int[]{3, 1, 3, 3, 3, 3, 3, 3});
        tb_JK.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPTable tb_exp = new PdfPTable(5);
        tb_exp.setWidths(new int[]{2, 3, 3, 3, 6});
        tb_exp.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        loadDataForAnlage2(tb_JK);
        tb_JK.setSpacingBefore(30);
        tb_JK.setSpacingAfter(10);
        document.add(tb_JK);
        Paragraph p = new Paragraph("Erläuterung zur Anrechnung von Fachkräften", NORMALBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(10);
        document.add(p);
        addExplanationTable(tb_exp);
        document.add(tb_exp);

        p = new Paragraph("Tatsächliche Berechnungstage: "
                + String.valueOf(_editPsyStaff.getStaffProof().getKidsEffectiveDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(5);
        document.add(p);

        p = new Paragraph("Tatsächliche Kosten für das Psych-PV-Personal in Summe nach § 5 in Euro: "
                + String.valueOf(_editPsyStaff.getStaffProof().getKidsEffectiveCosts()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(30);
        document.add(p);

        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3A2, 50);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForAdultAn1">
    private void createPageForAdultAn1(Document document) throws DocumentException, IOException, NoSuchAlgorithmException {

        addLogo(document, 
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Erwachsene",
                anlage1,
                _editPsyStaff.getStaffProof().getSignatureAgreement(PsychType.Adults));
        PdfPTable tb = new PdfPTable(6);
        tb.setWidths(new int[]{3, 1, 3, 3, 3, 3});

        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        loadDataForAnlage1(tb);
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

        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Erwachsene",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective(PsychType.Adults));
        PdfPTable tb = new PdfPTable(8);
        tb.setWidths(new int[]{3, 1, 3, 3, 3, 3, 3, 3});
        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPTable tb_exp = new PdfPTable(5);
        tb_exp.setWidths(new int[]{2, 3, 3, 3, 6});
        tb_exp.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        loadDataForAnlage2(tb);
        tb.setSpacingBefore(30);
        tb.setSpacingAfter(10);
        document.add(tb);
        Paragraph p = new Paragraph("Erläuterung zur Anrechnung von Fachkräften", NORMALBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(10);
        document.add(p);
        addExplanationTable(tb_exp);
        document.add(tb_exp);

        p = new Paragraph("Tatsächliche Berechnungstage : "
                + String.valueOf(_editPsyStaff.getStaffProof().getAdultsEffectiveDays()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(5);
        document.add(p);

        p = new Paragraph("Tatsächliche Kosten für das Psych-PV-Personal in Summe nach § 5 in Euro: "
                + String.valueOf(_editPsyStaff.getStaffProof().getAdultsEffectiveCosts()), SMALLBOLD);
        p.setIndentationLeft(50);
        p.setSpacingAfter(30);
        document.add(p);

        addInfoText(document, infoText1, 0);
        addInfoText(document, infoText2, 20);
        addInfoText(document, infoText3A2, 50);
        addSignaturAreaA2(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForAnlage1">
    private void loadDataForAnlage1(PdfPTable tb) {

        PsychType psychType = ("Anlage 1 - Erw").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle()) ? PsychType.Adults : PsychType.Kids;
        int index = 1;
        addHeader(tb, header1A1);
        addHeader(tb, header2A1);
        for (StaffProofAgreed staffProofAgreed : _editPsyStaff.getStaffProof().getStaffProofsAgreed(psychType)) {
            addRow(tb, String.valueOf(index), staffProofAgreed);
            index++;
        }
        addCell(tb, "", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumAgreedStaffingComplete(psychType).replace(",", "."),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumAgreedStaffingBudget(psychType).replace(",", "."),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForAnlage2">
    private void loadDataForAnlage2(PdfPTable tb) {

        PsychType psychType = ("Anlage 2 - Erw").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle()) ? PsychType.Adults : PsychType.Kids;
        int index = 1;
        addHeader(tb, header1A2);
        addHeader(tb, header2A2);
        for (StaffProofEffective staffProofEffective : _editPsyStaff.getStaffProof().getStaffProofsEffective(psychType)) {
            addRow2(tb, String.valueOf(index), staffProofEffective);
            index++;
        }
        addCell(tb, "", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingComplete(psychType).replace(",", "."),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionPsych(psychType).replace(",", "."),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionNonPsych(psychType).replace(",", "."),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, _editPsyStaff.sumEffectiveStaffingDeductionOther(psychType).replace(",", "."),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);

    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createExplanationTable">
    void addExplanationTable(PdfPTable tb) {

        addHeader(tb, headerExp);
        PsychType psychType = ("Anlage 2 - Erw").equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle()) ? PsychType.Adults : PsychType.Kids;

        for (StaffProofExplanation staffProofExplanation : _editPsyStaff.getStaffProof().getStaffProofExplanations(psychType)) {
            {
                addCell(tb, staffProofExplanation.getOccupationalCategory().getName(), SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
                String t = "";
//                        staffProofExplanation.getDeductedSpecialistId() == 4 ? "Anrechnung Fachkräfte anderer Berufsgruppen der Psych-PV"
//                        : staffProofExplanation.getDeductedSpecialistId() == 5 ? "Anrechnung Fachkräfte Nicht-Psych-PV Berufsgruppen"
//                        : "\\ Anrechnung Fachkräfte ohne direktes Beschäftigungsverh.";

                switch (staffProofExplanation.getDeductedSpecialistId()) {
                    case 4:
                        t = "Anrechnung Fachkräfte anderer Berufsgruppen der Psych-PV";
                        break;
                    case 5:
                        t = "Anrechnung Fachkräfte Nicht-Psych-PV Berufsgruppen";
                        break;
                    default:
                        t = "Anrechnung Fachkräfte ohne direktes Beschäftigungsverh.";
                        break;
                }
                addCell(tb, t, SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
                addCell(tb, staffProofExplanation.getEffectiveOccupationalCategory(), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
                addCell(tb, String.valueOf(staffProofExplanation.getDeductedFullVigor()), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
                addCell(tb, staffProofExplanation.getExplanation(), SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
            }
        }
    }
    //</editor-fold>

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
    private void addLogo(Document document, String bereich, String anlage, String sig)
            throws IOException, BadElementException, DocumentException {
        
        URL resource = FacesContext.getCurrentInstance().getExternalContext().getResource("/resources/img/InEK.gif");
        Image inekLogo = Image.getInstance(resource);
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

        DecimalFormat formatter = new DecimalFormat("###,##0.00");
        addCell(tb, staffProofAgreed.getOccupationalCategory().getPersonnelGroup().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, staffProofAgreed.getOccupationalCategory().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(staffProofAgreed.getStaffingComplete()).replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofAgreed.getStaffingBudget()).replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        //addCell(tb, String.valueOf(staffProofAgreed.getAvgCost()).replace(",", "."), 
        addCell(tb, String.valueOf(formatter.format(staffProofAgreed.getAvgCost())),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="addRow">
    private void addRow2(PdfPTable tb, String lfdNr, StaffProofEffective staffProofEffective) {
        addCell(tb, staffProofEffective.getOccupationalCategory().getPersonnelGroup().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, staffProofEffective.getOccupationalCategory().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingComplete()).replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingDeductionPsych()).replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingDeductionNonPsych()).replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(staffProofEffective.getStaffingDeductionOther()).replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, _editPsyStaff.determineFactor(staffProofEffective),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
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

    //<editor-fold defaultstate="collapsed" desc="addSignaturAreaA2">
    private void addSignaturAreaA2(Document document) throws DocumentException {
        PdfPTable signaturArea = new PdfPTable(1);
        //signaturArea.setWidths(new int[]{2, 2});
        signaturArea.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(signaturArea, "Bestätigung durch das Krankenhaus (Ort, Datum und Unterschrift)", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "Bestätigung durch den Jahesabschlussprüfer (Ort, Datum und Unterschrift)", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "________________________________________________", SMALL, Element.ALIGN_LEFT);
        //addLayoutCell(signaturArea, "________________________________________________", SMALL, Element.ALIGN_LEFT);

        document.add(signaturArea);
    }
    //</editor-fold>    

}
