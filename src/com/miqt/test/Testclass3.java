package com.miqt.test;

import com.miqt.pluginlib.tools.TimePrint;

public class Testclass3 {
    private float testeee(long num4,float num5) {
        return num4;
    }
    private float testeee(long n1,float n2,int n3,byte n4,short n5,char n6,String n7,boolean n8,double n9 ,byte[] n10) {
        return n2;
    }
    private float tttt(long n1,float n2,int n3,byte n4,short n5,char n6,String n7,boolean n8,double n9 ,byte[] n10) {
        try {
            int a = 100/10;
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return n3;
        }
    }
    private long tttll(long n1,float n2,int n3,byte n4,short n5,char n6,String n7,boolean n8,double n9 ,byte[] n10) {
        try {
            if("i".endsWith("i")){
                return Integer.valueOf(100);
            }
            int a = (int) (100/n2);
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return n3;
        }
    }
}
