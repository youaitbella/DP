/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.structures;

/**
 *
 * @author muellermi
 */
public class ProposalInfo<T> {

    private int _id;
    private String _name;
    private int _year;
    private T _status;  // status type may vary depending on the proposal type

    public ProposalInfo() {
    }

    public ProposalInfo(final int id, final String name, final int year, final T status) {
        _id = id;
        _name = name;
        _status = status;
        _year = year;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public T getStatus() {
        return _status;
    }

    public void setStatus(T status) {
        _status = status;
    }

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
    }

}
