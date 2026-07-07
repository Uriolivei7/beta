package com.facebook.react.devsupport;

import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.interfaces.exceptionmanager.ReactJsExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class l0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Pattern f6764a = Pattern.compile("^(?:(.*?)@)?(.*?)\\:([0-9]+)\\:([0-9]+)$");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Pattern f6765b = Pattern.compile("\\s*(?:at)\\s*(.+?)\\s*[@(](.*):([0-9]+):([0-9]+)[)]$");

    public static class a implements k1.j {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f6766a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final String f6767b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f6768c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f6769d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final String f6770e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final boolean f6771f;

        @Override // k1.j
        public String a() {
            return this.f6770e;
        }

        @Override // k1.j
        public boolean b() {
            return this.f6771f;
        }

        @Override // k1.j
        public int c() {
            return this.f6768c;
        }

        @Override // k1.j
        public String d() {
            return this.f6767b;
        }

        @Override // k1.j
        public int getColumn() {
            return this.f6769d;
        }

        @Override // k1.j
        public String getFile() {
            return this.f6766a;
        }

        private a(String str, String str2, String str3, int i3, int i4) {
            this.f6766a = str;
            this.f6770e = str2;
            this.f6767b = str3;
            this.f6768c = i3;
            this.f6769d = i4;
            this.f6771f = false;
        }
    }

    public static k1.j[] a(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        k1.j[] jVarArr = new k1.j[stackTrace.length];
        for (int i3 = 0; i3 < stackTrace.length; i3++) {
            jVarArr[i3] = new a(stackTrace[i3].getClassName(), stackTrace[i3].getFileName(), stackTrace[i3].getMethodName(), stackTrace[i3].getLineNumber(), -1);
        }
        return jVarArr;
    }

    public static JavaOnlyMap b(ReactJsExceptionHandler.ProcessedError processedError) {
        List<ReactJsExceptionHandler.ProcessedError.StackFrame> stack = processedError.getStack();
        ArrayList arrayList = new ArrayList();
        for (ReactJsExceptionHandler.ProcessedError.StackFrame stackFrame : stack) {
            JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
            if (stackFrame.getColumn() != null) {
                javaOnlyMap.putDouble("column", stackFrame.getColumn().intValue());
            }
            if (stackFrame.getLineNumber() != null) {
                javaOnlyMap.putDouble("lineNumber", stackFrame.getLineNumber().intValue());
            }
            javaOnlyMap.putString("file", stackFrame.getFile());
            javaOnlyMap.putString("methodName", stackFrame.getMethodName());
            arrayList.add(javaOnlyMap);
        }
        JavaOnlyMap javaOnlyMap2 = new JavaOnlyMap();
        javaOnlyMap2.putString("message", processedError.getMessage());
        if (processedError.getOriginalMessage() != null) {
            javaOnlyMap2.putString("originalMessage", processedError.getOriginalMessage());
        }
        if (processedError.getName() != null) {
            javaOnlyMap2.putString("name", processedError.getName());
        }
        if (processedError.getComponentStack() != null) {
            javaOnlyMap2.putString("componentStack", processedError.getComponentStack());
        }
        javaOnlyMap2.putArray("stack", JavaOnlyArray.from(arrayList));
        javaOnlyMap2.putInt("id", processedError.getId());
        javaOnlyMap2.putBoolean("isFatal", processedError.isFatal());
        javaOnlyMap2.putMap("extraData", processedError.getExtraData());
        return javaOnlyMap2;
    }

    public static String c(k1.j jVar) {
        StringBuilder sb = new StringBuilder();
        sb.append(jVar.a());
        int iC = jVar.c();
        if (iC > 0) {
            sb.append(":");
            sb.append(iC);
            int column = jVar.getColumn();
            if (column > 0) {
                sb.append(":");
                sb.append(column);
            }
        }
        return sb.toString();
    }
}
