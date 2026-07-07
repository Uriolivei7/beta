package androidx.appcompat.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.widget.O;
import androidx.appcompat.widget.h0;
import androidx.core.view.AbstractC0239b;
import androidx.core.view.B;
import d.j;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import n.InterfaceMenuC0615a;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class g extends MenuInflater {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    static final Class[] f3380e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    static final Class[] f3381f;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final Object[] f3382a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final Object[] f3383b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    Context f3384c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Object f3385d;

    private static class a implements MenuItem.OnMenuItemClickListener {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private static final Class[] f3386c = {MenuItem.class};

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Object f3387a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Method f3388b;

        public a(Object obj, String str) {
            this.f3387a = obj;
            Class<?> cls = obj.getClass();
            try {
                this.f3388b = cls.getMethod(str, f3386c);
            } catch (Exception e4) {
                InflateException inflateException = new InflateException("Couldn't resolve menu item onClick handler " + str + " in class " + cls.getName());
                inflateException.initCause(e4);
                throw inflateException;
            }
        }

        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            try {
                if (this.f3388b.getReturnType() == Boolean.TYPE) {
                    return ((Boolean) this.f3388b.invoke(this.f3387a, menuItem)).booleanValue();
                }
                this.f3388b.invoke(this.f3387a, menuItem);
                return true;
            } catch (Exception e4) {
                throw new RuntimeException(e4);
            }
        }
    }

    private class b {

        /* JADX INFO: renamed from: A, reason: collision with root package name */
        AbstractC0239b f3389A;

        /* JADX INFO: renamed from: B, reason: collision with root package name */
        private CharSequence f3390B;

        /* JADX INFO: renamed from: C, reason: collision with root package name */
        private CharSequence f3391C;

        /* JADX INFO: renamed from: D, reason: collision with root package name */
        private ColorStateList f3392D = null;

        /* JADX INFO: renamed from: E, reason: collision with root package name */
        private PorterDuff.Mode f3393E = null;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Menu f3395a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f3396b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f3397c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f3398d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f3399e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private boolean f3400f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private boolean f3401g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private boolean f3402h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private int f3403i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private int f3404j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private CharSequence f3405k;

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        private CharSequence f3406l;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        private int f3407m;

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        private char f3408n;

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        private int f3409o;

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        private char f3410p;

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        private int f3411q;

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        private int f3412r;

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        private boolean f3413s;

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        private boolean f3414t;

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        private boolean f3415u;

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        private int f3416v;

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        private int f3417w;

        /* JADX INFO: renamed from: x, reason: collision with root package name */
        private String f3418x;

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        private String f3419y;

        /* JADX INFO: renamed from: z, reason: collision with root package name */
        private String f3420z;

        public b(Menu menu) {
            this.f3395a = menu;
            h();
        }

        private char c(String str) {
            if (str == null) {
                return (char) 0;
            }
            return str.charAt(0);
        }

        private Object e(String str, Class[] clsArr, Object[] objArr) {
            try {
                Constructor<?> constructor = Class.forName(str, false, g.this.f3384c.getClassLoader()).getConstructor(clsArr);
                constructor.setAccessible(true);
                return constructor.newInstance(objArr);
            } catch (Exception e4) {
                Log.w("SupportMenuInflater", "Cannot instantiate class: " + str, e4);
                return null;
            }
        }

        private void i(MenuItem menuItem) {
            boolean z3 = false;
            menuItem.setChecked(this.f3413s).setVisible(this.f3414t).setEnabled(this.f3415u).setCheckable(this.f3412r >= 1).setTitleCondensed(this.f3406l).setIcon(this.f3407m);
            int i3 = this.f3416v;
            if (i3 >= 0) {
                menuItem.setShowAsAction(i3);
            }
            if (this.f3420z != null) {
                if (g.this.f3384c.isRestricted()) {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
                menuItem.setOnMenuItemClickListener(new a(g.this.b(), this.f3420z));
            }
            if (this.f3412r >= 2) {
                if (menuItem instanceof androidx.appcompat.view.menu.g) {
                    ((androidx.appcompat.view.menu.g) menuItem).t(true);
                } else if (menuItem instanceof i.c) {
                    ((i.c) menuItem).h(true);
                }
            }
            String str = this.f3418x;
            if (str != null) {
                menuItem.setActionView((View) e(str, g.f3380e, g.this.f3382a));
                z3 = true;
            }
            int i4 = this.f3417w;
            if (i4 > 0) {
                if (z3) {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                } else {
                    menuItem.setActionView(i4);
                }
            }
            AbstractC0239b abstractC0239b = this.f3389A;
            if (abstractC0239b != null) {
                B.a(menuItem, abstractC0239b);
            }
            B.c(menuItem, this.f3390B);
            B.g(menuItem, this.f3391C);
            B.b(menuItem, this.f3408n, this.f3409o);
            B.f(menuItem, this.f3410p, this.f3411q);
            PorterDuff.Mode mode = this.f3393E;
            if (mode != null) {
                B.e(menuItem, mode);
            }
            ColorStateList colorStateList = this.f3392D;
            if (colorStateList != null) {
                B.d(menuItem, colorStateList);
            }
        }

        public void a() {
            this.f3402h = true;
            i(this.f3395a.add(this.f3396b, this.f3403i, this.f3404j, this.f3405k));
        }

        public SubMenu b() {
            this.f3402h = true;
            SubMenu subMenuAddSubMenu = this.f3395a.addSubMenu(this.f3396b, this.f3403i, this.f3404j, this.f3405k);
            i(subMenuAddSubMenu.getItem());
            return subMenuAddSubMenu;
        }

        public boolean d() {
            return this.f3402h;
        }

        public void f(AttributeSet attributeSet) {
            TypedArray typedArrayObtainStyledAttributes = g.this.f3384c.obtainStyledAttributes(attributeSet, j.f9015o1);
            this.f3396b = typedArrayObtainStyledAttributes.getResourceId(j.f9023q1, 0);
            this.f3397c = typedArrayObtainStyledAttributes.getInt(j.f9031s1, 0);
            this.f3398d = typedArrayObtainStyledAttributes.getInt(j.f9035t1, 0);
            this.f3399e = typedArrayObtainStyledAttributes.getInt(j.f9039u1, 0);
            this.f3400f = typedArrayObtainStyledAttributes.getBoolean(j.f9027r1, true);
            this.f3401g = typedArrayObtainStyledAttributes.getBoolean(j.f9019p1, true);
            typedArrayObtainStyledAttributes.recycle();
        }

        public void g(AttributeSet attributeSet) {
            h0 h0VarT = h0.t(g.this.f3384c, attributeSet, j.f9043v1);
            this.f3403i = h0VarT.m(j.f9055y1, 0);
            this.f3404j = (h0VarT.j(j.f8854B1, this.f3397c) & (-65536)) | (h0VarT.j(j.f8858C1, this.f3398d) & 65535);
            this.f3405k = h0VarT.o(j.f8862D1);
            this.f3406l = h0VarT.o(j.f8866E1);
            this.f3407m = h0VarT.m(j.f9047w1, 0);
            this.f3408n = c(h0VarT.n(j.f8870F1));
            this.f3409o = h0VarT.j(j.f8898M1, 4096);
            this.f3410p = c(h0VarT.n(j.f8874G1));
            this.f3411q = h0VarT.j(j.f8914Q1, 4096);
            if (h0VarT.r(j.f8878H1)) {
                this.f3412r = h0VarT.a(j.f8878H1, false) ? 1 : 0;
            } else {
                this.f3412r = this.f3399e;
            }
            this.f3413s = h0VarT.a(j.f9059z1, false);
            this.f3414t = h0VarT.a(j.f8850A1, this.f3400f);
            this.f3415u = h0VarT.a(j.f9051x1, this.f3401g);
            this.f3416v = h0VarT.j(j.f8918R1, -1);
            this.f3420z = h0VarT.n(j.f8882I1);
            this.f3417w = h0VarT.m(j.f8886J1, 0);
            this.f3418x = h0VarT.n(j.f8894L1);
            String strN = h0VarT.n(j.f8890K1);
            this.f3419y = strN;
            boolean z3 = strN != null;
            if (z3 && this.f3417w == 0 && this.f3418x == null) {
                this.f3389A = (AbstractC0239b) e(strN, g.f3381f, g.this.f3383b);
            } else {
                if (z3) {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.f3389A = null;
            }
            this.f3390B = h0VarT.o(j.f8902N1);
            this.f3391C = h0VarT.o(j.f8922S1);
            if (h0VarT.r(j.f8910P1)) {
                this.f3393E = O.d(h0VarT.j(j.f8910P1, -1), this.f3393E);
            } else {
                this.f3393E = null;
            }
            if (h0VarT.r(j.f8906O1)) {
                this.f3392D = h0VarT.c(j.f8906O1);
            } else {
                this.f3392D = null;
            }
            h0VarT.w();
            this.f3402h = false;
        }

        public void h() {
            this.f3396b = 0;
            this.f3397c = 0;
            this.f3398d = 0;
            this.f3399e = 0;
            this.f3400f = true;
            this.f3401g = true;
        }
    }

    static {
        Class[] clsArr = {Context.class};
        f3380e = clsArr;
        f3381f = clsArr;
    }

    public g(Context context) {
        super(context);
        this.f3384c = context;
        Object[] objArr = {context};
        this.f3382a = objArr;
        this.f3383b = objArr;
    }

    private Object a(Object obj) {
        return (!(obj instanceof Activity) && (obj instanceof ContextWrapper)) ? a(((ContextWrapper) obj).getBaseContext()) : obj;
    }

    private void c(XmlPullParser xmlPullParser, AttributeSet attributeSet, Menu menu) throws XmlPullParserException, IOException {
        b bVar = new b(menu);
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType == 2) {
                String name = xmlPullParser.getName();
                if (!name.equals("menu")) {
                    throw new RuntimeException("Expecting menu, got " + name);
                }
                eventType = xmlPullParser.next();
            } else {
                eventType = xmlPullParser.next();
                if (eventType == 1) {
                    break;
                }
            }
        }
        boolean z3 = false;
        boolean z4 = false;
        String str = null;
        while (!z3) {
            if (eventType == 1) {
                throw new RuntimeException("Unexpected end of document");
            }
            if (eventType != 2) {
                if (eventType == 3) {
                    String name2 = xmlPullParser.getName();
                    if (z4 && name2.equals(str)) {
                        z4 = false;
                        str = null;
                    } else if (name2.equals("group")) {
                        bVar.h();
                    } else if (name2.equals("item")) {
                        if (!bVar.d()) {
                            AbstractC0239b abstractC0239b = bVar.f3389A;
                            if (abstractC0239b == null || !abstractC0239b.a()) {
                                bVar.a();
                            } else {
                                bVar.b();
                            }
                        }
                    } else if (name2.equals("menu")) {
                        z3 = true;
                    }
                }
            } else if (!z4) {
                String name3 = xmlPullParser.getName();
                if (name3.equals("group")) {
                    bVar.f(attributeSet);
                } else if (name3.equals("item")) {
                    bVar.g(attributeSet);
                } else if (name3.equals("menu")) {
                    c(xmlPullParser, attributeSet, bVar.b());
                } else {
                    str = name3;
                    z4 = true;
                }
            }
            eventType = xmlPullParser.next();
        }
    }

    Object b() {
        if (this.f3385d == null) {
            this.f3385d = a(this.f3384c);
        }
        return this.f3385d;
    }

    @Override // android.view.MenuInflater
    public void inflate(int i3, Menu menu) {
        if (!(menu instanceof InterfaceMenuC0615a)) {
            super.inflate(i3, menu);
            return;
        }
        XmlResourceParser layout = null;
        boolean z3 = false;
        try {
            try {
                layout = this.f3384c.getResources().getLayout(i3);
                AttributeSet attributeSetAsAttributeSet = Xml.asAttributeSet(layout);
                if (menu instanceof androidx.appcompat.view.menu.e) {
                    androidx.appcompat.view.menu.e eVar = (androidx.appcompat.view.menu.e) menu;
                    if (eVar.F()) {
                        eVar.e0();
                        z3 = true;
                    }
                }
                c(layout, attributeSetAsAttributeSet, menu);
                if (z3) {
                    ((androidx.appcompat.view.menu.e) menu).d0();
                }
                if (layout != null) {
                    layout.close();
                }
            } catch (IOException e4) {
                throw new InflateException("Error inflating menu XML", e4);
            } catch (XmlPullParserException e5) {
                throw new InflateException("Error inflating menu XML", e5);
            }
        } catch (Throwable th) {
            if (z3) {
                ((androidx.appcompat.view.menu.e) menu).d0();
            }
            if (layout != null) {
                layout.close();
            }
            throw th;
        }
    }
}
