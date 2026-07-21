package com.ll.wiseSaying;

public class Quote {
    private final int id;
    private String content;
    private String author;

    public Quote(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void modify(String content, String author) {
        this.content = content;
        this.author = author;
    }
}
