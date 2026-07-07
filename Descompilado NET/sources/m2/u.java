package M2;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class u {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f1229a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f1230b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f1231c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f1232d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final String f1233e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final int f1234f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final List f1235g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final List f1236h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final String f1237i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private final String f1238j;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    public static final b f1228l = new b(null);

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final char[] f1227k = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final class a {

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public static final C0022a f1239i = new C0022a(null);

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f1240a;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private String f1243d;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final List f1245f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private List f1246g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private String f1247h;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f1241b = "";

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String f1242c = "";

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f1244e = -1;

        /* JADX INFO: renamed from: M2.u$a$a, reason: collision with other inner class name */
        public static final class C0022a {
            private C0022a() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int e(String str, int i3, int i4) {
                try {
                    int i5 = Integer.parseInt(b.b(u.f1228l, str, i3, i4, "", false, false, false, false, null, 248, null));
                    if (1 <= i5 && 65535 >= i5) {
                        return i5;
                    }
                    return -1;
                } catch (NumberFormatException unused) {
                    return -1;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int f(String str, int i3, int i4) {
                while (i3 < i4) {
                    char cCharAt = str.charAt(i3);
                    if (cCharAt == ':') {
                        return i3;
                    }
                    if (cCharAt == '[') {
                        do {
                            i3++;
                            if (i3 < i4) {
                            }
                        } while (str.charAt(i3) != ']');
                    }
                    i3++;
                }
                return i4;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int g(String str, int i3, int i4) {
                if (i4 - i3 < 2) {
                    return -1;
                }
                char cCharAt = str.charAt(i3);
                if ((D2.h.g(cCharAt, 97) < 0 || D2.h.g(cCharAt, 122) > 0) && (D2.h.g(cCharAt, 65) < 0 || D2.h.g(cCharAt, 90) > 0)) {
                    return -1;
                }
                while (true) {
                    i3++;
                    if (i3 >= i4) {
                        return -1;
                    }
                    char cCharAt2 = str.charAt(i3);
                    if ('a' > cCharAt2 || 'z' < cCharAt2) {
                        if ('A' > cCharAt2 || 'Z' < cCharAt2) {
                            if ('0' > cCharAt2 || '9' < cCharAt2) {
                                if (cCharAt2 != '+' && cCharAt2 != '-' && cCharAt2 != '.') {
                                    if (cCharAt2 == ':') {
                                        return i3;
                                    }
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public final int h(String str, int i3, int i4) {
                int i5 = 0;
                while (i3 < i4) {
                    char cCharAt = str.charAt(i3);
                    if (cCharAt != '\\' && cCharAt != '/') {
                        break;
                    }
                    i5++;
                    i3++;
                }
                return i5;
            }

            public /* synthetic */ C0022a(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }

        public a() {
            ArrayList arrayList = new ArrayList();
            this.f1245f = arrayList;
            arrayList.add("");
        }

        private final int b() {
            int i3 = this.f1244e;
            if (i3 != -1) {
                return i3;
            }
            b bVar = u.f1228l;
            String str = this.f1240a;
            D2.h.c(str);
            return bVar.c(str);
        }

        private final boolean f(String str) {
            return D2.h.b(str, ".") || K2.o.n(str, "%2e", true);
        }

        private final boolean g(String str) {
            return D2.h.b(str, "..") || K2.o.n(str, "%2e.", true) || K2.o.n(str, ".%2e", true) || K2.o.n(str, "%2e%2e", true);
        }

        private final void j() {
            if (((String) this.f1245f.remove(r0.size() - 1)).length() != 0 || this.f1245f.isEmpty()) {
                this.f1245f.add("");
            } else {
                this.f1245f.set(r0.size() - 1, "");
            }
        }

        private final void l(String str, int i3, int i4, boolean z3, boolean z4) {
            String strB = b.b(u.f1228l, str, i3, i4, " \"<>^`{}|/\\?#", z4, false, false, false, null, 240, null);
            if (f(strB)) {
                return;
            }
            if (g(strB)) {
                j();
                return;
            }
            if (((CharSequence) this.f1245f.get(r2.size() - 1)).length() == 0) {
                this.f1245f.set(r2.size() - 1, strB);
            } else {
                this.f1245f.add(strB);
            }
            if (z3) {
                this.f1245f.add("");
            }
        }

        private final void n(String str, int i3, int i4) {
            if (i3 == i4) {
                return;
            }
            char cCharAt = str.charAt(i3);
            if (cCharAt == '/' || cCharAt == '\\') {
                this.f1245f.clear();
                this.f1245f.add("");
                i3++;
            } else {
                List list = this.f1245f;
                list.set(list.size() - 1, "");
            }
            while (true) {
                int i5 = i3;
                if (i5 >= i4) {
                    return;
                }
                i3 = N2.c.n(str, "/\\", i5, i4);
                boolean z3 = i3 < i4;
                l(str, i5, i3, z3, true);
                if (z3) {
                    i3++;
                }
            }
        }

        public final u a() {
            ArrayList arrayList;
            String str = this.f1240a;
            if (str == null) {
                throw new IllegalStateException("scheme == null");
            }
            b bVar = u.f1228l;
            String strG = b.g(bVar, this.f1241b, 0, 0, false, 7, null);
            String strG2 = b.g(bVar, this.f1242c, 0, 0, false, 7, null);
            String str2 = this.f1243d;
            if (str2 == null) {
                throw new IllegalStateException("host == null");
            }
            int iB = b();
            List list = this.f1245f;
            ArrayList arrayList2 = new ArrayList(AbstractC0717n.q(list, 10));
            Iterator it = list.iterator();
            while (it.hasNext()) {
                arrayList2.add(b.g(u.f1228l, (String) it.next(), 0, 0, false, 7, null));
            }
            List<String> list2 = this.f1246g;
            if (list2 != null) {
                arrayList = new ArrayList(AbstractC0717n.q(list2, 10));
                for (String str3 : list2) {
                    arrayList.add(str3 != null ? b.g(u.f1228l, str3, 0, 0, true, 3, null) : null);
                }
            } else {
                arrayList = null;
            }
            String str4 = this.f1247h;
            return new u(str, strG, strG2, str2, iB, arrayList2, arrayList, str4 != null ? b.g(u.f1228l, str4, 0, 0, false, 7, null) : null, toString());
        }

        /* JADX WARN: Removed duplicated region for block: B:6:0x001d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final M2.u.a c(java.lang.String r14) {
            /*
                r13 = this;
                if (r14 == 0) goto L1d
                M2.u$b r12 = M2.u.f1228l
                r10 = 211(0xd3, float:2.96E-43)
                r11 = 0
                r2 = 0
                r3 = 0
                java.lang.String r4 = " \"'<>#"
                r5 = 1
                r6 = 0
                r7 = 1
                r8 = 0
                r9 = 0
                r0 = r12
                r1 = r14
                java.lang.String r14 = M2.u.b.b(r0, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
                if (r14 == 0) goto L1d
                java.util.List r14 = r12.i(r14)
                goto L1e
            L1d:
                r14 = 0
            L1e:
                r13.f1246g = r14
                return r13
            */
            throw new UnsupportedOperationException("Method not decompiled: M2.u.a.c(java.lang.String):M2.u$a");
        }

        public final List d() {
            return this.f1245f;
        }

        public final a e(String str) {
            D2.h.f(str, "host");
            String strE = N2.a.e(b.g(u.f1228l, str, 0, 0, false, 7, null));
            if (strE != null) {
                this.f1243d = strE;
                return this;
            }
            throw new IllegalArgumentException("unexpected host: " + str);
        }

        public final a h(u uVar, String str) {
            int iN;
            int i3;
            int i4;
            boolean z3;
            int i5;
            String str2;
            int i6;
            boolean z4;
            boolean z5;
            D2.h.f(str, "input");
            int iX = N2.c.x(str, 0, 0, 3, null);
            int iZ = N2.c.z(str, iX, 0, 2, null);
            C0022a c0022a = f1239i;
            int iG = c0022a.g(str, iX, iZ);
            String str3 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
            boolean z6 = true;
            byte b4 = -1;
            if (iG != -1) {
                if (K2.o.w(str, "https:", iX, true)) {
                    this.f1240a = "https";
                    iX += 6;
                } else {
                    if (!K2.o.w(str, "http:", iX, true)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Expected URL scheme 'http' or 'https' but was '");
                        String strSubstring = str.substring(0, iG);
                        D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                        sb.append(strSubstring);
                        sb.append("'");
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.f1240a = "http";
                    iX += 5;
                }
            } else {
                if (uVar == null) {
                    throw new IllegalArgumentException("Expected URL scheme 'http' or 'https' but no colon was found");
                }
                this.f1240a = uVar.p();
            }
            int iH = c0022a.h(str, iX, iZ);
            byte b5 = 63;
            byte b6 = 35;
            if (iH >= 2 || uVar == null || !D2.h.b(uVar.p(), this.f1240a)) {
                int i7 = iX + iH;
                boolean z7 = false;
                boolean z8 = false;
                while (true) {
                    iN = N2.c.n(str, "@/\\?#", i7, iZ);
                    byte bCharAt = iN != iZ ? str.charAt(iN) : b4;
                    if (bCharAt == b4 || bCharAt == b6 || bCharAt == 47 || bCharAt == 92 || bCharAt == b5) {
                        break;
                    }
                    if (bCharAt != 64) {
                        z3 = z6;
                        i5 = iZ;
                        str2 = str3;
                    } else {
                        if (z7) {
                            z3 = z6;
                            i5 = iZ;
                            str2 = str3;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(this.f1242c);
                            sb2.append("%40");
                            i6 = iN;
                            sb2.append(b.b(u.f1228l, str, i7, iN, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null));
                            this.f1242c = sb2.toString();
                            z4 = z8;
                        } else {
                            int iM = N2.c.m(str, ':', i7, iN);
                            b bVar = u.f1228l;
                            z3 = z6;
                            i5 = iZ;
                            str2 = str3;
                            String strB = b.b(bVar, str, i7, iM, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                            if (z8) {
                                strB = this.f1241b + "%40" + strB;
                            }
                            this.f1241b = strB;
                            i6 = iN;
                            if (iM != i6) {
                                this.f1242c = b.b(bVar, str, iM + 1, i6, " \"':;<=>@[]^`{}|/\\?#", true, false, false, false, null, 240, null);
                                z5 = z3;
                            } else {
                                z5 = z7;
                            }
                            z7 = z5;
                            z4 = z3;
                        }
                        i7 = i6 + 1;
                        z8 = z4;
                    }
                    str3 = str2;
                    iZ = i5;
                    z6 = z3;
                    b6 = 35;
                    b5 = 63;
                    b4 = -1;
                }
                boolean z9 = z6;
                i3 = iZ;
                String str4 = str3;
                C0022a c0022a2 = f1239i;
                int iF = c0022a2.f(str, i7, iN);
                int i8 = iF + 1;
                if (i8 < iN) {
                    i4 = i7;
                    this.f1243d = N2.a.e(b.g(u.f1228l, str, i7, iF, false, 4, null));
                    int iE = c0022a2.e(str, i8, iN);
                    this.f1244e = iE;
                    if (!(iE != -1 ? z9 : false)) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Invalid URL port: \"");
                        String strSubstring2 = str.substring(i8, iN);
                        D2.h.e(strSubstring2, str4);
                        sb3.append(strSubstring2);
                        sb3.append('\"');
                        throw new IllegalArgumentException(sb3.toString().toString());
                    }
                } else {
                    i4 = i7;
                    b bVar2 = u.f1228l;
                    this.f1243d = N2.a.e(b.g(bVar2, str, i4, iF, false, 4, null));
                    String str5 = this.f1240a;
                    D2.h.c(str5);
                    this.f1244e = bVar2.c(str5);
                }
                if (!(this.f1243d != null ? z9 : false)) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Invalid URL host: \"");
                    String strSubstring3 = str.substring(i4, iF);
                    D2.h.e(strSubstring3, str4);
                    sb4.append(strSubstring3);
                    sb4.append('\"');
                    throw new IllegalArgumentException(sb4.toString().toString());
                }
                iX = iN;
            } else {
                this.f1241b = uVar.g();
                this.f1242c = uVar.c();
                this.f1243d = uVar.h();
                this.f1244e = uVar.l();
                this.f1245f.clear();
                this.f1245f.addAll(uVar.e());
                if (iX == iZ || str.charAt(iX) == '#') {
                    c(uVar.f());
                }
                i3 = iZ;
            }
            int i9 = i3;
            int iN2 = N2.c.n(str, "?#", iX, i9);
            n(str, iX, iN2);
            if (iN2 < i9 && str.charAt(iN2) == '?') {
                int iM2 = N2.c.m(str, '#', iN2, i9);
                b bVar3 = u.f1228l;
                this.f1246g = bVar3.i(b.b(bVar3, str, iN2 + 1, iM2, " \"'<>#", true, false, true, false, null, 208, null));
                iN2 = iM2;
            }
            if (iN2 < i9 && str.charAt(iN2) == '#') {
                this.f1247h = b.b(u.f1228l, str, iN2 + 1, i9, "", true, false, false, true, null, 176, null);
            }
            return this;
        }

        public final a i(String str) {
            D2.h.f(str, "password");
            this.f1242c = b.b(u.f1228l, str, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, null, 251, null);
            return this;
        }

        public final a k(int i3) {
            if (1 <= i3 && 65535 >= i3) {
                this.f1244e = i3;
                return this;
            }
            throw new IllegalArgumentException(("unexpected port: " + i3).toString());
        }

        public final a m() {
            String str = this.f1243d;
            this.f1243d = str != null ? new K2.k("[\"<>^`{|}]").c(str, "") : null;
            int size = this.f1245f.size();
            for (int i3 = 0; i3 < size; i3++) {
                List list = this.f1245f;
                list.set(i3, b.b(u.f1228l, (String) list.get(i3), 0, 0, "[]", true, true, false, false, null, 227, null));
            }
            List list2 = this.f1246g;
            if (list2 != null) {
                int size2 = list2.size();
                for (int i4 = 0; i4 < size2; i4++) {
                    String str2 = (String) list2.get(i4);
                    list2.set(i4, str2 != null ? b.b(u.f1228l, str2, 0, 0, "\\^`{|}", true, true, true, false, null, 195, null) : null);
                }
            }
            String str3 = this.f1247h;
            this.f1247h = str3 != null ? b.b(u.f1228l, str3, 0, 0, " \"#<>\\^`{|}", true, true, false, true, null, 163, null) : null;
            return this;
        }

        public final a o(String str) {
            D2.h.f(str, "scheme");
            if (K2.o.n(str, "http", true)) {
                this.f1240a = "http";
            } else {
                if (!K2.o.n(str, "https", true)) {
                    throw new IllegalArgumentException("unexpected scheme: " + str);
                }
                this.f1240a = "https";
            }
            return this;
        }

        public final void p(String str) {
            this.f1247h = str;
        }

        public final void q(String str) {
            D2.h.f(str, "<set-?>");
            this.f1242c = str;
        }

        public final void r(String str) {
            D2.h.f(str, "<set-?>");
            this.f1241b = str;
        }

        public final void s(String str) {
            this.f1243d = str;
        }

        public final void t(int i3) {
            this.f1244e = i3;
        }

        /* JADX WARN: Removed duplicated region for block: B:29:0x0085  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String toString() {
            /*
                r6 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = r6.f1240a
                if (r1 == 0) goto L12
                r0.append(r1)
                java.lang.String r1 = "://"
                r0.append(r1)
                goto L17
            L12:
                java.lang.String r1 = "//"
                r0.append(r1)
            L17:
                java.lang.String r1 = r6.f1241b
                int r1 = r1.length()
                r2 = 58
                if (r1 <= 0) goto L22
                goto L2a
            L22:
                java.lang.String r1 = r6.f1242c
                int r1 = r1.length()
                if (r1 <= 0) goto L44
            L2a:
                java.lang.String r1 = r6.f1241b
                r0.append(r1)
                java.lang.String r1 = r6.f1242c
                int r1 = r1.length()
                if (r1 <= 0) goto L3f
                r0.append(r2)
                java.lang.String r1 = r6.f1242c
                r0.append(r1)
            L3f:
                r1 = 64
                r0.append(r1)
            L44:
                java.lang.String r1 = r6.f1243d
                if (r1 == 0) goto L69
                D2.h.c(r1)
                r3 = 2
                r4 = 0
                r5 = 0
                boolean r1 = K2.o.D(r1, r2, r5, r3, r4)
                if (r1 == 0) goto L64
                r1 = 91
                r0.append(r1)
                java.lang.String r1 = r6.f1243d
                r0.append(r1)
                r1 = 93
                r0.append(r1)
                goto L69
            L64:
                java.lang.String r1 = r6.f1243d
                r0.append(r1)
            L69:
                int r1 = r6.f1244e
                r3 = -1
                if (r1 != r3) goto L72
                java.lang.String r1 = r6.f1240a
                if (r1 == 0) goto L8b
            L72:
                int r1 = r6.b()
                java.lang.String r3 = r6.f1240a
                if (r3 == 0) goto L85
                M2.u$b r4 = M2.u.f1228l
                D2.h.c(r3)
                int r3 = r4.c(r3)
                if (r1 == r3) goto L8b
            L85:
                r0.append(r2)
                r0.append(r1)
            L8b:
                M2.u$b r1 = M2.u.f1228l
                java.util.List r2 = r6.f1245f
                r1.h(r2, r0)
                java.util.List r2 = r6.f1246g
                if (r2 == 0) goto La3
                r2 = 63
                r0.append(r2)
                java.util.List r2 = r6.f1246g
                D2.h.c(r2)
                r1.j(r2, r0)
            La3:
                java.lang.String r1 = r6.f1247h
                if (r1 == 0) goto Lb1
                r1 = 35
                r0.append(r1)
                java.lang.String r1 = r6.f1247h
                r0.append(r1)
            Lb1:
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "StringBuilder().apply(builderAction).toString()"
                D2.h.e(r0, r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: M2.u.a.toString():java.lang.String");
        }

        public final void u(String str) {
            this.f1240a = str;
        }

        public final a v(String str) {
            D2.h.f(str, "username");
            this.f1241b = b.b(u.f1228l, str, 0, 0, " \"':;<=>@[]^`{}|/\\?#", false, false, false, false, null, 251, null);
            return this;
        }
    }

    public static final class b {
        private b() {
        }

        public static /* synthetic */ String b(b bVar, String str, int i3, int i4, String str2, boolean z3, boolean z4, boolean z5, boolean z6, Charset charset, int i5, Object obj) {
            return bVar.a(str, (i5 & 1) != 0 ? 0 : i3, (i5 & 2) != 0 ? str.length() : i4, str2, (i5 & 8) != 0 ? false : z3, (i5 & 16) != 0 ? false : z4, (i5 & 32) != 0 ? false : z5, (i5 & 64) != 0 ? false : z6, (i5 & 128) != 0 ? null : charset);
        }

        private final boolean e(String str, int i3, int i4) {
            int i5 = i3 + 2;
            return i5 < i4 && str.charAt(i3) == '%' && N2.c.F(str.charAt(i3 + 1)) != -1 && N2.c.F(str.charAt(i5)) != -1;
        }

        public static /* synthetic */ String g(b bVar, String str, int i3, int i4, boolean z3, int i5, Object obj) {
            if ((i5 & 1) != 0) {
                i3 = 0;
            }
            if ((i5 & 2) != 0) {
                i4 = str.length();
            }
            if ((i5 & 4) != 0) {
                z3 = false;
            }
            return bVar.f(str, i3, i4, z3);
        }

        /* JADX WARN: Removed duplicated region for block: B:41:0x006a  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x006c  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x0074  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x008d  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x0096 A[LOOP:1: B:52:0x0090->B:54:0x0096, LOOP_END] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private final void k(b3.i r15, java.lang.String r16, int r17, int r18, java.lang.String r19, boolean r20, boolean r21, boolean r22, boolean r23, java.nio.charset.Charset r24) throws java.io.EOFException {
            /*
                Method dump skipped, instruction units count: 202
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: M2.u.b.k(b3.i, java.lang.String, int, int, java.lang.String, boolean, boolean, boolean, boolean, java.nio.charset.Charset):void");
        }

        private final void l(b3.i iVar, String str, int i3, int i4, boolean z3) {
            int i5;
            while (i3 < i4) {
                if (str == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                int iCodePointAt = str.codePointAt(i3);
                if (iCodePointAt == 37 && (i5 = i3 + 2) < i4) {
                    int iF = N2.c.F(str.charAt(i3 + 1));
                    int iF2 = N2.c.F(str.charAt(i5));
                    if (iF == -1 || iF2 == -1) {
                        iVar.V0(iCodePointAt);
                        i3 += Character.charCount(iCodePointAt);
                    } else {
                        iVar.L((iF << 4) + iF2);
                        i3 = Character.charCount(iCodePointAt) + i5;
                    }
                } else if (iCodePointAt == 43 && z3) {
                    iVar.L(32);
                    i3++;
                } else {
                    iVar.V0(iCodePointAt);
                    i3 += Character.charCount(iCodePointAt);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:22:0x0041  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.lang.String a(java.lang.String r14, int r15, int r16, java.lang.String r17, boolean r18, boolean r19, boolean r20, boolean r21, java.nio.charset.Charset r22) throws java.io.EOFException {
            /*
                r13 = this;
                r2 = r14
                r4 = r16
                r5 = r17
                java.lang.String r0 = "$this$canonicalize"
                D2.h.f(r14, r0)
                java.lang.String r0 = "encodeSet"
                D2.h.f(r5, r0)
                r3 = r15
            L10:
                if (r3 >= r4) goto L71
                int r0 = r14.codePointAt(r3)
                r1 = 32
                if (r0 < r1) goto L25
                r1 = 127(0x7f, float:1.78E-43)
                if (r0 == r1) goto L25
                r1 = 128(0x80, float:1.8E-43)
                if (r0 < r1) goto L27
                if (r21 == 0) goto L25
                goto L27
            L25:
                r11 = r13
                goto L4f
            L27:
                char r1 = (char) r0
                r6 = 2
                r7 = 0
                r8 = 0
                boolean r1 = K2.o.D(r5, r1, r8, r6, r7)
                if (r1 != 0) goto L25
                r1 = 37
                if (r0 != r1) goto L41
                if (r18 == 0) goto L25
                if (r19 == 0) goto L41
                r11 = r13
                boolean r1 = r13.e(r14, r3, r4)
                if (r1 == 0) goto L4f
                goto L42
            L41:
                r11 = r13
            L42:
                r1 = 43
                if (r0 != r1) goto L49
                if (r20 == 0) goto L49
                goto L4f
            L49:
                int r0 = java.lang.Character.charCount(r0)
                int r3 = r3 + r0
                goto L10
            L4f:
                b3.i r12 = new b3.i
                r12.<init>()
                r0 = r15
                r12.U0(r14, r15, r3)
                r0 = r13
                r1 = r12
                r2 = r14
                r4 = r16
                r5 = r17
                r6 = r18
                r7 = r19
                r8 = r20
                r9 = r21
                r10 = r22
                r0.k(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
                java.lang.String r0 = r12.O()
                return r0
            L71:
                r11 = r13
                r0 = r15
                java.lang.String r0 = r14.substring(r15, r16)
                java.lang.String r1 = "(this as java.lang.Strin…ing(startIndex, endIndex)"
                D2.h.e(r0, r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: M2.u.b.a(java.lang.String, int, int, java.lang.String, boolean, boolean, boolean, boolean, java.nio.charset.Charset):java.lang.String");
        }

        public final int c(String str) {
            D2.h.f(str, "scheme");
            int iHashCode = str.hashCode();
            if (iHashCode != 3213448) {
                if (iHashCode == 99617003 && str.equals("https")) {
                    return 443;
                }
            } else if (str.equals("http")) {
                return 80;
            }
            return -1;
        }

        public final u d(String str) {
            D2.h.f(str, "$this$toHttpUrl");
            return new a().h(null, str).a();
        }

        public final String f(String str, int i3, int i4, boolean z3) {
            D2.h.f(str, "$this$percentDecode");
            for (int i5 = i3; i5 < i4; i5++) {
                char cCharAt = str.charAt(i5);
                if (cCharAt == '%' || (cCharAt == '+' && z3)) {
                    b3.i iVar = new b3.i();
                    iVar.U0(str, i3, i5);
                    l(iVar, str, i5, i4, z3);
                    return iVar.O();
                }
            }
            String strSubstring = str.substring(i3, i4);
            D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return strSubstring;
        }

        public final void h(List list, StringBuilder sb) {
            D2.h.f(list, "$this$toPathString");
            D2.h.f(sb, "out");
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                sb.append('/');
                sb.append((String) list.get(i3));
            }
        }

        public final List i(String str) {
            D2.h.f(str, "$this$toQueryNamesAndValues");
            ArrayList arrayList = new ArrayList();
            int i3 = 0;
            while (i3 <= str.length()) {
                int iN = K2.o.N(str, '&', i3, false, 4, null);
                if (iN == -1) {
                    iN = str.length();
                }
                int i4 = iN;
                int iN2 = K2.o.N(str, '=', i3, false, 4, null);
                if (iN2 == -1 || iN2 > i4) {
                    String strSubstring = str.substring(i3, i4);
                    D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    arrayList.add(strSubstring);
                    arrayList.add(null);
                } else {
                    String strSubstring2 = str.substring(i3, iN2);
                    D2.h.e(strSubstring2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    arrayList.add(strSubstring2);
                    String strSubstring3 = str.substring(iN2 + 1, i4);
                    D2.h.e(strSubstring3, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    arrayList.add(strSubstring3);
                }
                i3 = i4 + 1;
            }
            return arrayList;
        }

        public final void j(List list, StringBuilder sb) {
            D2.h.f(list, "$this$toQueryString");
            D2.h.f(sb, "out");
            H2.a aVarH = H2.d.h(H2.d.i(0, list.size()), 2);
            int iA = aVarH.a();
            int iB = aVarH.b();
            int iC = aVarH.c();
            if (iC >= 0) {
                if (iA > iB) {
                    return;
                }
            } else if (iA < iB) {
                return;
            }
            while (true) {
                String str = (String) list.get(iA);
                String str2 = (String) list.get(iA + 1);
                if (iA > 0) {
                    sb.append('&');
                }
                sb.append(str);
                if (str2 != null) {
                    sb.append('=');
                    sb.append(str2);
                }
                if (iA == iB) {
                    return;
                } else {
                    iA += iC;
                }
            }
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public u(String str, String str2, String str3, String str4, int i3, List<String> list, List<String> list2, String str5, String str6) {
        D2.h.f(str, "scheme");
        D2.h.f(str2, "username");
        D2.h.f(str3, "password");
        D2.h.f(str4, "host");
        D2.h.f(list, "pathSegments");
        D2.h.f(str6, "url");
        this.f1230b = str;
        this.f1231c = str2;
        this.f1232d = str3;
        this.f1233e = str4;
        this.f1234f = i3;
        this.f1235g = list;
        this.f1236h = list2;
        this.f1237i = str5;
        this.f1238j = str6;
        this.f1229a = D2.h.b(str, "https");
    }

    public final String b() {
        if (this.f1237i == null) {
            return null;
        }
        int iN = K2.o.N(this.f1238j, '#', 0, false, 6, null) + 1;
        String str = this.f1238j;
        if (str == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str.substring(iN);
        D2.h.e(strSubstring, "(this as java.lang.String).substring(startIndex)");
        return strSubstring;
    }

    public final String c() {
        if (this.f1232d.length() == 0) {
            return "";
        }
        int iN = K2.o.N(this.f1238j, ':', this.f1230b.length() + 3, false, 4, null) + 1;
        int iN2 = K2.o.N(this.f1238j, '@', 0, false, 6, null);
        String str = this.f1238j;
        if (str == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str.substring(iN, iN2);
        D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    public final String d() {
        int iN = K2.o.N(this.f1238j, '/', this.f1230b.length() + 3, false, 4, null);
        String str = this.f1238j;
        int iN2 = N2.c.n(str, "?#", iN, str.length());
        String str2 = this.f1238j;
        if (str2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str2.substring(iN, iN2);
        D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    public final List e() {
        int iN = K2.o.N(this.f1238j, '/', this.f1230b.length() + 3, false, 4, null);
        String str = this.f1238j;
        int iN2 = N2.c.n(str, "?#", iN, str.length());
        ArrayList arrayList = new ArrayList();
        while (iN < iN2) {
            int i3 = iN + 1;
            int iM = N2.c.m(this.f1238j, '/', i3, iN2);
            String str2 = this.f1238j;
            if (str2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            String strSubstring = str2.substring(i3, iM);
            D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            arrayList.add(strSubstring);
            iN = iM;
        }
        return arrayList;
    }

    public boolean equals(Object obj) {
        return (obj instanceof u) && D2.h.b(((u) obj).f1238j, this.f1238j);
    }

    public final String f() {
        if (this.f1236h == null) {
            return null;
        }
        int iN = K2.o.N(this.f1238j, '?', 0, false, 6, null) + 1;
        String str = this.f1238j;
        int iM = N2.c.m(str, '#', iN, str.length());
        String str2 = this.f1238j;
        if (str2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str2.substring(iN, iM);
        D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    public final String g() {
        if (this.f1231c.length() == 0) {
            return "";
        }
        int length = this.f1230b.length() + 3;
        String str = this.f1238j;
        int iN = N2.c.n(str, ":@", length, str.length());
        String str2 = this.f1238j;
        if (str2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring = str2.substring(length, iN);
        D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    public final String h() {
        return this.f1233e;
    }

    public int hashCode() {
        return this.f1238j.hashCode();
    }

    public final boolean i() {
        return this.f1229a;
    }

    public final a j() {
        a aVar = new a();
        aVar.u(this.f1230b);
        aVar.r(g());
        aVar.q(c());
        aVar.s(this.f1233e);
        aVar.t(this.f1234f != f1228l.c(this.f1230b) ? this.f1234f : -1);
        aVar.d().clear();
        aVar.d().addAll(e());
        aVar.c(f());
        aVar.p(b());
        return aVar;
    }

    public final a k(String str) {
        D2.h.f(str, "link");
        try {
            return new a().h(this, str);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public final int l() {
        return this.f1234f;
    }

    public final String m() {
        if (this.f1236h == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        f1228l.j(this.f1236h, sb);
        return sb.toString();
    }

    public final String n() {
        a aVarK = k("/...");
        D2.h.c(aVarK);
        return aVarK.v("").i("").a().toString();
    }

    public final u o(String str) {
        D2.h.f(str, "link");
        a aVarK = k(str);
        if (aVarK != null) {
            return aVarK.a();
        }
        return null;
    }

    public final String p() {
        return this.f1230b;
    }

    public final URI q() {
        String string = j().m().toString();
        try {
            return new URI(string);
        } catch (URISyntaxException e4) {
            try {
                URI uriCreate = URI.create(new K2.k("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]").c(string, ""));
                D2.h.e(uriCreate, "try {\n        val stripp…e) // Unexpected!\n      }");
                return uriCreate;
            } catch (Exception unused) {
                throw new RuntimeException(e4);
            }
        }
    }

    public final URL r() {
        try {
            return new URL(this.f1238j);
        } catch (MalformedURLException e4) {
            throw new RuntimeException(e4);
        }
    }

    public String toString() {
        return this.f1238j;
    }
}
