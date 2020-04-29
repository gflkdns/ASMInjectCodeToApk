package com.miqt.pluginlib.tools;


public class SampleTimePrint implements ITimePrint {
    @Override
    public void onMethodEnter(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {
    }

    @Override
    public void onMethodReturn(Object returnObj, Object thisObj, String className, String methodName, String argsType, String returnType) {
    }
}
