package com.facebook.react.uimanager;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.WritableMap;
import d1.AbstractC0505m;
import d1.AbstractC0508p;
import java.util.HashMap;
import java.util.List;
import r.v;
import x.AbstractC0766a;

/* JADX INFO: renamed from: com.facebook.react.uimanager.h0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0433h0 extends AbstractC0766a {

    /* JADX INFO: renamed from: u, reason: collision with root package name */
    public static final HashMap f7485u;

    /* JADX INFO: renamed from: v, reason: collision with root package name */
    private static int f7486v;

    /* JADX INFO: renamed from: w, reason: collision with root package name */
    private static final int f7487w;

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private final View f7488q;

    /* JADX INFO: renamed from: r, reason: collision with root package name */
    private Handler f7489r;

    /* JADX INFO: renamed from: s, reason: collision with root package name */
    private final HashMap f7490s;

    /* JADX INFO: renamed from: t, reason: collision with root package name */
    View f7491t;

    /* JADX INFO: renamed from: com.facebook.react.uimanager.h0$a */
    class a extends Handler {
        a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ((View) message.obj).sendAccessibilityEvent(4);
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.uimanager.h0$b */
    class b extends P1.d {

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ WritableMap f7493h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(int i3, int i4, WritableMap writableMap) {
            super(i3, i4);
            this.f7493h = writableMap;
        }

        @Override // P1.d
        protected WritableMap j() {
            return this.f7493h;
        }

        @Override // P1.d
        public String k() {
            return "topAccessibilityAction";
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.uimanager.h0$c */
    static /* synthetic */ class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ int[] f7495a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        static final /* synthetic */ int[] f7496b;

        static {
            int[] iArr = new int[e.values().length];
            f7496b = iArr;
            try {
                iArr[e.ALERT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7496b[e.BUTTON.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f7496b[e.CHECKBOX.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f7496b[e.COMBOBOX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f7496b[e.GRID.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f7496b[e.HEADING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f7496b[e.IMG.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f7496b[e.LINK.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f7496b[e.LIST.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                f7496b[e.MENU.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                f7496b[e.MENUBAR.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                f7496b[e.MENUITEM.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                f7496b[e.NONE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                f7496b[e.PROGRESSBAR.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                f7496b[e.RADIO.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                f7496b[e.RADIOGROUP.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                f7496b[e.SCROLLBAR.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                f7496b[e.SEARCHBOX.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                f7496b[e.SLIDER.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                f7496b[e.SPINBUTTON.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                f7496b[e.SUMMARY.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                f7496b[e.SWITCH.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                f7496b[e.TAB.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                f7496b[e.TABLIST.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                f7496b[e.TIMER.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                f7496b[e.TOOLBAR.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            int[] iArr2 = new int[d.values().length];
            f7495a = iArr2;
            try {
                iArr2[d.BUTTON.ordinal()] = 1;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                f7495a[d.DROPDOWNLIST.ordinal()] = 2;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                f7495a[d.TOGGLEBUTTON.ordinal()] = 3;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                f7495a[d.SEARCH.ordinal()] = 4;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                f7495a[d.IMAGE.ordinal()] = 5;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                f7495a[d.IMAGEBUTTON.ordinal()] = 6;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                f7495a[d.KEYBOARDKEY.ordinal()] = 7;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                f7495a[d.TEXT.ordinal()] = 8;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                f7495a[d.ADJUSTABLE.ordinal()] = 9;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                f7495a[d.CHECKBOX.ordinal()] = 10;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                f7495a[d.RADIO.ordinal()] = 11;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                f7495a[d.SPINBUTTON.ordinal()] = 12;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                f7495a[d.SWITCH.ordinal()] = 13;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                f7495a[d.LIST.ordinal()] = 14;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                f7495a[d.GRID.ordinal()] = 15;
            } catch (NoSuchFieldError unused41) {
            }
            try {
                f7495a[d.SCROLLVIEW.ordinal()] = 16;
            } catch (NoSuchFieldError unused42) {
            }
            try {
                f7495a[d.HORIZONTALSCROLLVIEW.ordinal()] = 17;
            } catch (NoSuchFieldError unused43) {
            }
            try {
                f7495a[d.PAGER.ordinal()] = 18;
            } catch (NoSuchFieldError unused44) {
            }
            try {
                f7495a[d.DRAWERLAYOUT.ordinal()] = 19;
            } catch (NoSuchFieldError unused45) {
            }
            try {
                f7495a[d.SLIDINGDRAWER.ordinal()] = 20;
            } catch (NoSuchFieldError unused46) {
            }
            try {
                f7495a[d.ICONMENU.ordinal()] = 21;
            } catch (NoSuchFieldError unused47) {
            }
            try {
                f7495a[d.VIEWGROUP.ordinal()] = 22;
            } catch (NoSuchFieldError unused48) {
            }
            try {
                f7495a[d.WEBVIEW.ordinal()] = 23;
            } catch (NoSuchFieldError unused49) {
            }
            try {
                f7495a[d.NONE.ordinal()] = 24;
            } catch (NoSuchFieldError unused50) {
            }
            try {
                f7495a[d.LINK.ordinal()] = 25;
            } catch (NoSuchFieldError unused51) {
            }
            try {
                f7495a[d.SUMMARY.ordinal()] = 26;
            } catch (NoSuchFieldError unused52) {
            }
            try {
                f7495a[d.HEADER.ordinal()] = 27;
            } catch (NoSuchFieldError unused53) {
            }
            try {
                f7495a[d.ALERT.ordinal()] = 28;
            } catch (NoSuchFieldError unused54) {
            }
            try {
                f7495a[d.COMBOBOX.ordinal()] = 29;
            } catch (NoSuchFieldError unused55) {
            }
            try {
                f7495a[d.MENU.ordinal()] = 30;
            } catch (NoSuchFieldError unused56) {
            }
            try {
                f7495a[d.MENUBAR.ordinal()] = 31;
            } catch (NoSuchFieldError unused57) {
            }
            try {
                f7495a[d.MENUITEM.ordinal()] = 32;
            } catch (NoSuchFieldError unused58) {
            }
            try {
                f7495a[d.PROGRESSBAR.ordinal()] = 33;
            } catch (NoSuchFieldError unused59) {
            }
            try {
                f7495a[d.RADIOGROUP.ordinal()] = 34;
            } catch (NoSuchFieldError unused60) {
            }
            try {
                f7495a[d.SCROLLBAR.ordinal()] = 35;
            } catch (NoSuchFieldError unused61) {
            }
            try {
                f7495a[d.TAB.ordinal()] = 36;
            } catch (NoSuchFieldError unused62) {
            }
            try {
                f7495a[d.TABLIST.ordinal()] = 37;
            } catch (NoSuchFieldError unused63) {
            }
            try {
                f7495a[d.TIMER.ordinal()] = 38;
            } catch (NoSuchFieldError unused64) {
            }
            try {
                f7495a[d.TOOLBAR.ordinal()] = 39;
            } catch (NoSuchFieldError unused65) {
            }
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.uimanager.h0$d */
    public enum d {
        NONE,
        BUTTON,
        DROPDOWNLIST,
        TOGGLEBUTTON,
        LINK,
        SEARCH,
        IMAGE,
        IMAGEBUTTON,
        KEYBOARDKEY,
        TEXT,
        ADJUSTABLE,
        SUMMARY,
        HEADER,
        ALERT,
        CHECKBOX,
        COMBOBOX,
        MENU,
        MENUBAR,
        MENUITEM,
        PROGRESSBAR,
        RADIO,
        RADIOGROUP,
        SCROLLBAR,
        SPINBUTTON,
        SWITCH,
        TAB,
        TABLIST,
        TIMER,
        LIST,
        GRID,
        PAGER,
        SCROLLVIEW,
        HORIZONTALSCROLLVIEW,
        VIEWGROUP,
        WEBVIEW,
        DRAWERLAYOUT,
        SLIDINGDRAWER,
        ICONMENU,
        TOOLBAR;

        public static d b(e eVar) {
            switch (c.f7496b[eVar.ordinal()]) {
                case 1:
                    return ALERT;
                case 2:
                    return BUTTON;
                case 3:
                    return CHECKBOX;
                case 4:
                    return COMBOBOX;
                case 5:
                    return GRID;
                case 6:
                    return HEADER;
                case 7:
                    return IMAGE;
                case 8:
                    return LINK;
                case 9:
                    return LIST;
                case 10:
                    return MENU;
                case 11:
                    return MENUBAR;
                case 12:
                    return MENUITEM;
                case 13:
                    return NONE;
                case 14:
                    return PROGRESSBAR;
                case 15:
                    return RADIO;
                case 16:
                    return RADIOGROUP;
                case 17:
                    return SCROLLBAR;
                case 18:
                    return SEARCH;
                case 19:
                    return ADJUSTABLE;
                case 20:
                    return SPINBUTTON;
                case 21:
                    return SUMMARY;
                case 22:
                    return SWITCH;
                case 23:
                    return TAB;
                case 24:
                    return TABLIST;
                case 25:
                    return TIMER;
                case 26:
                    return TOOLBAR;
                default:
                    return null;
            }
        }

        public static d c(String str) {
            if (str == null) {
                return NONE;
            }
            for (d dVar : values()) {
                if (dVar.name().equalsIgnoreCase(str)) {
                    return dVar;
                }
            }
            throw new IllegalArgumentException("Invalid accessibility role value: " + str);
        }

        public static d d(View view) {
            e eVar = (e) view.getTag(AbstractC0505m.f9253z);
            return eVar != null ? b(eVar) : (d) view.getTag(AbstractC0505m.f9234g);
        }

        public static String e(d dVar) {
            switch (c.f7495a[dVar.ordinal()]) {
                case 1:
                    return "android.widget.Button";
                case 2:
                    return "android.widget.Spinner";
                case 3:
                    return "android.widget.ToggleButton";
                case 4:
                    return "android.widget.EditText";
                case 5:
                    return "android.widget.ImageView";
                case 6:
                    return "android.widget.ImageButton";
                case 7:
                    return "android.inputmethodservice.Keyboard$Key";
                case 8:
                    return "android.widget.TextView";
                case 9:
                    return "android.widget.SeekBar";
                case 10:
                    return "android.widget.CheckBox";
                case 11:
                    return "android.widget.RadioButton";
                case 12:
                    return "android.widget.SpinButton";
                case 13:
                    return "android.widget.Switch";
                case 14:
                    return "android.widget.AbsListView";
                case 15:
                    return "android.widget.GridView";
                case 16:
                    return "android.widget.ScrollView";
                case 17:
                    return "android.widget.HorizontalScrollView";
                case 18:
                    return "androidx.viewpager.widget.ViewPager";
                case 19:
                    return "androidx.drawerlayout.widget.DrawerLayout";
                case 20:
                    return "android.widget.SlidingDrawer";
                case 21:
                    return "com.android.internal.view.menu.IconMenuView";
                case 22:
                    return "android.view.ViewGroup";
                case 23:
                    return "android.webkit.WebView";
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                    return "android.view.View";
                default:
                    throw new IllegalArgumentException("Invalid accessibility role value: " + dVar);
            }
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.uimanager.h0$e */
    public enum e {
        ALERT,
        ALERTDIALOG,
        APPLICATION,
        ARTICLE,
        BANNER,
        BUTTON,
        CELL,
        CHECKBOX,
        COLUMNHEADER,
        COMBOBOX,
        COMPLEMENTARY,
        CONTENTINFO,
        DEFINITION,
        DIALOG,
        DIRECTORY,
        DOCUMENT,
        FEED,
        FIGURE,
        FORM,
        GRID,
        GROUP,
        HEADING,
        IMG,
        LINK,
        LIST,
        LISTITEM,
        LOG,
        MAIN,
        MARQUEE,
        MATH,
        MENU,
        MENUBAR,
        MENUITEM,
        METER,
        NAVIGATION,
        NONE,
        NOTE,
        OPTION,
        PRESENTATION,
        PROGRESSBAR,
        RADIO,
        RADIOGROUP,
        REGION,
        ROW,
        ROWGROUP,
        ROWHEADER,
        SCROLLBAR,
        SEARCHBOX,
        SEPARATOR,
        SLIDER,
        SPINBUTTON,
        STATUS,
        SUMMARY,
        SWITCH,
        TAB,
        TABLE,
        TABLIST,
        TABPANEL,
        TERM,
        TIMER,
        TOOLBAR,
        TOOLTIP,
        TREE,
        TREEGRID,
        TREEITEM;

        public static e b(String str) {
            for (e eVar : values()) {
                if (eVar.name().equalsIgnoreCase(str)) {
                    return eVar;
                }
            }
            return null;
        }
    }

    static {
        HashMap map = new HashMap();
        f7485u = map;
        f7486v = 1056964608;
        f7487w = 2;
        map.put("activate", Integer.valueOf(v.a.f10462h.a()));
        map.put("longpress", Integer.valueOf(v.a.f10463i.a()));
        map.put("increment", Integer.valueOf(v.a.f10470p.a()));
        map.put("decrement", Integer.valueOf(v.a.f10471q.a()));
        map.put("expand", Integer.valueOf(v.a.f10476v.a()));
        map.put("collapse", Integer.valueOf(v.a.f10477w.a()));
    }

    public C0433h0(View view, boolean z3, int i3) {
        super(view);
        this.f7488q = view;
        this.f7490s = new HashMap();
        this.f7489r = new a();
        view.setFocusable(z3);
        androidx.core.view.Z.f0(view, i3);
    }

    public static r.v U(View view) {
        if (view == null) {
            return null;
        }
        r.v vVarC0 = r.v.c0();
        try {
            androidx.core.view.Z.N(view, vVarC0);
            return vVarC0;
        } catch (NullPointerException unused) {
            if (vVarC0 != null) {
                vVarC0.g0();
            }
            return null;
        }
    }

    public static CharSequence W(View view, r.v vVar) {
        r.v vVarU = vVar == null ? U(view) : r.v.e0(vVar);
        if (vVarU == null) {
            return null;
        }
        try {
            CharSequence charSequenceU = vVarU.u();
            CharSequence charSequenceE = vVarU.E();
            boolean zIsEmpty = TextUtils.isEmpty(charSequenceE);
            boolean z3 = view instanceof EditText;
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(charSequenceU) && (!z3 || zIsEmpty)) {
                sb.append(charSequenceU);
                return sb;
            }
            if (!zIsEmpty) {
                sb.append(charSequenceE);
                return sb;
            }
            if (!(view instanceof ViewGroup)) {
                return null;
            }
            StringBuilder sb2 = new StringBuilder();
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = viewGroup.getChildAt(i3);
                r.v vVarC0 = r.v.c0();
                androidx.core.view.Z.N(childAt, vVarC0);
                if (d0(vVarC0, childAt) && !b0(vVarC0, childAt)) {
                    CharSequence charSequenceW = W(childAt, null);
                    if (!TextUtils.isEmpty(charSequenceW)) {
                        sb2.append(((Object) charSequenceW) + ", ");
                    }
                }
                vVarC0.g0();
            }
            return e0(sb2);
        } finally {
            vVarU.g0();
        }
    }

    public static boolean X(r.v vVar, View view) {
        if (vVar != null && view != null && (view instanceof ViewGroup)) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = viewGroup.getChildAt(i3);
                if (childAt != null) {
                    r.v vVarC0 = r.v.c0();
                    try {
                        androidx.core.view.Z.N(childAt, vVarC0);
                        if (vVarC0.b0() && !b0(vVarC0, childAt) && d0(vVarC0, childAt)) {
                            vVarC0.g0();
                            return true;
                        }
                        vVarC0.g0();
                    } catch (Throwable th) {
                        if (vVarC0 != null) {
                            vVarC0.g0();
                        }
                        throw th;
                    }
                }
            }
        }
        return false;
    }

    private static boolean Y(r.v vVar) {
        return vVar != null && (!TextUtils.isEmpty(vVar.D()) || vVar.M() || a0(vVar));
    }

    public static boolean Z(r.v vVar) {
        return (vVar == null || vVar.s() != null || (TextUtils.isEmpty(vVar.E()) && TextUtils.isEmpty(vVar.u()) && TextUtils.isEmpty(vVar.x()))) ? false : true;
    }

    public static boolean a0(r.v vVar) {
        v.g gVarB;
        if (vVar == null || (gVarB = vVar.B()) == null) {
            return false;
        }
        float fB = gVarB.b();
        float fC = gVarB.c();
        float fA = gVarB.a();
        return fB - fC > 0.0f && fA >= fC && fA <= fB;
    }

    public static boolean b0(r.v vVar, View view) {
        if (vVar == null || view == null || !vVar.b0()) {
            return false;
        }
        return vVar.X() || c0(vVar);
    }

    public static boolean c0(r.v vVar) {
        if (vVar == null) {
            return false;
        }
        if (vVar.O() || vVar.V() || vVar.R()) {
            return true;
        }
        List listI = vVar.i();
        return listI.contains(16) || listI.contains(32) || listI.contains(1);
    }

    public static boolean d0(r.v vVar, View view) {
        int iR;
        if (vVar == null || view == null || (iR = androidx.core.view.Z.r(view)) == 4) {
            return false;
        }
        if (iR != 2 || vVar.p() > 0) {
            return Z(vVar) || Y(vVar) || vVar.M() || X(vVar, view);
        }
        return false;
    }

    private static String e0(StringBuilder sb) {
        int length = sb.length();
        if (length > 0) {
            sb.delete(length - f7487w, length);
        }
        return sb.toString();
    }

    private void f0(View view) {
        if (this.f7489r.hasMessages(1, view)) {
            this.f7489r.removeMessages(1, view);
        }
        this.f7489r.sendMessageDelayed(this.f7489r.obtainMessage(1, view), 200L);
    }

    public static void g0(View view, boolean z3, int i3) {
        if (androidx.core.view.Z.C(view)) {
            return;
        }
        if (view.getTag(AbstractC0505m.f9234g) == null && view.getTag(AbstractC0505m.f9235h) == null && view.getTag(AbstractC0505m.f9228a) == null && view.getTag(AbstractC0505m.f9247t) == null && view.getTag(AbstractC0505m.f9230c) == null && view.getTag(AbstractC0505m.f9233f) == null && view.getTag(AbstractC0505m.f9253z) == null) {
            return;
        }
        androidx.core.view.Z.X(view, new C0433h0(view, z3, i3));
    }

    public static void h0(r.v vVar, d dVar, Context context) {
        if (dVar == null) {
            dVar = d.NONE;
        }
        vVar.p0(d.e(dVar));
        if (dVar.equals(d.LINK)) {
            vVar.F0(context.getString(AbstractC0508p.f9296w));
            return;
        }
        if (dVar.equals(d.IMAGE)) {
            vVar.F0(context.getString(AbstractC0508p.f9294u));
            return;
        }
        if (dVar.equals(d.IMAGEBUTTON)) {
            vVar.F0(context.getString(AbstractC0508p.f9295v));
            vVar.q0(true);
            return;
        }
        if (dVar.equals(d.BUTTON)) {
            vVar.q0(true);
            return;
        }
        if (dVar.equals(d.TOGGLEBUTTON)) {
            vVar.q0(true);
            vVar.n0(true);
            return;
        }
        if (dVar.equals(d.SUMMARY)) {
            vVar.F0(context.getString(AbstractC0508p.f9270I));
            return;
        }
        if (dVar.equals(d.HEADER)) {
            vVar.x0(true);
            return;
        }
        if (dVar.equals(d.ALERT)) {
            vVar.F0(context.getString(AbstractC0508p.f9274a));
            return;
        }
        if (dVar.equals(d.COMBOBOX)) {
            vVar.F0(context.getString(AbstractC0508p.f9293t));
            return;
        }
        if (dVar.equals(d.MENU)) {
            vVar.F0(context.getString(AbstractC0508p.f9297x));
            return;
        }
        if (dVar.equals(d.MENUBAR)) {
            vVar.F0(context.getString(AbstractC0508p.f9298y));
            return;
        }
        if (dVar.equals(d.MENUITEM)) {
            vVar.F0(context.getString(AbstractC0508p.f9299z));
            return;
        }
        if (dVar.equals(d.PROGRESSBAR)) {
            vVar.F0(context.getString(AbstractC0508p.f9262A));
            return;
        }
        if (dVar.equals(d.RADIOGROUP)) {
            vVar.F0(context.getString(AbstractC0508p.f9263B));
            return;
        }
        if (dVar.equals(d.SCROLLBAR)) {
            vVar.F0(context.getString(AbstractC0508p.f9265D));
            return;
        }
        if (dVar.equals(d.SPINBUTTON)) {
            vVar.F0(context.getString(AbstractC0508p.f9266E));
            return;
        }
        if (dVar.equals(d.TAB)) {
            vVar.F0(context.getString(AbstractC0508p.f9264C));
            return;
        }
        if (dVar.equals(d.TABLIST)) {
            vVar.F0(context.getString(AbstractC0508p.f9271J));
        } else if (dVar.equals(d.TIMER)) {
            vVar.F0(context.getString(AbstractC0508p.f9272K));
        } else if (dVar.equals(d.TOOLBAR)) {
            vVar.F0(context.getString(AbstractC0508p.f9273L));
        }
    }

    private static void i0(r.v vVar, ReadableMap readableMap, Context context) {
        ReadableMapKeySetIterator readableMapKeySetIteratorKeySetIterator = readableMap.keySetIterator();
        while (readableMapKeySetIteratorKeySetIterator.hasNextKey()) {
            String strNextKey = readableMapKeySetIteratorKeySetIterator.nextKey();
            Dynamic dynamic = readableMap.getDynamic(strNextKey);
            if (strNextKey.equals("selected") && dynamic.getType() == ReadableType.Boolean) {
                vVar.I0(dynamic.asBoolean());
            } else if (strNextKey.equals("disabled") && dynamic.getType() == ReadableType.Boolean) {
                vVar.u0(!dynamic.asBoolean());
            } else if (strNextKey.equals("checked") && dynamic.getType() == ReadableType.Boolean) {
                boolean zAsBoolean = dynamic.asBoolean();
                vVar.n0(true);
                vVar.o0(zAsBoolean);
            }
        }
    }

    @Override // x.AbstractC0766a
    protected void A(List list) {
    }

    @Override // x.AbstractC0766a
    protected boolean H(int i3, int i4, Bundle bundle) {
        return false;
    }

    @Override // x.AbstractC0766a
    protected void L(int i3, r.v vVar) {
        vVar.t0("");
        vVar.l0(new Rect(0, 0, 1, 1));
    }

    protected View V() {
        return this.f7488q;
    }

    @Override // x.AbstractC0766a, androidx.core.view.C0237a
    public r.w b(View view) {
        return null;
    }

    @Override // x.AbstractC0766a, androidx.core.view.C0237a
    public void f(View view, AccessibilityEvent accessibilityEvent) {
        super.f(view, accessibilityEvent);
        ReadableMap readableMap = (ReadableMap) view.getTag(AbstractC0505m.f9237j);
        if (readableMap != null && readableMap.hasKey("min") && readableMap.hasKey("now") && readableMap.hasKey("max")) {
            Dynamic dynamic = readableMap.getDynamic("min");
            Dynamic dynamic2 = readableMap.getDynamic("now");
            Dynamic dynamic3 = readableMap.getDynamic("max");
            if (dynamic != null) {
                ReadableType type = dynamic.getType();
                ReadableType readableType = ReadableType.Number;
                if (type == readableType && dynamic2 != null && dynamic2.getType() == readableType && dynamic3 != null && dynamic3.getType() == readableType) {
                    int iAsInt = dynamic.asInt();
                    int iAsInt2 = dynamic2.asInt();
                    int iAsInt3 = dynamic3.asInt();
                    if (iAsInt3 <= iAsInt || iAsInt2 < iAsInt || iAsInt3 < iAsInt2) {
                        return;
                    }
                    accessibilityEvent.setItemCount(iAsInt3 - iAsInt);
                    accessibilityEvent.setCurrentItemIndex(iAsInt2);
                }
            }
        }
    }

    @Override // x.AbstractC0766a, androidx.core.view.C0237a
    public void g(View view, r.v vVar) {
        super.g(view, vVar);
        if (view.getTag(AbstractC0505m.f9236i) != null) {
            vVar.a(((Boolean) view.getTag(AbstractC0505m.f9236i)).booleanValue() ? 524288 : 262144);
        }
        d dVarD = d.d(view);
        String str = (String) view.getTag(AbstractC0505m.f9231d);
        if (dVarD != null) {
            h0(vVar, dVarD, view.getContext());
        }
        if (str != null) {
            vVar.M0(str);
        }
        Object tag = view.getTag(AbstractC0505m.f9244q);
        if (tag != null) {
            View viewA = S1.a.a(view.getRootView(), (String) tag);
            this.f7491t = viewA;
            if (viewA != null) {
                vVar.y0(viewA);
            }
        }
        ReadableMap readableMap = (ReadableMap) view.getTag(AbstractC0505m.f9235h);
        if (readableMap != null) {
            i0(vVar, readableMap, view.getContext());
        }
        ReadableArray readableArray = (ReadableArray) view.getTag(AbstractC0505m.f9228a);
        ReadableMap readableMap2 = (ReadableMap) view.getTag(AbstractC0505m.f9230c);
        if (readableMap2 != null) {
            vVar.s0(v.f.a(readableMap2.getInt("rowIndex"), readableMap2.getInt("rowSpan"), readableMap2.getInt("columnIndex"), readableMap2.getInt("columnSpan"), readableMap2.getBoolean("heading")));
        }
        boolean z3 = true;
        if (readableArray != null) {
            for (int i3 = 0; i3 < readableArray.size(); i3++) {
                ReadableMap map = readableArray.getMap(i3);
                if (!map.hasKey("name")) {
                    throw new IllegalArgumentException("Unknown accessibility action.");
                }
                int iIntValue = f7486v;
                String string = map.hasKey("label") ? map.getString("label") : null;
                HashMap map2 = f7485u;
                if (map2.containsKey(map.getString("name"))) {
                    iIntValue = ((Integer) map2.get(map.getString("name"))).intValue();
                } else {
                    f7486v++;
                }
                this.f7490s.put(Integer.valueOf(iIntValue), map.getString("name"));
                vVar.b(new v.a(iIntValue, string));
            }
        }
        ReadableMap readableMap3 = (ReadableMap) view.getTag(AbstractC0505m.f9237j);
        if (readableMap3 != null && readableMap3.hasKey("min") && readableMap3.hasKey("now") && readableMap3.hasKey("max")) {
            Dynamic dynamic = readableMap3.getDynamic("min");
            Dynamic dynamic2 = readableMap3.getDynamic("now");
            Dynamic dynamic3 = readableMap3.getDynamic("max");
            if (dynamic != null) {
                ReadableType type = dynamic.getType();
                ReadableType readableType = ReadableType.Number;
                if (type == readableType && dynamic2 != null && dynamic2.getType() == readableType && dynamic3 != null && dynamic3.getType() == readableType) {
                    int iAsInt = dynamic.asInt();
                    int iAsInt2 = dynamic2.asInt();
                    int iAsInt3 = dynamic3.asInt();
                    if (iAsInt3 > iAsInt && iAsInt2 >= iAsInt && iAsInt3 >= iAsInt2) {
                        vVar.E0(v.g.d(0, iAsInt, iAsInt3, iAsInt2));
                    }
                }
            }
        }
        String str2 = (String) view.getTag(AbstractC0505m.f9247t);
        if (str2 != null) {
            vVar.N0(str2);
        }
        boolean z4 = TextUtils.isEmpty(vVar.u()) && TextUtils.isEmpty(vVar.E());
        if (readableArray == null && readableMap == null && tag == null && dVarD == null) {
            z3 = false;
        }
        if (z4 && z3) {
            vVar.t0(W(view, vVar));
        }
    }

    @Override // androidx.core.view.C0237a
    public boolean j(View view, int i3, Bundle bundle) {
        if (i3 == 524288) {
            view.setTag(AbstractC0505m.f9236i, Boolean.FALSE);
        }
        if (i3 == 262144) {
            view.setTag(AbstractC0505m.f9236i, Boolean.TRUE);
        }
        if (!this.f7490s.containsKey(Integer.valueOf(i3))) {
            return super.j(view, i3, bundle);
        }
        WritableMap writableMapCreateMap = Arguments.createMap();
        writableMapCreateMap.putString("actionName", (String) this.f7490s.get(Integer.valueOf(i3)));
        ReactContext reactContext = (ReactContext) view.getContext();
        if (reactContext.hasActiveReactInstance()) {
            int id = view.getId();
            int iE = H0.e(reactContext);
            UIManager uIManagerG = H0.g(reactContext, M1.a.a(id));
            if (uIManagerG != null) {
                uIManagerG.getEventDispatcher().b(new b(iE, id, writableMapCreateMap));
            }
        } else {
            ReactSoftExceptionLogger.logSoftException("ReactAccessibilityDelegate", new ReactNoCrashSoftException("Cannot get RCTEventEmitter, no CatalystInstance"));
        }
        d dVar = (d) view.getTag(AbstractC0505m.f9234g);
        ReadableMap readableMap = (ReadableMap) view.getTag(AbstractC0505m.f9237j);
        if (dVar != d.ADJUSTABLE) {
            return true;
        }
        if (i3 != v.a.f10470p.a() && i3 != v.a.f10471q.a()) {
            return true;
        }
        if (readableMap != null && !readableMap.hasKey("text")) {
            f0(view);
        }
        return super.j(view, i3, bundle);
    }

    public r.w j0(View view) {
        return super.b(view);
    }

    @Override // x.AbstractC0766a
    protected int z(float f3, float f4) {
        return Integer.MIN_VALUE;
    }
}
