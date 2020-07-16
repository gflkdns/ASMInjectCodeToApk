package com.miqt.test

class TestASDF {
     static void main(String[] args) {

         println(new File("E:/blog/db.json").bytes.length);
         println(new File("E://blog//db.json").bytes.length);
         println(new File("E:////////blog//////db.json").bytes.length);
         println(new File("E:\\\\///blog//\\\\//db.json").bytes.length);
         println(new File("E:\\blog\\db.json").bytes.length);
         println(new File("E:\\\\blog\\\\db.json").bytes.length);
         println(new File("E:\\\\\\\\\\\\blog\\\\\\\\\\db.json").bytes.length);




     }
}
