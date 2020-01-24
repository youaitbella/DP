package org.inek.dataportal.psy.nub.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "NubRequestDocument", schema = "psy")
public class PsyNubRequestDocument implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npdId")
    private int _id;

    @ManyToOne
    @JoinColumn(name = "npdNubRequestId")
    private PsyNubRequest _psyNubRequest;

    @Column(name = "npdName")
    private String _name = "";

    @Lob
    @Column(name = "npdContent")
    private byte[] _content;


    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public PsyNubRequest getPsyNubRequest() {
        return _psyNubRequest;
    }

    public void setPsyNubRequest(PsyNubRequest psyNubRequest) {
        this._psyNubRequest = psyNubRequest;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        this._content = content;
    }

    public String getContentTyp() {
        String[] content = _name.split("\\.");
        return content[content.length - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestDocument that = (PsyNubRequestDocument) o;
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
