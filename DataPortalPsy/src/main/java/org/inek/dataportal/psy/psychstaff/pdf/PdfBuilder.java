package org.inek.dataportal.psy.psychstaff.pdf;

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
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.inek.dataportal.psy.psychstaff.backingbean.EditPsyStaff;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofExplanation;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

/**
 *
 * @author aitbellayo
 */
public class PdfBuilder extends PdfPageEventHelper {

    private EditPsyStaff _editPsyStaff;

    // todo: pass PsychStaff object instead of editor
    // refactor class according to DRY
    public PdfBuilder(EditPsyStaff editPsyStaff) {
        _editPsyStaff = editPsyStaff;
    }

    //<editor-fold defaultstate="collapsed" desc="Fonts">
    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font NORMALBOLD = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font MEDIUMBOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font SMALLBOLD = new Font(Font.getFamily("TIMES_ROMAN"), 9, Font.BOLD);
    private static final Font SMALL = new Font(Font.getFamily("TIMES_ROMAN"), 8, Font.NORMAL);
    private static final Font SSMALL = new Font(Font.FontFamily.HELVETICA, 8);
    private static final Font FONT_NOTE = new Font(Font.FontFamily.HELVETICA, 6);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Texts">
    private final String fs = System.getProperty("file.separator");
    //private final String pu = ".." + fs;

    private final String infoText1 = "1. Die vereinbarten Berechnungstage in Anlage 1 und die tatsächlichen "
            + "Berechnungstage in Anlage 2 sind in einer einheitlichen Zählweise entweder nach LKA "
            + "oder nach PEPPV anzugeben.";
    private final String anlage1 = "Anlage 1 zur Psych-Personalnachweisvereinbarung";
    private final String anlage2 = "Anlage 2 zur Psych-Personalnachweisvereinbarung";
    private final String infoText2 = "2. Bei Kinder- und Jugendpsychiatrie einschließlich Erziehungsdienst";
    private final String infoText3 = "Diese Datei ist durch die Vertragspartner nach §11 BPflV zu unterschreiben "
            + "und als elektronische Kopie an das InEK zu senden";
    private final String infoText3A2 = "Dieser Ausdruck ist durch das Krankenhaus und den Jahresabschlussprüfer "
            + "nach §7 der Psych-Personalnachweis-Vereinbarung zu unterschreiben und als elektronische Kopie an das InEK zu senden";
    private final List<String> header1A1 = Arrays.asList("\nPersonalgruppen", "\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "Stellenbesetzung für \neine vollständige Umsetzung der Psych-\nPV in VK",
            "Stellenbesetzung \nals \nBudgetgrundlage in VK",
            "\nDurschnittskosten \nin EURO");

    private final List<String> header0A2 = Arrays.asList("", "", "", "Davon", "");
    private final List<String> header2A1 = Arrays.asList("", "", "1", "2", "3", "4");
    private final List<String> header1A2 = Arrays.asList("\nLfd. Nr.",
            "\nBerufsgruppen der Psych-PV",
            "\nPsych-PV-Personal in VK (jeweils in Summe)",
            "Anrechnung Fachkräfte anderer Berufsgruppen der Psych-PV in VK (§4 Abs. 4 Vereinb.) ",
            "Anrechnung Fachkräfte Nicht-Psych-PV Berufsgruppen in VK (§4 Abs. 5 Vereinb.) ",
            "Anrechnung Fachkräfte ohne direktes Beschäftigungsverh. in VK (§4 Abs. 6 Vereinb.)",
            "\nUmsetzungsgrad der Psych-PV in %");
    private final List<String> header2A2 = Arrays.asList("", "1", "2", "3", "4", "5", "6");
    private final List<String> headerExp = Arrays.asList("Psych-PV-Berufsgruppe, bei der die Anrechnung erfolgt",
            "Anrechnungs-\ntatbestand",
            "Tatsächlich Berufsgruppe der angerechneten Fachkraft ",
            "Angerechnete Stellenbesetzung in VK ",
            "\nErläuterung");

    private final String footer2 = "Die verwendeten Verfahren zur Ermittlung der tatsächlichen Stellenbesetzung und der "
            + "tatsächliche Kosten für das Psych-PV-Personal in Summe stellen eine sachgerechte Abgrenzung des für den "
            + "Nachweis zu berücksichtigenden Personals vom Gesamtpersonal des Krankenhauses nach den §§ 4 und 5 sicher.";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PdfBuilder">
    public PdfBuilder() {
    }
    //</editor-fold>

    private final DecimalFormat formatter0 = new DecimalFormat("###,##0");
    private final DecimalFormat formatter2 = new DecimalFormat("###,##0.00");

    //<editor-fold defaultstate="collapsed" desc="createDocument">
    public void createDocument() throws DocumentException, FileNotFoundException, IOException,
            BadElementException, MalformedURLException, NoSuchAlgorithmException {

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                int pageNumber = writer.getPageNumber();
                String text = "Seite " + pageNumber;
                Rectangle page = document.getPageSize();
                PdfPTable structure = new PdfPTable(1);
                structure.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                structure.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                structure.addCell(new Paragraph(text));
                structure.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
                structure.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
            }
        });

        document.open();
        createMetadata(document);

        if ("Anlage 1".equalsIgnoreCase(_editPsyStaff.getActiveTopic().getTitle().substring(0, 8))) {
            createPageForApx1(document, writer);
        } else {
            createPageForAnlage2(document, writer);
        }

        document.close();
        onEndPage(writer, document);

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext externalContext = fc.getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType("Psychpersonalnachweis_" + _editPsyStaff.getActiveTopic().getTitle().substring(0, 8) + ".pdf");

        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        response.reset();
        String fileName = "\"Psychpersonalnachweis_" + _editPsyStaff.getActiveTopic().getTitle().substring(0, 8) + ".pdf\"";
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentLength(byteArrayOutputStream.toByteArray().length);

        ServletOutputStream servletOutputStream = response.getOutputStream();
        servletOutputStream.write(byteArrayOutputStream.toByteArray());
        servletOutputStream.close();

        fc.responseComplete();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForApx1">
    public void createPageForApx1(Document document, PdfWriter writer) throws DocumentException, IOException, NoSuchAlgorithmException {
        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Erwachsene",
                anlage1,
                _editPsyStaff.getStaffProof().getSignatureAgreement());

        if (_editPsyStaff.getStaffProof().isForAdults()) {
            createPageForApx1(document, PsychType.Adults);
        }
        if (_editPsyStaff.getStaffProof().isForKids()) {
            createPageForApx1(document, PsychType.Kids);
        }
        addNote(document, infoText1, 50, 0);
        addNote(document, infoText2, 0, 20);
        document.newPage();
        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage1,
                _editPsyStaff.getStaffProof().getSignatureAgreement());

        addInfoText(document, infoText3, 30);
        addSignaturArea(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForAnlage2">
    public void createPageForAnlage2(Document document, PdfWriter writer) throws DocumentException, IOException, NoSuchAlgorithmException {
        if (_editPsyStaff.getStaffProof().isForAdults()) {
            createPageForAdultAn2(document, writer);
        }
        if (_editPsyStaff.getStaffProof().isForKids()) {
            createPageForKidsAn2(document, writer);
        }

        addNote(document, infoText1, 0, 0);
        addNote(document, infoText2, 0, 0);

        document.newPage();
        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective());

        addInfoText(document, footer2, 10);
        addInfoText(document, infoText3A2, 30);
        addSignaturAreaA2(document);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForKidsAn1">
    private void createPageForApx1(Document document, PsychType psychType) throws DocumentException, NoSuchAlgorithmException, IOException {
        String header = psychType == PsychType.Adults
                ? "Bereich Erwachsene"
                : "Bereich Kinder und Jugendliche";
        int agreedDays = psychType == PsychType.Adults
                ? _editPsyStaff.getStaffProof().getAdultsAgreedDays()
                : _editPsyStaff.getStaffProof().getKidsAgreedDays();
        document.add(new Paragraph(header, FONT_TITLE));
        PdfPTable pdfTable = new PdfPTable(6);
        pdfTable.setWidthPercentage(100);
        pdfTable.setWidths(new int[]{3, 1, 3, 3, 3, 3});
        pdfTable.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        loadDataForApx1(pdfTable, psychType);
        pdfTable.setSpacingBefore(10);
        pdfTable.setSpacingAfter(10);
        document.add(pdfTable);

        Paragraph p = new Paragraph("Vereinbarte Berechnungstage: " + formatter0.format(agreedDays), SMALLBOLD);
        p.setSpacingAfter(30);
        document.add(p);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForKidsAn2">
    private void createPageForKidsAn2(Document document, PdfWriter writer) throws DocumentException,
            NoSuchAlgorithmException, IOException {
        if (_editPsyStaff.getStaffProof().isForAdults()) {
            document.newPage();
        }
        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Kinder und Jugendliche",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective());
        document.add(new Paragraph("Bereich Kinder und Jugendliche", FONT_TITLE));
        PdfPTable tb_JK = new PdfPTable(7);
        tb_JK.setWidthPercentage(100);
        tb_JK.setWidths(new int[]{1, 4, 3, 3, 3, 3, 3});
        tb_JK.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        PdfPTable tb_exp = new PdfPTable(5);
        tb_exp.setWidthPercentage(100);
        tb_exp.setWidths(new int[]{4, 4, 3, 3, 6});
        tb_exp.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        loadDataForAnlage2(tb_JK, PsychType.Kids);
        tb_JK.setSpacingBefore(10);
        tb_JK.setSpacingAfter(10);
        document.add(tb_JK);
        if (IsToexplain(PsychType.Kids)) {
            Paragraph p = new Paragraph("Erläuterung zur Anrechnung von Fachkräften", NORMALBOLD);
            p.setSpacingAfter(10);
            document.add(p);
            addExplanationTable(tb_exp, PsychType.Kids);
        }
        document.add(tb_exp);
        Paragraph p = new Paragraph("Tatsächliche Berechnungstage: "
                + formatter0.format(_editPsyStaff.getStaffProof().getKidsEffectiveDays()), SMALLBOLD);
        p.setSpacingAfter(5);
        document.add(p);

        p = new Paragraph("Tatsächliche Kosten für das Psych-PV-Personal in Summe nach § 5 in Euro: "
                + formatter2.format(_editPsyStaff.getStaffProof().getKidsEffectiveCosts()), SMALLBOLD);
        p.setSpacingAfter(10);
        document.add(p);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="createPageForAdultAn2">
    private void createPageForAdultAn2(Document document, PdfWriter writer) throws DocumentException, IOException, NoSuchAlgorithmException {

        addLogo(document,
                "Vereinbarte Stellenbesetzung in Vollkräften \nBereich Erwachsene",
                anlage2,
                _editPsyStaff.getStaffProof().getSignatureEffective());
        document.add(new Paragraph("Bereich Erwachsene", FONT_TITLE));
        PdfPTable tb = new PdfPTable(7);
        tb.setWidthPercentage(100);
        tb.setWidths(new int[]{1, 4, 3, 3, 3, 3, 3});
        tb.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        PdfPTable tb_exp = new PdfPTable(5);
        tb_exp.setWidths(new int[]{4, 4, 3, 3, 6});
        tb_exp.setWidthPercentage(100);
        tb_exp.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        loadDataForAnlage2(tb, PsychType.Adults);
        tb.setSpacingBefore(10);
        tb.setSpacingAfter(10);
        document.add(tb);
        if (IsToexplain(PsychType.Adults)) {
            Paragraph p = new Paragraph("Erläuterung zur Anrechnung von Fachkräften", NORMALBOLD);
            p.setSpacingAfter(10);
            document.add(p);
            addExplanationTable(tb_exp, PsychType.Adults);
        }
        document.add(tb_exp);

        Paragraph p = new Paragraph("Tatsächliche Berechnungstage : "
                + formatter0.format(_editPsyStaff.getStaffProof().getAdultsEffectiveDays()), SMALLBOLD);
        p.setSpacingAfter(5);
        document.add(p);

        p = new Paragraph("Tatsächliche Kosten für das Psych-PV-Personal in Summe nach § 5 in Euro: "
                + formatter2.format(_editPsyStaff.getStaffProof().getAdultsEffectiveCosts()), SMALLBOLD);
        p.setSpacingAfter(30);
        document.add(p);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForApx1">
    private void loadDataForApx1(PdfPTable tb, PsychType psychType) {

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
        addCell(tb, formatter2.format(_editPsyStaff.sumAgreedStaffingComplete(psychType)),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, formatter2.format(_editPsyStaff.sumAgreedStaffingBudget(psychType)),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="loadDataForAnlage2">
    private void loadDataForAnlage2(PdfPTable tb, PsychType psychType) {

        int index = 1;
        addHeader0(tb, header0A2);
        addHeader(tb, header1A2);
        addHeader(tb, header2A2);
        for (StaffProofEffective staffProofEffective : _editPsyStaff.getStaffProof().getStaffProofsEffective(psychType)) {
            addRow2(tb, String.valueOf(index), staffProofEffective);
            index++;
        }
        addCell(tb, "8", SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, "Gesamt", SMALLBOLD, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, formatter2.format(_editPsyStaff.sumEffectiveStaffingComplete(psychType)),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, formatter2.format(_editPsyStaff.sumEffectiveStaffingDeductionPsych(psychType)),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, formatter2.format(_editPsyStaff.sumEffectiveStaffingDeductionNonPsych(psychType)),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, formatter2.format(_editPsyStaff.sumEffectiveStaffingDeductionOther(psychType)),
                SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);
        addCell(tb, "", SMALLBOLD, Element.ALIGN_RIGHT, BaseColor.LIGHT_GRAY);

    }
//    </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addExplanationTable">
    void addExplanationTable(PdfPTable tb, PsychType psychType) {

        addHeader(tb, headerExp);

        for (StaffProofExplanation staffProofExplanation : _editPsyStaff.getStaffProof().getStaffProofExplanations(psychType)) {
            {
                addCell(tb, staffProofExplanation.getOccupationalCategory().getName(), SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
                String t = "";
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
                addCell(tb, staffProofExplanation.getExplanation(), SMALL, Element.ALIGN_LEFT, BaseColor.WHITE);
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
        URL resource = FacesContext.getCurrentInstance().getExternalContext().getResource("/Image/InEK.gif");
        Image inekLogo = Image.getInstance(resource);
        PdfPTable tb;
        PdfPCell cell;
        tb = new PdfPTable(2);
        tb.setWidthPercentage(100);
        tb.setWidths(new int[]{1, 4});
        cell = new PdfPCell(new PdfPCell(inekLogo, false));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb.addCell(cell);

        PdfPTable tb1 = new PdfPTable(1);
        tb1.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        cell = new PdfPCell(new Paragraph(anlage, NORMALBOLD));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);

        cell = new PdfPCell(new Paragraph("", FONT_TITLE));
        cell.setLeft(50);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);

        printHospitalInfo(tb1, sig);

        cell = new PdfPCell(new Paragraph("", SMALLBOLD));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(PdfPCell.NO_BORDER);
        tb1.addCell(cell);

        cell = new PdfPCell(tb1);
        cell.setBorder(PdfPCell.NO_BORDER);

        tb.addCell(cell);
        tb.setSpacingAfter(20);

        document.add(tb);
    }

    private void printHospitalInfo(PdfPTable tb1, String sig) throws DocumentException {
        PdfPCell cell;
        StaffProof staffProof = _editPsyStaff.getStaffProof();
        PdfPTable tb_Hosp = new PdfPTable(2);
        tb_Hosp.setWidths(new int[]{2, 3});
        tb_Hosp.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        cell = new PdfPCell(new Paragraph("IK: " + staffProof.getIk(), NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        String hospital = _editPsyStaff.retrieveHospitalInfo();
        cell = new PdfPCell(new Paragraph("Name: " + hospital, NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        cell = new PdfPCell(new Paragraph("Vereinbarungsjahr: " + staffProof.getYear(), NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        cell = new PdfPCell(new Paragraph("Zählweise: " + (staffProof.getCalculationType() == 1 ? "PEPPV" : "BPflV/LKA"), NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);

        cell = new PdfPCell(new Paragraph("", NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        cell = new PdfPCell(new Paragraph("", NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        cell = new PdfPCell(new Paragraph("", NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        cell = new PdfPCell(new Paragraph("Signatur: " + sig, NORMALBOLD));
        cell.setBorder(PdfPCell.NO_BORDER);
        tb_Hosp.addCell(cell);
        tb1.addCell(tb_Hosp);

    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addHeader0">
    private void addHeader0(PdfPTable tb, List<String> l) {
        l.stream().forEach(e -> addCell(tb, e, SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY));
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="addHeader">
    private void addHeader(PdfPTable tb, List<String> l) {
        l.stream().forEach(e -> addCell(tb, e, SMALLBOLD, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY));
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="addRow">
    private void addRow(PdfPTable tb, String lfdNr, StaffProofAgreed staffProofAgreed) {

        addCell(tb, staffProofAgreed.getOccupationalCategory().getPersonnelGroup().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, staffProofAgreed.getOccupationalCategory().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(formatter2.format(staffProofAgreed.getStaffingComplete())),//.replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(formatter2.format(staffProofAgreed.getStaffingBudget())),//.replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(formatter2.format(staffProofAgreed.getAvgCost())),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="addRow2">
    private void addRow2(PdfPTable tb, String lfdNr, StaffProofEffective staffProofEffective) {

//        DecimalFormat formatter = new DecimalFormat("###,##0.00");
        addCell(tb, lfdNr, SMALL, Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY);
        addCell(tb, staffProofEffective.getOccupationalCategory().getName(),
                SMALL, Element.ALIGN_LEFT, BaseColor.LIGHT_GRAY);
        addCell(tb, String.valueOf(formatter2.format(staffProofEffective.getStaffingComplete())),//.replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(formatter2.format(staffProofEffective.getStaffingDeductionPsych())),//.replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(formatter2.format(staffProofEffective.getStaffingDeductionNonPsych())),//.replace(",", "."),
                SMALL, Element.ALIGN_RIGHT, BaseColor.WHITE);
        addCell(tb, String.valueOf(formatter2.format(staffProofEffective.getStaffingDeductionOther())),//.replace(",", "."),
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
        } else if (("Davon").equalsIgnoreCase(text)) {
            PdfPCell cell = new PdfPCell(new Paragraph(text, f));
            cell.setColspan(3);
            cell.setBackgroundColor(bg);
            cell.setHorizontalAlignment(align);
            tb.addCell(cell);
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
        cell.setPaddingBottom(30);
        tb.addCell(cell);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="addInfoText">
    private void addInfoText(Document document, String text, int spacing) throws DocumentException {
        Paragraph p;
        p = new Paragraph(text, SMALL);
        p.setSpacingBefore(spacing);
        p.setSpacingAfter(spacing);
        document.add(p);
    }
    //</editor-fold>     

    //<editor-fold defaultstate="collapsed" desc="addNote">
    private void addNote(Document document, String text, int spacingBefore, int spacingAfter) throws DocumentException {
        Paragraph p;
        p = new Paragraph(text, FONT_NOTE);
        p.setSpacingBefore(spacingBefore);
        p.setSpacingAfter(spacingAfter);
        document.add(p);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="addSignaturArea">
    private void addSignaturArea(Document document) throws DocumentException {
        PdfPTable signaturArea = new PdfPTable(2);
        signaturArea.setWidthPercentage(100);
        signaturArea.setWidths(new int[]{2, 2});
        signaturArea.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(signaturArea, "für das Krankenhaus", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "für die Sozialleistungsträger", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "________________________________________________", SMALL, Element.ALIGN_LEFT);
        for (int i = 0; i < 6; i++) {
            addLayoutCell(signaturArea, "_____________________________________________________", SMALL, Element.ALIGN_LEFT);
            addLayoutCell(signaturArea, "", SMALL, Element.ALIGN_LEFT);
        }
        document.add(signaturArea);
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="addSignaturAreaA2">
    private void addSignaturAreaA2(Document document) throws DocumentException {
        PdfPTable signaturArea = new PdfPTable(1);
        signaturArea.setWidthPercentage(100);
        signaturArea.setSpacingBefore(30);
        //signaturArea.setWidths(new int[]{3, 2});
        signaturArea.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        addLayoutCell(signaturArea, "Bestätigung durch das Krankenhaus (Ort, Datum und Unterschrift)", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "____________________________________________________________", SMALL, Element.ALIGN_LEFT);

        addLayoutCell(signaturArea, "Bestätigung durch den Jahresabschlussprüfer (Ort, Datum und Unterschrift)", SMALL, Element.ALIGN_LEFT);
        addLayoutCell(signaturArea, "____________________________________________________________", SMALL, Element.ALIGN_LEFT);

        document.add(signaturArea);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="IsToexplain">
    private boolean IsToexplain(PsychType psychType) {

        boolean t = false;
        for (StaffProofExplanation staffProofExplanation : _editPsyStaff.getStaffProof().getStaffProofExplanations(psychType)) {
            if (!("0".equalsIgnoreCase(staffProofExplanation.getEffectiveOccupationalCategory()))
                    || !("0".equalsIgnoreCase(String.valueOf(staffProofExplanation.getDeductedFullVigor())))
                    || !("0".equalsIgnoreCase(staffProofExplanation.getExplanation()))) {
                t = true;
            }
        }
        return t;
    }
    //</editor-fold>

}
