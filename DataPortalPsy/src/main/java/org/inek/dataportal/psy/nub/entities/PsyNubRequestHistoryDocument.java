package org.inek.dataportal.psy.nub.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "NubRequestHistoryDocument", schema = "psy")
public class PsyNubRequestHistoryDocument implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npdId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "npdNubRequestHistoryId")
    private PsyNubRequestHistory _psyNubRequest;

    @Column(name = "npdName")
    private String _name = "";

    @Lob
    @Column(name = "npdContent")
    private byte[] _content;


    public PsyNubRequestHistory getPsyNubRequest() {
        return _psyNubRequest;
    }

    public String getName() {
        return _name;
    }

    public byte[] getContent() {
        return _content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestHistoryDocument that = (PsyNubRequestHistoryDocument) o;
        return _id == that._id &&
                Objects.equals(_psyNubRequest, that._psyNubRequest) &&
                Objects.equals(_name, that._name) &&
                Arrays.equals(_content, that._content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_id, _psyNubRequest, _name);
        result = 31 * result + Arrays.hashCode(_content);
        return result;
    }
}
