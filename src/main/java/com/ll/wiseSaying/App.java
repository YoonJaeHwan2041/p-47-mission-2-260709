package com.ll.wiseSaying;

import java.util.Scanner;

public class App {
    private final Scanner sc = new Scanner(System.in);
    private final QuoteController quoteController = new QuoteController(sc);

    public void run() {
        while (true) {
            System.out.println("== 명언 앱 ==");
            System.out.print("명령) ");
            String command = sc.nextLine();

            if (command.equals("종료")) {
                System.out.println("앱을 종료합니다.");
                break;
            }

            quoteController.doAction(command);
        }
    }
}
