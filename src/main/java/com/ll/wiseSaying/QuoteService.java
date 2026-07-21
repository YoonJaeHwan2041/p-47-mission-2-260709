package com.ll.wiseSaying;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class QuoteService {
    private final QuoteRepository quoteRepository = new QuoteRepository();

    public int add(String content, String author) {
        quoteRepository.save(content, author);
        return quoteRepository.count();
    }

    // 전체 목록
    public List<Quote> getList() {

        List<Quote> quotes = quoteRepository.findAll();
        return quotes.stream()
                .sorted(Comparator.comparing(Quote::getId).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
    //오오오버어어어어로로로로디이이이잉
    public List<Quote> getList(int page){
        List<Quote> quotes = quoteRepository.findAll();
        int skip = page * 5 -5;

        int totalPage = (quotes.size() -1) / 5 + 1;
        return quotes.stream()
                .sorted(Comparator.comparing(Quote::getId).reversed())
                .skip(skip)
                .limit(5)
                .collect(Collectors.toList());
    }

    public int totalPage(){
        int count = quoteRepository.count();
        return (count - 1) / 5 + 1;
    }

    //검색 목록
    public List<Quote> search(String keywordType, String keyword){
        return quoteRepository.findAll().stream()
                .filter(quote -> {
                    if (keyword == null) {
                        return true; // keyword가 없으면 다보여줌
                    }
                    String target = "author".equals(keywordType) ? quote.getAuthor() : quote.getContent();
                    return target.contains(keyword);
                })
                .collect(Collectors.toList());
    }


    public Quote findById(int id) {
        return quoteRepository.findById(id);
    }

    public boolean delete(int id) {
        return quoteRepository.deleteById(id);
    }

    public void update(Quote target, String content, String author) {
        target.modify(content, author);
    }
}
