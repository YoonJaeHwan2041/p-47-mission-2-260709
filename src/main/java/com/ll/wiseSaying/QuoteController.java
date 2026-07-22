package com.ll.wiseSaying;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuoteController {
    private final Scanner sc;
    private final QuoteService quoteService = new QuoteService();

    public QuoteController(Scanner sc) {
        this.sc = sc;
    }

    public void doAction(String command) {
        QuoteRequest qr = new QuoteRequest(command);
        try {
            switch (qr.actionName) {
                case "등록":
                    register();
                    break;

                case "목록":
                    list(qr);
                    break;

                case "삭제":
                    delete(qr);
                    break;

                case "수정":
                    modify(qr);
                    break;

                default:
                    System.out.println("잘못된 명령어입니다.");
                    break;
            }
        }catch (NumberFormatException e){
            System.out.println("잘못된 명령어 입니다.");
        }

    }

    private void register() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        int count = quoteService.add(content, author);
        System.out.println("현재 " + count + "개의 명언이 등록되었습니다.");
    }

    //목록 분기
    private void list(QuoteRequest qr) {
        List<Quote> quotes = new ArrayList<>();
        String keywordType = qr.getParam("keywordtype", null);
        String keyword = qr.getParam("keyword", null);
        int page = qr.getParamAsInt("page", 1);


        if (keywordType != null && keyword != null){
            quotes = quoteService.search(keywordType,keyword);

            System.out.println("----------------------");
            System.out.println("검색타입 : " + keywordType);
            System.out.println("검색어 : " + keyword);
        }else if(page != 1){
            quotes = quoteService.getList(page);
        } else {
            quotes = quoteService.getList();
        }

        int totalPage = quoteService.totalPage();

        System.out.println("번호 / 명언 / 작가");
        System.out.println("---------------------");
        for (Quote quote : quotes) {
            System.out.println(quote.getId() + " / " + quote.getContent() + " / " + quote.getAuthor());
        }
        System.out.println("---------------------");
        StringBuilder sb = new StringBuilder("페이지 : ");
        for (int i = 1; i <= totalPage; i++) {
            if (i == page) {
                sb.append("[").append(i).append("]");
            } else {
                sb.append(i);
            }
            if (i < totalPage) {
                sb.append(" / ");
            }
        }
        System.out.println(sb);

    }

    private void delete(QuoteRequest qr) {
        int id = qr.getParamAsInt("id", -1);
        if (quoteService.delete(id)) {
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언을 찾지 못했습니다.");
        }
    }

    private void modify(QuoteRequest qr) {
        int id = qr.getParamAsInt("id", -1);
        Quote target = quoteService.findById(id);
        if (target == null) {
            System.out.println(id + "번의 명언을 찾지 못했습니다.");
            return;
        }

        System.out.println("명언(기존) : " + target.getContent());
        System.out.print("새 명언 : ");
        String newContent = sc.nextLine();
        System.out.println("명언(기존) : " + target.getAuthor());
        System.out.print("새 작가 : ");
        String newAuthor = sc.nextLine();

        quoteService.update(target, newContent, newAuthor);
        System.out.println(id + "번의 명언이 수정되었습니다.");
    }
}
