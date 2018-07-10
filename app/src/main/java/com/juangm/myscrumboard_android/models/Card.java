package com.juangm.myscrumboard_android.models;

//Card model
public class Card {
    public String getOwner() {
        return owner;
    }

    public String getBoard_id() {
        return board_id;
    }

    private String content;
    private String owner;
    private String category;
    private String board_id;
    private String _id;
    private int time;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Card(String id, String content, String owner , String category, String boardId, Integer time ) {
        this.content = content;
        this.category = category;
        this.board_id = boardId;
        this.owner = owner;
        this._id = id;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBoard() {
        return board_id;
    }

    public void setBoard(String board) {
        this.board_id = board;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
