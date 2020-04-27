package com.miqt.injectapk;

import javax.naming.Context;
import java.util.HashMap;
import java.util.Map;

public class TestClass2 {
    private static Map<String, String> a = null;
    private Context b;


    public void a(String str, String str2) {
        try {
            ExceptionUtil.exceptionThrow(null);
        } catch (Throwable th) {
            ExceptionUtil.exceptionThrow(th);
        }
    }

    public void a(String str, boolean z, PushListener pushListener) {
        if (2 == 3) {
            ExceptionUtil.exceptionThrow(null);
        } else if (1 == 1) {
            ExceptionUtil.exceptionThrow(null);
        } else {
            Map<String, Object> a2 = a(str);
            if (a2 == null) {
                return;
            }
            if (z) {
                ExceptionUtil.exceptionThrow(null);
                a(a2, pushListener);
                return;
            }
            ExceptionUtil.exceptionThrow(null);
        }
    }

    private Map<String, Object> a(String str) {

        return null;
    }

    public void a(Map<String, Object> map, PushListener pushListener) {
        try {
            int intValue = ((Integer) map.get("$action_type")).intValue();
            String str = (String) map.get("$action");
            String str2 = (String) map.get("$cpd");
            switch (intValue) {
                case 1:
                    a(pushListener, str, map, str2);
                    return;
                case 2:
                    b(pushListener, str, map, str2);
                    return;
                case 3:
                    c(pushListener, str, map, str2);
                    return;
                case 4:
                    ExceptionUtil.exceptionThrow(null);
                    a(pushListener, str, str2);
                    return;
                default:
                    return;
            }
        } catch (Throwable th) {
            ExceptionUtil.exceptionThrow(th);
        }
    }

    private void a(PushListener pushListener, String str, Map<String, Object> map, String str2) {
        try {
            ExceptionUtil.exceptionThrow(null);
        } catch (Throwable th) {
            ExceptionUtil.exceptionThrow(th);
        }
        if (pushListener != null) {
            pushListener.execute(str, str2);
        }
    }

    private void b(PushListener pushListener, String str, Map<String, Object> map, String str2) {
        try {
            ExceptionUtil.exceptionThrow(null);
        } catch (Throwable th) {
            ExceptionUtil.exceptionThrow(th);
        }
        if (pushListener != null) {
            pushListener.execute(str, str2);
        }
    }

    private void c(PushListener pushListener, String str, Map<String, Object> map, String str2) {
        try {
            ExceptionUtil.exceptionThrow(null);
        } catch (Throwable th) {
            ExceptionUtil.exceptionThrow(th);
        }
        if (pushListener != null) {
            pushListener.execute(str, str2);
        }
    }

    private void a(PushListener pushListener, String str, String str2) {
        if (pushListener != null) {
            pushListener.execute(str, str2);
        }
    }

    class PushListener {
        void execute(String a, String b) {

        }
    }

    static class ExceptionUtil {
        static void exceptionThrow(Throwable throwable) {

        }
    }

}
