package com.facebook.react.bridge;

import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class ReactCxxErrorHandler {
    private static Method mHandleErrorFunc;
    private static Object mObject;

    private static void handleError(String str) {
        if (mHandleErrorFunc != null) {
            try {
                mHandleErrorFunc.invoke(mObject, new Exception(str));
            } catch (Exception e4) {
                Y.a.n("ReactCxxErrorHandler", "Failed to invoke error handler function", e4);
            }
        }
    }

    public static void setHandleErrorFunc(Object obj, Method method) {
        mObject = obj;
        mHandleErrorFunc = method;
    }
}
