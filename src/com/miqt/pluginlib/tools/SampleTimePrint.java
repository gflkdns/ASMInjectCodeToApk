package com.miqt.pluginlib.tools;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SampleTimePrint implements ITimePrint {

    @Override
    public void onMethodEnter(Object o,
                              String className,
                              String methodName,
                              String argsType,
                              String returnType,
                              Object... args) {
    }

    @Override
    public void onMethodReturn(Object o,
                               String className,
                               String methodName,
                               String argsType,
                               String returnType,
                               Object... args) {
    }

}
