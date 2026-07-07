package com.facebook.soloader;

import android.os.StrictMode;
import b2.C0317b;
import com.facebook.soloader.s;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public final class t {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final int f8257a = 3;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final int f8258b = 3;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static volatile boolean f8260d = false;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static byte[] f8261e = null;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static List f8262f = null;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private static Map f8263g = null;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private static volatile boolean f8264h = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final int f8259c = 3 + 3;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private static final ReentrantReadWriteLock f8265i = new ReentrantReadWriteLock();

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private static final HashSet f8266j = new a();

    class a extends HashSet {
        a() {
            add("libEGL.so");
            add("libGLESv2.so");
            add("libGLESv3.so");
            add("libOpenSLES.so");
            add("libandroid.so");
            add("libc.so");
            add("libdl.so");
            add("libjnigraphics.so");
            add("liblog.so");
            add("libm.so");
            add("libstdc++.so");
            add("libz.so");
        }
    }

    private static String[] a(String str) {
        if (f8260d) {
            return i(str);
        }
        if (!f8264h) {
            return null;
        }
        ReentrantReadWriteLock reentrantReadWriteLock = f8265i;
        reentrantReadWriteLock.readLock().lock();
        try {
            String[] strArrI = i(str);
            reentrantReadWriteLock.readLock().unlock();
            return strArrI;
        } catch (Throwable th) {
            f8265i.readLock().unlock();
            throw th;
        }
    }

    public static String[] b(String str, h hVar) {
        boolean z3 = SoLoader.f8203a;
        if (z3) {
            Api18TraceUtils.a("soloader.NativeDeps.getDependencies[", str, "]");
        }
        C0317b.b();
        try {
            try {
                String[] strArrA = a(str);
                if (strArrA != null) {
                    C0317b.a(null);
                    if (z3) {
                        Api18TraceUtils.b();
                    }
                    return strArrA;
                }
                String[] strArrA2 = s.a(hVar);
                C0317b.a(null);
                if (z3) {
                    Api18TraceUtils.b();
                }
                return strArrA2;
            } catch (s.a e4) {
                throw D.b(str, e4);
            } catch (Error e5) {
                e = e5;
                throw e;
            } catch (RuntimeException e6) {
                e = e6;
                throw e;
            }
        } catch (Throwable th) {
            C0317b.a(null);
            if (SoLoader.f8203a) {
                Api18TraceUtils.b();
            }
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x003e, code lost:
    
        if (r2 == false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0040, code lost:
    
        r6 = d(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0044, code lost:
    
        if (r6 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0046, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0047, code lost:
    
        r0.add(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x004e, code lost:
    
        if (r0.isEmpty() == false) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0050, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005d, code lost:
    
        return (java.lang.String[]) r0.toArray(new java.lang.String[r0.size()]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String[] c(int r6, int r7) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            int r6 = r6 + r7
            int r7 = com.facebook.soloader.t.f8259c
            int r6 = r6 - r7
            r7 = 0
            r1 = r7
            r2 = r1
        Lc:
            byte[] r3 = com.facebook.soloader.t.f8261e
            int r4 = r3.length
            r5 = 0
            if (r6 >= r4) goto L3e
            r3 = r3[r6]
            r4 = 10
            if (r3 == r4) goto L3e
            r4 = 32
            if (r3 != r4) goto L2b
            if (r2 == 0) goto L3a
            java.lang.String r1 = d(r1)
            if (r1 != 0) goto L25
            return r5
        L25:
            r0.add(r1)
            r1 = r7
            r2 = r1
            goto L3a
        L2b:
            r2 = 48
            if (r3 < r2) goto L3d
            r2 = 57
            if (r3 <= r2) goto L34
            goto L3d
        L34:
            int r1 = r1 * 10
            int r3 = r3 + (-48)
            int r1 = r1 + r3
            r2 = 1
        L3a:
            int r6 = r6 + 1
            goto Lc
        L3d:
            return r5
        L3e:
            if (r2 == 0) goto L4a
            java.lang.String r6 = d(r1)
            if (r6 != 0) goto L47
            return r5
        L47:
            r0.add(r6)
        L4a:
            boolean r6 = r0.isEmpty()
            if (r6 == 0) goto L51
            return r5
        L51:
            int r6 = r0.size()
            java.lang.String[] r6 = new java.lang.String[r6]
            java.lang.Object[] r6 = r0.toArray(r6)
            java.lang.String[] r6 = (java.lang.String[]) r6
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.t.c(int, int):java.lang.String[]");
    }

    private static String d(int i3) {
        if (i3 >= f8262f.size()) {
            return null;
        }
        int iIntValue = ((Integer) f8262f.get(i3)).intValue();
        int i4 = iIntValue;
        while (true) {
            byte[] bArr = f8261e;
            if (i4 >= bArr.length || bArr[i4] <= 32) {
                break;
            }
            i4++;
        }
        int i5 = (i4 - iIntValue) + f8259c;
        char[] cArr = new char[i5];
        cArr[0] = 'l';
        cArr[1] = 'i';
        cArr[2] = 'b';
        for (int i6 = 0; i6 < i5 - f8259c; i6++) {
            cArr[f8257a + i6] = (char) f8261e[iIntValue + i6];
        }
        cArr[i5 - 3] = '.';
        cArr[i5 - 2] = 's';
        cArr[i5 - 1] = 'o';
        return new String(cArr);
    }

    private static int e(String str) {
        List list = (List) f8263g.get(Integer.valueOf(f(str)));
        if (list == null) {
            return -1;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            if (g(str, iIntValue)) {
                return iIntValue;
            }
        }
        return -1;
    }

    private static int f(String str) {
        int iCodePointAt = 5381;
        for (int i3 = f8257a; i3 < str.length() - f8258b; i3++) {
            iCodePointAt = str.codePointAt(i3) + (iCodePointAt << 5) + iCodePointAt;
        }
        return iCodePointAt;
    }

    private static boolean g(String str, int i3) {
        int i4;
        int i5 = f8257a;
        while (true) {
            int length = str.length();
            i4 = f8258b;
            if (i5 >= length - i4 || i3 >= f8261e.length || (str.codePointAt(i5) & 255) != f8261e[i3]) {
                break;
            }
            i5++;
            i3++;
        }
        return i5 == str.length() - i4;
    }

    public static void h(String str, h hVar, int i3, StrictMode.ThreadPolicy threadPolicy) {
        String[] strArrB = b(str, hVar);
        p.a("SoLoader", "Loading " + str + "'s dependencies: " + Arrays.toString(strArrB));
        for (String str2 : strArrB) {
            if (!str2.startsWith("/") && !f8266j.contains(str2)) {
                SoLoader.s(str2, i3, threadPolicy);
            }
        }
    }

    static String[] i(String str) {
        int iE;
        if (f8260d && str.length() > f8259c && (iE = e(str)) != -1) {
            return c(iE, str.length());
        }
        return null;
    }
}
