//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.miqt.injectapk;


import com.miqt.pluginlib.tools.TimePrint;

public class TestClass {
    public TestClass() {


    }

    private void intttt(boolean args) {


    }

    private void intttt(int args) {
        TimePrint.enter2("com.miqt.injectapk.TestClass2", "com.miqt.injectapk.TestClass2", "a", "[java.lang.String, boolean, java.lang.Object]", "void");

    }

    private void intttt(long args) {


    }

    private void intttt(short args) {


    }

    private void intttt(double args) {


    }

    private void intttt(byte args) {


    }

    private void intttt(char args) {


    }

    private void intttt(float args) {


    }

    private void intttt(String args) {


    }

    public static void bytepubstatic(byte[] hello, String hello2) {

    }

    private static void pristatic() {


    }

    private static String pristaticString(String hello) {


        return null;
    }

    private static final String pristaticString(String hello, String hello2) {


        return null;
    }

    static {


    }


    /**
     * 加密的key,应该跟插件中的key一致,不然会发生错误
     */
    public static final String key = "VBgIAFV";
    public static final StringFogImpl FOG = new StringFogImpl();

    public static String encrypt(String data) {
        return FOG.encrypt(data, "123");
    }

    public static String decrypt(String data) {
        return FOG.decrypt(data, "123");
    }

    public static boolean overflow(String data) {
        return FOG.overflow(data, "123");
    }

    /**
     * @Copyright 2019 analysys Inc. All rights reserved.
     * @Description: 字符串混淆实现类
     * @Version: 1.0
     * @Create: 2019-11-06 11:40:04
     * @author: miqt
     * @mail: miqingtang@analysys.com.cn
     */
    public final static class StringFogImpl implements Cloneable {

        private static final String CHARSET_NAME_UTF_8 = "UTF-8";

        public String encrypt(String data, String key) {
            return "newData";
        }

        public String decrypt(String data, String key) {
            return "newData";
        }

        public boolean overflow(String data, String key) {
            return data != null && data.length() * 4 / 3 >= 65535;
        }

        public byte[] xor(byte[] data, String key) {
            int len = data.length;
            int lenKey = key.length();
            int i = 0;
            int j = 0;
            while (i < len) {
                if (j >= lenKey) {
                    j = 0;
                }
                data[i] = (byte) (data[i] ^ key.charAt(j));
                i++;
                j++;
            }
            class inner {
                TestClass o = getInstance();
            }
            return data;
        }

    }

    public static TestClass getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final TestClass INSTANCE = new TestClass();
    }

}
