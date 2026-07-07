package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Base64;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import m.AbstractC0597c;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public abstract class d {

    static class a {
        static int a(TypedArray typedArray, int i3) {
            return typedArray.getType(i3);
        }
    }

    public interface b {
    }

    public static final class c implements b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final C0060d[] f4433a;

        public c(C0060d[] c0060dArr) {
            this.f4433a = c0060dArr;
        }

        public C0060d[] a() {
            return this.f4433a;
        }
    }

    /* JADX INFO: renamed from: androidx.core.content.res.d$d, reason: collision with other inner class name */
    public static final class C0060d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f4434a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f4435b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final boolean f4436c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f4437d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int f4438e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private final int f4439f;

        public C0060d(String str, int i3, boolean z3, String str2, int i4, int i5) {
            this.f4434a = str;
            this.f4435b = i3;
            this.f4436c = z3;
            this.f4437d = str2;
            this.f4438e = i4;
            this.f4439f = i5;
        }

        public String a() {
            return this.f4434a;
        }

        public int b() {
            return this.f4439f;
        }

        public int c() {
            return this.f4438e;
        }

        public String d() {
            return this.f4437d;
        }

        public int e() {
            return this.f4435b;
        }

        public boolean f() {
            return this.f4436c;
        }
    }

    private static int a(TypedArray typedArray, int i3) {
        return a.a(typedArray, i3);
    }

    public static b b(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        int next;
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return d(xmlPullParser, resources);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static List c(Resources resources, int i3) {
        if (i3 == 0) {
            return Collections.emptyList();
        }
        TypedArray typedArrayObtainTypedArray = resources.obtainTypedArray(i3);
        try {
            if (typedArrayObtainTypedArray.length() == 0) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            if (a(typedArrayObtainTypedArray, 0) == 1) {
                for (int i4 = 0; i4 < typedArrayObtainTypedArray.length(); i4++) {
                    int resourceId = typedArrayObtainTypedArray.getResourceId(i4, 0);
                    if (resourceId != 0) {
                        arrayList.add(h(resources.getStringArray(resourceId)));
                    }
                }
            } else {
                arrayList.add(h(resources.getStringArray(i3)));
            }
            return arrayList;
        } finally {
            typedArrayObtainTypedArray.recycle();
        }
    }

    private static b d(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return e(xmlPullParser, resources);
        }
        g(xmlPullParser);
        return null;
    }

    private static b e(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        TypedArray typedArrayObtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), AbstractC0597c.f9777h);
        String string = typedArrayObtainAttributes.getString(AbstractC0597c.f9778i);
        String string2 = typedArrayObtainAttributes.getString(AbstractC0597c.f9782m);
        String string3 = typedArrayObtainAttributes.getString(AbstractC0597c.f9783n);
        int resourceId = typedArrayObtainAttributes.getResourceId(AbstractC0597c.f9779j, 0);
        int integer = typedArrayObtainAttributes.getInteger(AbstractC0597c.f9780k, 1);
        int integer2 = typedArrayObtainAttributes.getInteger(AbstractC0597c.f9781l, 500);
        String string4 = typedArrayObtainAttributes.getString(AbstractC0597c.f9784o);
        typedArrayObtainAttributes.recycle();
        if (string != null && string2 != null && string3 != null) {
            while (xmlPullParser.next() != 3) {
                g(xmlPullParser);
            }
            return new e(new p.e(string, string2, string3, (List<List<byte[]>>) c(resources, resourceId)), integer, integer2, string4);
        }
        ArrayList arrayList = new ArrayList();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("font")) {
                    arrayList.add(f(xmlPullParser, resources));
                } else {
                    g(xmlPullParser);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new c((C0060d[]) arrayList.toArray(new C0060d[0]));
    }

    private static C0060d f(XmlPullParser xmlPullParser, Resources resources) throws XmlPullParserException, IOException {
        TypedArray typedArrayObtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), AbstractC0597c.f9785p);
        int i3 = typedArrayObtainAttributes.getInt(typedArrayObtainAttributes.hasValue(AbstractC0597c.f9794y) ? AbstractC0597c.f9794y : AbstractC0597c.f9787r, 400);
        boolean z3 = 1 == typedArrayObtainAttributes.getInt(typedArrayObtainAttributes.hasValue(AbstractC0597c.f9792w) ? AbstractC0597c.f9792w : AbstractC0597c.f9788s, 0);
        int i4 = typedArrayObtainAttributes.hasValue(AbstractC0597c.f9795z) ? AbstractC0597c.f9795z : AbstractC0597c.f9789t;
        String string = typedArrayObtainAttributes.getString(typedArrayObtainAttributes.hasValue(AbstractC0597c.f9793x) ? AbstractC0597c.f9793x : AbstractC0597c.f9790u);
        int i5 = typedArrayObtainAttributes.getInt(i4, 0);
        int i6 = typedArrayObtainAttributes.hasValue(AbstractC0597c.f9791v) ? AbstractC0597c.f9791v : AbstractC0597c.f9786q;
        int resourceId = typedArrayObtainAttributes.getResourceId(i6, 0);
        String string2 = typedArrayObtainAttributes.getString(i6);
        typedArrayObtainAttributes.recycle();
        while (xmlPullParser.next() != 3) {
            g(xmlPullParser);
        }
        return new C0060d(string2, i3, z3, string, i5, resourceId);
    }

    private static void g(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int i3 = 1;
        while (i3 > 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                i3++;
            } else if (next == 3) {
                i3--;
            }
        }
    }

    private static List h(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(Base64.decode(str, 0));
        }
        return arrayList;
    }

    public static final class e implements b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final p.e f4440a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f4441b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f4442c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f4443d;

        public e(p.e eVar, int i3, int i4, String str) {
            this.f4440a = eVar;
            this.f4442c = i3;
            this.f4441b = i4;
            this.f4443d = str;
        }

        public int a() {
            return this.f4442c;
        }

        public p.e b() {
            return this.f4440a;
        }

        public String c() {
            return this.f4443d;
        }

        public int d() {
            return this.f4441b;
        }

        public e(p.e eVar, int i3, int i4) {
            this(eVar, i3, i4, null);
        }
    }
}
