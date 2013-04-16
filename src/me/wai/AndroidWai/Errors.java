package me.wai.AndroidWai;

public class Errors {
    public static void handleError(Throwable e) {
        System.out.println("Error!");
        e.printStackTrace();
    }
}
