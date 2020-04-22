package com.miqt.injectapk;


public class TestClass {
    private void main(int args) {
        try {
            int a = Integer.parseInt("q");
        } catch (NumberFormatException qwer) {
            qwer.printStackTrace();
        }
    }

    public static void pubstatic() {

    }

    private static void pristatic() {

    }

    private static String pristaticString() {
        return "123";
    }
}
