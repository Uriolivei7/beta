package M2;

import M2.t;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import r2.C0686i;
import s2.AbstractC0696D;
import s2.AbstractC0717n;

/* JADX INFO: loaded from: classes.dex */
public final class B {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private C0193d f897a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final u f898b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f899c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final t f900d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final C f901e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final Map f902f;

    public B(u uVar, String str, t tVar, C c4, Map<Class<?>, ? extends Object> map) {
        D2.h.f(uVar, "url");
        D2.h.f(str, "method");
        D2.h.f(tVar, "headers");
        D2.h.f(map, "tags");
        this.f898b = uVar;
        this.f899c = str;
        this.f900d = tVar;
        this.f901e = c4;
        this.f902f = map;
    }

    public final C a() {
        return this.f901e;
    }

    public final C0193d b() {
        C0193d c0193d = this.f897a;
        if (c0193d != null) {
            return c0193d;
        }
        C0193d c0193dB = C0193d.f1005p.b(this.f900d);
        this.f897a = c0193dB;
        return c0193dB;
    }

    public final Map c() {
        return this.f902f;
    }

    public final String d(String str) {
        D2.h.f(str, "name");
        return this.f900d.a(str);
    }

    public final t e() {
        return this.f900d;
    }

    public final List f(String str) {
        D2.h.f(str, "name");
        return this.f900d.i(str);
    }

    public final boolean g() {
        return this.f898b.i();
    }

    public final String h() {
        return this.f899c;
    }

    public final a i() {
        return new a(this);
    }

    public final Object j() {
        return k(Object.class);
    }

    public final Object k(Class cls) {
        D2.h.f(cls, "type");
        return cls.cast(this.f902f.get(cls));
    }

    public final u l() {
        return this.f898b;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request{method=");
        sb.append(this.f899c);
        sb.append(", url=");
        sb.append(this.f898b);
        if (this.f900d.size() != 0) {
            sb.append(", headers=[");
            int i3 = 0;
            for (Object obj : this.f900d) {
                int i4 = i3 + 1;
                if (i3 < 0) {
                    AbstractC0717n.p();
                }
                C0686i c0686i = (C0686i) obj;
                String str = (String) c0686i.a();
                String str2 = (String) c0686i.b();
                if (i3 > 0) {
                    sb.append(", ");
                }
                sb.append(str);
                sb.append(':');
                sb.append(str2);
                i3 = i4;
            }
            sb.append(']');
        }
        if (!this.f902f.isEmpty()) {
            sb.append(", tags=");
            sb.append(this.f902f);
        }
        sb.append('}');
        String string = sb.toString();
        D2.h.e(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private u f903a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f904b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private t.a f905c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private C f906d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private Map f907e;

        public a() {
            this.f907e = new LinkedHashMap();
            this.f904b = "GET";
            this.f905c = new t.a();
        }

        public a a(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            this.f905c.a(str, str2);
            return this;
        }

        public B b() {
            u uVar = this.f903a;
            if (uVar != null) {
                return new B(uVar, this.f904b, this.f905c.e(), this.f906d, N2.c.S(this.f907e));
            }
            throw new IllegalStateException("url == null");
        }

        public a c(C0193d c0193d) {
            D2.h.f(c0193d, "cacheControl");
            String string = c0193d.toString();
            return string.length() == 0 ? i("Cache-Control") : e("Cache-Control", string);
        }

        public a d() {
            return g("GET", null);
        }

        public a e(String str, String str2) {
            D2.h.f(str, "name");
            D2.h.f(str2, "value");
            this.f905c.i(str, str2);
            return this;
        }

        public a f(t tVar) {
            D2.h.f(tVar, "headers");
            this.f905c = tVar.e();
            return this;
        }

        public a g(String str, C c4) {
            D2.h.f(str, "method");
            if (!(str.length() > 0)) {
                throw new IllegalArgumentException("method.isEmpty() == true");
            }
            if (c4 == null) {
                if (S2.f.e(str)) {
                    throw new IllegalArgumentException(("method " + str + " must have a request body.").toString());
                }
            } else if (!S2.f.b(str)) {
                throw new IllegalArgumentException(("method " + str + " must not have a request body.").toString());
            }
            this.f904b = str;
            this.f906d = c4;
            return this;
        }

        public a h(C c4) {
            D2.h.f(c4, "body");
            return g("POST", c4);
        }

        public a i(String str) {
            D2.h.f(str, "name");
            this.f905c.h(str);
            return this;
        }

        public a j(Class cls, Object obj) {
            D2.h.f(cls, "type");
            if (obj == null) {
                this.f907e.remove(cls);
            } else {
                if (this.f907e.isEmpty()) {
                    this.f907e = new LinkedHashMap();
                }
                Map map = this.f907e;
                Object objCast = cls.cast(obj);
                D2.h.c(objCast);
                map.put(cls, objCast);
            }
            return this;
        }

        public a k(Object obj) {
            return j(Object.class, obj);
        }

        public a l(u uVar) {
            D2.h.f(uVar, "url");
            this.f903a = uVar;
            return this;
        }

        public a m(String str) {
            D2.h.f(str, "url");
            if (K2.o.x(str, "ws:", true)) {
                StringBuilder sb = new StringBuilder();
                sb.append("http:");
                String strSubstring = str.substring(3);
                D2.h.e(strSubstring, "(this as java.lang.String).substring(startIndex)");
                sb.append(strSubstring);
                str = sb.toString();
            } else if (K2.o.x(str, "wss:", true)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("https:");
                String strSubstring2 = str.substring(4);
                D2.h.e(strSubstring2, "(this as java.lang.String).substring(startIndex)");
                sb2.append(strSubstring2);
                str = sb2.toString();
            }
            return l(u.f1228l.d(str));
        }

        public a(B b4) {
            Map mapQ;
            D2.h.f(b4, "request");
            this.f907e = new LinkedHashMap();
            this.f903a = b4.l();
            this.f904b = b4.h();
            this.f906d = b4.a();
            if (b4.c().isEmpty()) {
                mapQ = new LinkedHashMap();
            } else {
                mapQ = AbstractC0696D.q(b4.c());
            }
            this.f907e = mapQ;
            this.f905c = b4.e().e();
        }
    }
}
