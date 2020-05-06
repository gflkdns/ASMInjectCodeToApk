package com.miqt.test;

public class TestClass4 {
    public void hello() {
        new AbTestClass() {

            @Override
            void hello(double pm1, float pm2, int pm3) {
                Object[] o = new Object[]{TestClass4.this, this,pm1,pm2,pm3};
            }
        };
    }
}
