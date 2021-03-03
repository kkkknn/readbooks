package com.kkkkkn.readbooks.entity;

public class MainBooks {
    private int id;
    private String name;
    private int replaceSum;

    public MainBooks(int id, String name,int replace) {
        this.id = id;
        this.name = name;
        this.replaceSum = replace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReplaceSum() {
        return replaceSum;
    }

    public void setReplaceSum(int update) {
        replaceSum = update;
    }

    public boolean isUpdate(){
        return replaceSum>0;
    }
}
