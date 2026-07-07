package M2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0686i;
import s2.AbstractC0711h;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class t implements Iterable, E2.a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final b f1224c = new b(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String[] f1225b;

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final List f1226a = new ArrayList(20);

        public final a a(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            b bVar = t.f1224c;
            bVar.d(str);
            bVar.e(str2, str);
            c(str, str2);
            return this;
        }

        public final a b(String str) {
            D2.h.f(str, "line");
            int iN = K2.o.N(str, ':', 1, false, 4, null);
            if (iN != -1) {
                String strSubstring = str.substring(0, iN);
                D2.h.e(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                String strSubstring2 = str.substring(iN + 1);
                D2.h.e(strSubstring2, "(this as java.lang.String).substring(startIndex)");
                c(strSubstring, strSubstring2);
            } else if (str.charAt(0) == ':') {
                String strSubstring3 = str.substring(1);
                D2.h.e(strSubstring3, "(this as java.lang.String).substring(startIndex)");
                c("", strSubstring3);
            } else {
                c("", str);
            }
            return this;
        }

        public final a c(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            this.f1226a.add(str);
            this.f1226a.add(K2.o.w0(str2).toString());
            return this;
        }

        public final a d(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            t.f1224c.d(str);
            c(str, str2);
            return this;
        }

        public final t e() {
            Object[] array = this.f1226a.toArray(new String[0]);
            if (array != null) {
                return new t((String[]) array, null);
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }

        public final String f(String str) {
            D2.h.f(str, "name");
            H2.a aVarH = H2.d.h(H2.d.g(this.f1226a.size() - 2, 0), 2);
            int iA = aVarH.a();
            int iB = aVarH.b();
            int iC = aVarH.c();
            if (iC >= 0) {
                if (iA > iB) {
                    return null;
                }
            } else if (iA < iB) {
                return null;
            }
            while (!K2.o.n(str, (String) this.f1226a.get(iA), true)) {
                if (iA == iB) {
                    return null;
                }
                iA += iC;
            }
            return (String) this.f1226a.get(iA + 1);
        }

        public final List g() {
            return this.f1226a;
        }

        public final a h(String str) {
            D2.h.f(str, "name");
            int i3 = 0;
            while (i3 < this.f1226a.size()) {
                if (K2.o.n(str, (String) this.f1226a.get(i3), true)) {
                    this.f1226a.remove(i3);
                    this.f1226a.remove(i3);
                    i3 -= 2;
                }
                i3 += 2;
            }
            return this;
        }

        public final a i(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            b bVar = t.f1224c;
            bVar.d(str);
            bVar.e(str2, str);
            h(str);
            c(str, str2);
            return this;
        }
    }

    public static final class b {
        private b() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void d(String str) {
            if (!(str.length() > 0)) {
                throw new IllegalArgumentException("name is empty");
            }
            int length = str.length();
            for (int i3 = 0; i3 < length; i3++) {
                char cCharAt = str.charAt(i3);
                if (!('!' <= cCharAt && '~' >= cCharAt)) {
                    throw new IllegalArgumentException(N2.c.q("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(cCharAt), Integer.valueOf(i3), str).toString());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void e(String str, String str2) {
            int length = str.length();
            for (int i3 = 0; i3 < length; i3++) {
                char cCharAt = str.charAt(i3);
                if (!(cCharAt == '\t' || (' ' <= cCharAt && '~' >= cCharAt))) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(N2.c.q("Unexpected char %#04x at %d in %s value", Integer.valueOf(cCharAt), Integer.valueOf(i3), str2));
                    sb.append(N2.c.E(str2) ? "" : ": " + str);
                    throw new IllegalArgumentException(sb.toString().toString());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String f(String[] strArr, String str) {
            H2.a aVarH = H2.d.h(H2.d.g(strArr.length - 2, 0), 2);
            int iA = aVarH.a();
            int iB = aVarH.b();
            int iC = aVarH.c();
            if (iC >= 0) {
                if (iA > iB) {
                    return null;
                }
            } else if (iA < iB) {
                return null;
            }
            while (!K2.o.n(str, strArr[iA], true)) {
                if (iA == iB) {
                    return null;
                }
                iA += iC;
            }
            return strArr[iA + 1];
        }

        public final t g(Map map) {
            D2.h.f(map, "$this$toHeaders");
            String[] strArr = new String[map.size() * 2];
            int i3 = 0;
            for (Map.Entry entry : map.entrySet()) {
                String str = (String) entry.getKey();
                String str2 = (String) entry.getValue();
                if (str == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                String string = K2.o.w0(str).toString();
                if (str2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                String string2 = K2.o.w0(str2).toString();
                d(string);
                e(string2, string);
                strArr[i3] = string;
                strArr[i3 + 1] = string2;
                i3 += 2;
            }
            return new t(strArr, null);
        }

        public final t h(String... strArr) throws CloneNotSupportedException {
            D2.h.f(strArr, "namesAndValues");
            if (!(strArr.length % 2 == 0)) {
                throw new IllegalArgumentException("Expected alternating header names and values");
            }
            Object objClone = strArr.clone();
            if (objClone == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.String>");
            }
            String[] strArr2 = (String[]) objClone;
            int length = strArr2.length;
            for (int i3 = 0; i3 < length; i3++) {
                String str = strArr2[i3];
                if (!(str != null)) {
                    throw new IllegalArgumentException("Headers cannot be null");
                }
                if (str == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                strArr2[i3] = K2.o.w0(str).toString();
            }
            H2.a aVarH = H2.d.h(AbstractC0711h.p(strArr2), 2);
            int iA = aVarH.a();
            int iB = aVarH.b();
            int iC = aVarH.c();
            if (iC < 0 ? iA >= iB : iA <= iB) {
                while (true) {
                    String str2 = strArr2[iA];
                    String str3 = strArr2[iA + 1];
                    d(str2);
                    e(str3, str2);
                    if (iA == iB) {
                        break;
                    }
                    iA += iC;
                }
            }
            return new t(strArr2, null);
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    private t(String[] strArr) {
        this.f1225b = strArr;
    }

    public static final t f(Map map) {
        return f1224c.g(map);
    }

    public final String a(String str) {
        D2.h.f(str, "name");
        return f1224c.f(this.f1225b, str);
    }

    public final String b(int i3) {
        return this.f1225b[i3 * 2];
    }

    public final Set c() {
        TreeSet treeSet = new TreeSet(K2.o.o(D2.u.f192a));
        int size = size();
        for (int i3 = 0; i3 < size; i3++) {
            treeSet.add(b(i3));
        }
        Set setUnmodifiableSet = Collections.unmodifiableSet(treeSet);
        D2.h.e(setUnmodifiableSet, "Collections.unmodifiableSet(result)");
        return setUnmodifiableSet;
    }

    public final a e() {
        a aVar = new a();
        AbstractC0717n.u(aVar.g(), this.f1225b);
        return aVar;
    }

    public boolean equals(Object obj) {
        return (obj instanceof t) && Arrays.equals(this.f1225b, ((t) obj).f1225b);
    }

    public final String h(int i3) {
        return this.f1225b[(i3 * 2) + 1];
    }

    public int hashCode() {
        return Arrays.hashCode(this.f1225b);
    }

    public final List i(String str) {
        D2.h.f(str, "name");
        int size = size();
        ArrayList arrayList = null;
        for (int i3 = 0; i3 < size; i3++) {
            if (K2.o.n(str, b(i3), true)) {
                if (arrayList == null) {
                    arrayList = new ArrayList(2);
                }
                arrayList.add(h(i3));
            }
        }
        if (arrayList == null) {
            return AbstractC0717n.g();
        }
        List listUnmodifiableList = Collections.unmodifiableList(arrayList);
        D2.h.e(listUnmodifiableList, "Collections.unmodifiableList(result)");
        return listUnmodifiableList;
    }

    @Override // java.lang.Iterable
    public Iterator iterator() {
        int size = size();
        C0686i[] c0686iArr = new C0686i[size];
        for (int i3 = 0; i3 < size; i3++) {
            c0686iArr[i3] = r2.n.a(b(i3), h(i3));
        }
        return D2.b.a(c0686iArr);
    }

    public final int size() {
        return this.f1225b.length / 2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = size();
        for (int i3 = 0; i3 < size; i3++) {
            String strB = b(i3);
            String strH = h(i3);
            sb.append(strB);
            sb.append(": ");
            if (N2.c.E(strB)) {
                strH = "██";
            }
            sb.append(strH);
            sb.append("\n");
        }
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }

    public /* synthetic */ t(String[] strArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(strArr);
    }
}
