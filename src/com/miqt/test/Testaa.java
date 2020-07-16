package com.miqt.test;

import java.io.File;

public class Testaa {
    public static void main(String[] args) {
        System.out.println(new File("E:/blog/db.json").length());
        System.out.println(new File("E://blog//db.json").length());
        System.out.println(new File("E:////////blog//////db.json").length());
        System.out.println(new File("E:\\\\///blog//\\\\//db.json").length());
        System.out.println(new File("E:\\blog\\db.json").length());
        System.out.println(new File("E:\\\\blog\\\\db.json").length());
        System.out.println(new File("E:\\\\\\\\\\\\blog\\\\\\\\\\db.json").length());
    }
}
