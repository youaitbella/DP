/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.entities;

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

    public C_122_222(int _type, char _type2, String _name, int _count, int _sort) {
        this._type = _type;
        this._type2 = _type2;
        this._name = _name;
        this._count = _count;
        this._sort = _sort;
    }

    public C_122_222() {
    }

    public int getType() {
        return _type;
    }

    public void setType(int _type) {
        this._type = _type;
    }

    public char getType2() {
        return _type2;
    }

    public void setType2(char _type2) {
        this._type2 = _type2;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public int getCount() {
        return _count;
    }

    public void setCount(int _count) {
        this._count = _count;
    }

    public int getSort() {
        return _sort;
    }

    public void setSort(int _sort) {
        this._sort = _sort;
    }
}
