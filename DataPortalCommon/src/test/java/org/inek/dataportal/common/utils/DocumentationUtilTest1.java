package org.inek.dataportal.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.common.utils.KeyValueLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class DocumentationUtilTest1 {
    
    @Test
    public void testGetDocumentation() {
        System.out.println("getDocumentation");
        Object o = new TestEntity1();
        List<KeyValueLevel<?, ?>> expResult = new ArrayList<>();
        expResult.add(new KeyValueLevel<>("Name", "myName", 0));
        expResult.add(new KeyValueLevel<>("Anzahl", "100", 0));
        expResult.add(new KeyValueLevel<>("DateTime", "01.01.2015 10:20", 0));
        expResult.add(new KeyValueLevel<>("DateOnly", "01.01.2015", 0));
        List<KeyValueLevel> result = DocumentationUtil.getDocumentation(o);
        assertEquals(expResult, result);
    }
    
}

class TestEntity1{
    TestEntity1(){
        _persons.add(new Person1());
        _persons.add(new Person1());
        try {
            _dateTime = (new SimpleDateFormat("dd.MM.yyyy hh:mm:ss")).parse("01.01.2015 10:20:30");
            _dateOnly = (new SimpleDateFormat("dd.MM.yyyy hh:mm:ss")).parse("01.01.2015 10:20:30");
        } catch (ParseException ex) {
            Logger.getLogger(TestEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Documentation
    private String _name = "myName";
    
    @Documentation(name = "Anzahl")
    private int count=100;
    
    @Documentation
    private Date _dateTime;

    @Documentation(dateFormat = "dd.MM.yyyy")
    private Date _dateOnly;

    @Documentation(omitOnOtherValues = "TestEntity1._name=myName")
    private List<Person1> _persons = new ArrayList<>();
        
}

class Person1{
    @Documentation
    private String _firstName = "Firstname";
    
    @Documentation
    private String _lastName = "Lastname";
    
}
