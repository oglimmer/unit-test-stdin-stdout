package de.oglimmer.stdinout;

import java.util.Scanner;

public class FakeApp {

    public static void main(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            if (!line.equals("add-user oli glimmer")) {
                System.out.println("error");
                return;
            }
            System.out.println("user added");

            line = scanner.nextLine();
            if (!line.equals("add-user heiner test")) {
                System.out.println("error");
                return;
            }
            System.out.println("user added");

            line = scanner.nextLine();
            if (!line.equals("list-users")) {
                System.out.println("error");
                return;
            }
            System.out.println("user:");
            System.out.println("1,oli,glimmer");
            System.out.println("2,heiner,test");

            line = scanner.nextLine();
            if (!line.equals("del-user 1")) {
                System.out.println("error");
                return;
            }
            System.out.println("user deleted");

            line = scanner.nextLine();
            if (!line.equals("list-users")) {
                System.out.println("error");
                return;
            }
            System.out.println("user:");
            System.out.println("2,heiner,test");

            line = scanner.nextLine();
            if (!line.equals("quit")) {
                System.out.println("error");
                return;
            }
        }
    }

}
