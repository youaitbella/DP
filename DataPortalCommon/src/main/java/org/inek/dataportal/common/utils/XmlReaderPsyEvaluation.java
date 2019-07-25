package org.inek.dataportal.common.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class XmlReaderPsyEvaluation {

    private static final Logger LOGGER = Logger.getLogger("XmlReaderPsyEvaluation");

    private static final String XML_FILE = "/PsyEvaluationStatements.xml";

    public static String getStatementById(String id) {
        try {
            InputStream is = XmlReaderPsyEvaluation.class.getResourceAsStream(XML_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("statement");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getAttributes().getNamedItem("id").getTextContent().equals(id)) {
                    return nNode.getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return "";
    }
}
