package com.ll.wiseSaying;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuoteRepository {
    private static final String QUOTE_DIR = "db/wiseSaying";
    private static final Path LAST_ID_PATH = Path.of(QUOTE_DIR, "lastId.txt");

    private final JsonMapper jsonMapper = new JsonMapper();
    private final List<Quote> quotes = new ArrayList<>();
    private int lastId = 0;

    public QuoteRepository() {
        try {
            Files.createDirectories(Path.of(QUOTE_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadLastId();
        loadAllQuotes();
    }

    private void loadLastId(){
        if(Files.exists(LAST_ID_PATH)) {
            try {
                String text = Files.readString(LAST_ID_PATH).trim();
                lastId = text.isEmpty() ? 0 : Integer.parseInt(text);
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    private void saveLastId() {
        try {
            Files.writeString(LAST_ID_PATH, String.valueOf(lastId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAllQuotes() {
        File dir = new File(QUOTE_DIR);
        File[] files = dir.listFiles((d, name) -> name.matches("\\d+\\.json"));
        if(files == null) return;

        for(File file : files){
            try{
                String json = Files.readString(file.toPath());
                quotes.add(jsonMapper.fromJson(json));
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void saveQuoteFile(Quote quote) {
        try {
            Files.writeString(Path.of(QUOTE_DIR, quote.getId() + ".json"), jsonMapper.toJson(quote));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String content, String author) {
        lastId++;
        Quote quote = new Quote(lastId, content, author, LocalDateTime.now(), LocalDateTime.now());
        quotes.add(quote);
        saveQuoteFile(quote);
        saveLastId();
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
