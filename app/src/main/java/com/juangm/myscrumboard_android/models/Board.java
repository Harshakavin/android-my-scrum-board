package com.juangm.myscrumboard_android.models;

import java.util.ArrayList;

//Board model
public class Board {

    private String name;
    private String description;
    private ArrayList<String> cards = new ArrayList<>();
    private String owner;
    private String _id;

    public Board(String name, String description,String id) {
        this.name = name;
        this.description = description;
        this.owner = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }

    public String getOwners() {
        return owner;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
