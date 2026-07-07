package e1;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: renamed from: e1.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0527d {

    /* JADX INFO: renamed from: e1.d$a */
    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Map f9352a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private boolean f9353b;

        public Map a() {
            if (!this.f9353b) {
                throw new IllegalStateException("Underlying map has already been built");
            }
            this.f9353b = false;
            return this.f9352a;
        }

        public a b(Object obj, Object obj2) {
            if (!this.f9353b) {
                throw new IllegalStateException("Underlying map has already been built");
            }
            this.f9352a.put(obj, obj2);
            return this;
        }

        private a() {
            this.f9352a = C0527d.b();
            this.f9353b = true;
        }
    }

    public static a a() {
        return new a();
    }

    public static HashMap b() {
        return new HashMap();
    }

    public static Map c() {
        return b();
    }

    public static Map d(Object obj, Object obj2) {
        Map mapC = c();
        mapC.put(obj, obj2);
        return mapC;
    }

    public static Map e(Object obj, Object obj2, Object obj3, Object obj4) {
        Map mapC = c();
        mapC.put(obj, obj2);
        mapC.put(obj3, obj4);
        return mapC;
    }

    public static Map f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        Map mapC = c();
        mapC.put(obj, obj2);
        mapC.put(obj3, obj4);
        mapC.put(obj5, obj6);
        return mapC;
    }

    public static Map g(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        Map mapC = c();
        mapC.put(obj, obj2);
        mapC.put(obj3, obj4);
        mapC.put(obj5, obj6);
        mapC.put(obj7, obj8);
        return mapC;
    }

    public static Map h(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10) {
        Map mapC = c();
        mapC.put(obj, obj2);
        mapC.put(obj3, obj4);
        mapC.put(obj5, obj6);
        mapC.put(obj7, obj8);
        mapC.put(obj9, obj10);
        return mapC;
    }
}
