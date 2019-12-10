package com.jitenderkumar.newsfeed.models;

public class Note {

    String text;
    String authorName;

    public Note(String text, String authorName) {
        this.text = text;
        this.authorName = authorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
