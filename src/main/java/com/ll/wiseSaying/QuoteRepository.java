package com.ll.wiseSaying;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuoteRepository {
    private final List<Quote> quotes = new ArrayList<>();
    private int lastId = 0;

    public void save(String content, String author) {
        Quote quote = new Quote(++lastId, content, author, LocalDateTime.now(), LocalDateTime.now());
        quotes.add(quote);
    }



    //전체 목록
    public List<Quote> findAll() {
        return quotes;
    }

    public Quote findById(int id) {
        for (Quote quote : quotes) {
            if (quote.getId() == id) {
                return quote;
            }
        }
        return null;
    }

    public boolean deleteById(int id) {
        return quotes.removeIf(quote -> quote.getId() == id);
    }

    public int count() {
        return quotes.size();
    }
}
