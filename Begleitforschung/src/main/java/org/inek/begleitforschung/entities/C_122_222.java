/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.begleitforschung.entities;

/**
 *
 * @author vohldo
 */
public class C_122_222 {

    private int _type;
    private char _type2;
    private String _name;
    private int _count;
    private int _sort;

    public C_122_222(int type, char type2, String name, int count, int sort) {
        this._type = type;
        this._type2 = type2;
        this._name = name;
        this._count = count;
        this._sort = sort;
    }

    public C_122_222() {
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }

    public char getType2() {
        return _type2;
    }

    public void setType2(char type2) {
        this._type2 = type2;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getCount() {
        return _count;
    }

    public void setCount(int count) {
        this._count = count;
    }

    public int getSort() {
        return _sort;
    }

    public void setSort(int sort) {
        this._sort = sort;
    }
}
