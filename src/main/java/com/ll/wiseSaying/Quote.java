package com.ll.wiseSaying;

import java.time.LocalDateTime;

public class Quote {
    private final int id;
    private String content;
    private String author;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Quote(int id, String content, String author, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public boolean isNew(int id){
        return id == 0;
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
