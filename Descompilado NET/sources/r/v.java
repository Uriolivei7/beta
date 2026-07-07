package r;

import android.R;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import m.AbstractC0596b;
import r.y;

/* JADX INFO: loaded from: classes.dex */
public class v {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static int f10433d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AccessibilityNodeInfo f10434a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f10435b = -1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f10436c = -1;

    public static class a {

        /* JADX INFO: renamed from: A, reason: collision with root package name */
        public static final a f10437A;

        /* JADX INFO: renamed from: B, reason: collision with root package name */
        public static final a f10438B;

        /* JADX INFO: renamed from: C, reason: collision with root package name */
        public static final a f10439C;

        /* JADX INFO: renamed from: D, reason: collision with root package name */
        public static final a f10440D;

        /* JADX INFO: renamed from: E, reason: collision with root package name */
        public static final a f10441E;

        /* JADX INFO: renamed from: F, reason: collision with root package name */
        public static final a f10442F;

        /* JADX INFO: renamed from: G, reason: collision with root package name */
        public static final a f10443G;

        /* JADX INFO: renamed from: H, reason: collision with root package name */
        public static final a f10444H;

        /* JADX INFO: renamed from: I, reason: collision with root package name */
        public static final a f10445I;

        /* JADX INFO: renamed from: J, reason: collision with root package name */
        public static final a f10446J;

        /* JADX INFO: renamed from: K, reason: collision with root package name */
        public static final a f10447K;

        /* JADX INFO: renamed from: L, reason: collision with root package name */
        public static final a f10448L;

        /* JADX INFO: renamed from: M, reason: collision with root package name */
        public static final a f10449M;

        /* JADX INFO: renamed from: N, reason: collision with root package name */
        public static final a f10450N;

        /* JADX INFO: renamed from: O, reason: collision with root package name */
        public static final a f10451O;

        /* JADX INFO: renamed from: P, reason: collision with root package name */
        public static final a f10452P;

        /* JADX INFO: renamed from: Q, reason: collision with root package name */
        public static final a f10453Q;

        /* JADX INFO: renamed from: R, reason: collision with root package name */
        public static final a f10454R;

        /* JADX INFO: renamed from: S, reason: collision with root package name */
        public static final a f10455S;

        /* JADX INFO: renamed from: T, reason: collision with root package name */
        public static final a f10456T;

        /* JADX INFO: renamed from: U, reason: collision with root package name */
        public static final a f10457U;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public static final a f10458d = new a(1, null);

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        public static final a f10459e = new a(2, null);

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        public static final a f10460f = new a(4, null);

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        public static final a f10461g = new a(8, null);

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        public static final a f10462h = new a(16, null);

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        public static final a f10463i = new a(32, null);

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        public static final a f10464j = new a(64, null);

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        public static final a f10465k = new a(128, null);

        /* JADX INFO: renamed from: l, reason: collision with root package name */
        public static final a f10466l = new a(256, (CharSequence) null, y.b.class);

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        public static final a f10467m = new a(512, (CharSequence) null, y.b.class);

        /* JADX INFO: renamed from: n, reason: collision with root package name */
        public static final a f10468n = new a(1024, (CharSequence) null, y.c.class);

        /* JADX INFO: renamed from: o, reason: collision with root package name */
        public static final a f10469o = new a(2048, (CharSequence) null, y.c.class);

        /* JADX INFO: renamed from: p, reason: collision with root package name */
        public static final a f10470p = new a(4096, null);

        /* JADX INFO: renamed from: q, reason: collision with root package name */
        public static final a f10471q = new a(8192, null);

        /* JADX INFO: renamed from: r, reason: collision with root package name */
        public static final a f10472r = new a(16384, null);

        /* JADX INFO: renamed from: s, reason: collision with root package name */
        public static final a f10473s = new a(32768, null);

        /* JADX INFO: renamed from: t, reason: collision with root package name */
        public static final a f10474t = new a(65536, null);

        /* JADX INFO: renamed from: u, reason: collision with root package name */
        public static final a f10475u = new a(131072, (CharSequence) null, y.g.class);

        /* JADX INFO: renamed from: v, reason: collision with root package name */
        public static final a f10476v = new a(262144, null);

        /* JADX INFO: renamed from: w, reason: collision with root package name */
        public static final a f10477w = new a(524288, null);

        /* JADX INFO: renamed from: x, reason: collision with root package name */
        public static final a f10478x = new a(1048576, null);

        /* JADX INFO: renamed from: y, reason: collision with root package name */
        public static final a f10479y = new a(2097152, (CharSequence) null, y.h.class);

        /* JADX INFO: renamed from: z, reason: collision with root package name */
        public static final a f10480z;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Object f10481a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f10482b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Class f10483c;

        static {
            int i3 = Build.VERSION.SDK_INT;
            f10480z = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN, R.id.accessibilityActionShowOnScreen, null, null, null);
            f10437A = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION, R.id.accessibilityActionScrollToPosition, null, null, y.e.class);
            f10438B = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP, R.id.accessibilityActionScrollUp, null, null, null);
            f10439C = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT, R.id.accessibilityActionScrollLeft, null, null, null);
            f10440D = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN, R.id.accessibilityActionScrollDown, null, null, null);
            f10441E = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT, R.id.accessibilityActionScrollRight, null, null, null);
            f10442F = new a(i3 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_UP : null, R.id.accessibilityActionPageUp, null, null, null);
            f10443G = new a(i3 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_DOWN : null, R.id.accessibilityActionPageDown, null, null, null);
            f10444H = new a(i3 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT : null, R.id.accessibilityActionPageLeft, null, null, null);
            f10445I = new a(i3 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT : null, R.id.accessibilityActionPageRight, null, null, null);
            f10446J = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK, R.id.accessibilityActionContextClick, null, null, null);
            f10447K = new a(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS, R.id.accessibilityActionSetProgress, null, null, y.f.class);
            f10448L = new a(i3 >= 26 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW : null, R.id.accessibilityActionMoveWindow, null, null, y.d.class);
            f10449M = new a(i3 >= 28 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TOOLTIP : null, R.id.accessibilityActionShowTooltip, null, null, null);
            f10450N = new a(i3 >= 28 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_HIDE_TOOLTIP : null, R.id.accessibilityActionHideTooltip, null, null, null);
            f10451O = new a(i3 >= 30 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PRESS_AND_HOLD : null, R.id.accessibilityActionPressAndHold, null, null, null);
            f10452P = new a(i3 >= 30 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_IME_ENTER : null, R.id.accessibilityActionImeEnter, null, null, null);
            f10453Q = new a(i3 >= 32 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_START : null, R.id.accessibilityActionDragStart, null, null, null);
            f10454R = new a(i3 >= 32 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_DROP : null, R.id.accessibilityActionDragDrop, null, null, null);
            f10455S = new a(i3 >= 32 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_DRAG_CANCEL : null, R.id.accessibilityActionDragCancel, null, null, null);
            f10456T = new a(i3 >= 33 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TEXT_SUGGESTIONS : null, R.id.accessibilityActionShowTextSuggestions, null, null, null);
            f10457U = new a(i3 >= 34 ? d.a() : null, R.id.accessibilityActionScrollInDirection, null, null, null);
        }

        public a(int i3, CharSequence charSequence) {
            this(null, i3, charSequence, null, null);
        }

        public int a() {
            return ((AccessibilityNodeInfo.AccessibilityAction) this.f10481a).getId();
        }

        public CharSequence b() {
            return ((AccessibilityNodeInfo.AccessibilityAction) this.f10481a).getLabel();
        }

        public boolean c(View view, Bundle bundle) {
            return false;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof a)) {
                return false;
            }
            a aVar = (a) obj;
            Object obj2 = this.f10481a;
            return obj2 == null ? aVar.f10481a == null : obj2.equals(aVar.f10481a);
        }

        public int hashCode() {
            Object obj = this.f10481a;
            if (obj != null) {
                return obj.hashCode();
            }
            return 0;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("AccessibilityActionCompat: ");
            String strJ = v.j(this.f10482b);
            if (strJ.equals("ACTION_UNKNOWN") && b() != null) {
                strJ = b().toString();
            }
            sb.append(strJ);
            return sb.toString();
        }

        public a(int i3, CharSequence charSequence, y yVar) {
            this(null, i3, charSequence, yVar, null);
        }

        a(Object obj) {
            this(obj, 0, null, null, null);
        }

        private a(int i3, CharSequence charSequence, Class cls) {
            this(null, i3, charSequence, null, cls);
        }

        a(Object obj, int i3, CharSequence charSequence, y yVar, Class cls) {
            this.f10482b = i3;
            if (obj == null) {
                this.f10481a = new AccessibilityNodeInfo.AccessibilityAction(i3, charSequence);
            } else {
                this.f10481a = obj;
            }
            this.f10483c = cls;
        }
    }

    private static class b {
        public static Object a(int i3, float f3, float f4, float f5) {
            return new AccessibilityNodeInfo.RangeInfo(i3, f3, f4, f5);
        }

        public static CharSequence b(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getStateDescription();
        }

        public static void c(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
            accessibilityNodeInfo.setStateDescription(charSequence);
        }
    }

    private static class c {
        public static f a(boolean z3, int i3, int i4, int i5, int i6, boolean z4, String str, String str2) {
            return new f(new AccessibilityNodeInfo.CollectionItemInfo.Builder().setHeading(z3).setColumnIndex(i3).setRowIndex(i4).setColumnSpan(i5).setRowSpan(i6).setSelected(z4).setRowTitle(str).setColumnTitle(str2).build());
        }

        public static v b(AccessibilityNodeInfo accessibilityNodeInfo, int i3, int i4) {
            return v.R0(accessibilityNodeInfo.getChild(i3, i4));
        }

        public static String c(Object obj) {
            return ((AccessibilityNodeInfo.CollectionItemInfo) obj).getColumnTitle();
        }

        public static String d(Object obj) {
            return ((AccessibilityNodeInfo.CollectionItemInfo) obj).getRowTitle();
        }

        public static AccessibilityNodeInfo.ExtraRenderingInfo e(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getExtraRenderingInfo();
        }

        public static v f(AccessibilityNodeInfo accessibilityNodeInfo, int i3) {
            return v.R0(accessibilityNodeInfo.getParent(i3));
        }

        public static String g(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getUniqueId();
        }

        public static boolean h(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isTextSelectable();
        }

        public static void i(AccessibilityNodeInfo accessibilityNodeInfo, boolean z3) {
            accessibilityNodeInfo.setTextSelectable(z3);
        }

        public static void j(AccessibilityNodeInfo accessibilityNodeInfo, String str) {
            accessibilityNodeInfo.setUniqueId(str);
        }
    }

    private static class d {
        public static AccessibilityNodeInfo.AccessibilityAction a() {
            return AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_IN_DIRECTION;
        }

        public static void b(AccessibilityNodeInfo accessibilityNodeInfo, Rect rect) {
            accessibilityNodeInfo.getBoundsInWindow(rect);
        }

        public static CharSequence c(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getContainerTitle();
        }

        public static long d(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getMinDurationBetweenContentChanges().toMillis();
        }

        public static boolean e(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.hasRequestInitialAccessibilityFocus();
        }

        public static boolean f(AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isAccessibilityDataSensitive();
        }

        public static void g(AccessibilityNodeInfo accessibilityNodeInfo, boolean z3) {
            accessibilityNodeInfo.setAccessibilityDataSensitive(z3);
        }

        public static void h(AccessibilityNodeInfo accessibilityNodeInfo, Rect rect) {
            accessibilityNodeInfo.setBoundsInWindow(rect);
        }

        public static void i(AccessibilityNodeInfo accessibilityNodeInfo, CharSequence charSequence) {
            accessibilityNodeInfo.setContainerTitle(charSequence);
        }

        public static void j(AccessibilityNodeInfo accessibilityNodeInfo, long j3) {
            accessibilityNodeInfo.setMinDurationBetweenContentChanges(Duration.ofMillis(j3));
        }

        public static void k(AccessibilityNodeInfo accessibilityNodeInfo, View view, boolean z3) {
            accessibilityNodeInfo.setQueryFromAppProcessEnabled(view, z3);
        }

        public static void l(AccessibilityNodeInfo accessibilityNodeInfo, boolean z3) {
            accessibilityNodeInfo.setRequestInitialAccessibilityFocus(z3);
        }
    }

    public static class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Object f10484a;

        e(Object obj) {
            this.f10484a = obj;
        }

        public static e a(int i3, int i4, boolean z3) {
            return new e(AccessibilityNodeInfo.CollectionInfo.obtain(i3, i4, z3));
        }
    }

    public static class f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Object f10485a;

        f(Object obj) {
            this.f10485a = obj;
        }

        public static f a(int i3, int i4, int i5, int i6, boolean z3) {
            return new f(AccessibilityNodeInfo.CollectionItemInfo.obtain(i3, i4, i5, i6, z3));
        }
    }

    @Deprecated
    public v(Object obj) {
        this.f10434a = (AccessibilityNodeInfo) obj;
    }

    private SparseArray C(View view) {
        return (SparseArray) view.getTag(AbstractC0596b.f9731I);
    }

    private boolean I() {
        return !h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").isEmpty();
    }

    private int J(ClickableSpan clickableSpan, SparseArray sparseArray) {
        if (sparseArray != null) {
            for (int i3 = 0; i3 < sparseArray.size(); i3++) {
                if (clickableSpan.equals((ClickableSpan) ((WeakReference) sparseArray.valueAt(i3)).get())) {
                    return sparseArray.keyAt(i3);
                }
            }
        }
        int i4 = f10433d;
        f10433d = i4 + 1;
        return i4;
    }

    public static v Q0(AccessibilityNodeInfo accessibilityNodeInfo) {
        return new v(accessibilityNodeInfo);
    }

    static v R0(Object obj) {
        if (obj != null) {
            return new v(obj);
        }
        return null;
    }

    public static v c0() {
        return Q0(AccessibilityNodeInfo.obtain());
    }

    public static v d0(View view) {
        return Q0(AccessibilityNodeInfo.obtain(view));
    }

    private void e(ClickableSpan clickableSpan, Spanned spanned, int i3) {
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").add(Integer.valueOf(spanned.getSpanStart(clickableSpan)));
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY").add(Integer.valueOf(spanned.getSpanEnd(clickableSpan)));
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY").add(Integer.valueOf(spanned.getSpanFlags(clickableSpan)));
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY").add(Integer.valueOf(i3));
    }

    public static v e0(v vVar) {
        return Q0(AccessibilityNodeInfo.obtain(vVar.f10434a));
    }

    private void g() {
        this.f10434a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
        this.f10434a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
        this.f10434a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
        this.f10434a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
    }

    private List h(String str) {
        ArrayList<Integer> integerArrayList = this.f10434a.getExtras().getIntegerArrayList(str);
        if (integerArrayList != null) {
            return integerArrayList;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        this.f10434a.getExtras().putIntegerArrayList(str, arrayList);
        return arrayList;
    }

    private void i0(View view) {
        SparseArray sparseArrayC = C(view);
        if (sparseArrayC != null) {
            ArrayList arrayList = new ArrayList();
            for (int i3 = 0; i3 < sparseArrayC.size(); i3++) {
                if (((WeakReference) sparseArrayC.valueAt(i3)).get() == null) {
                    arrayList.add(Integer.valueOf(i3));
                }
            }
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                sparseArrayC.remove(((Integer) arrayList.get(i4)).intValue());
            }
        }
    }

    static String j(int i3) {
        if (i3 == 1) {
            return "ACTION_FOCUS";
        }
        if (i3 == 2) {
            return "ACTION_CLEAR_FOCUS";
        }
        switch (i3) {
            case 4:
                return "ACTION_SELECT";
            case 8:
                return "ACTION_CLEAR_SELECTION";
            case 16:
                return "ACTION_CLICK";
            case 32:
                return "ACTION_LONG_CLICK";
            case 64:
                return "ACTION_ACCESSIBILITY_FOCUS";
            case 128:
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            case 256:
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            case 512:
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            case 1024:
                return "ACTION_NEXT_HTML_ELEMENT";
            case 2048:
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            case 4096:
                return "ACTION_SCROLL_FORWARD";
            case 8192:
                return "ACTION_SCROLL_BACKWARD";
            case 16384:
                return "ACTION_COPY";
            case 32768:
                return "ACTION_PASTE";
            case 65536:
                return "ACTION_CUT";
            case 131072:
                return "ACTION_SET_SELECTION";
            case 262144:
                return "ACTION_EXPAND";
            case 524288:
                return "ACTION_COLLAPSE";
            case 2097152:
                return "ACTION_SET_TEXT";
            case R.id.accessibilityActionMoveWindow:
                return "ACTION_MOVE_WINDOW";
            case R.id.accessibilityActionScrollInDirection:
                return "ACTION_SCROLL_IN_DIRECTION";
            default:
                switch (i3) {
                    case R.id.accessibilityActionShowOnScreen:
                        return "ACTION_SHOW_ON_SCREEN";
                    case R.id.accessibilityActionScrollToPosition:
                        return "ACTION_SCROLL_TO_POSITION";
                    case R.id.accessibilityActionScrollUp:
                        return "ACTION_SCROLL_UP";
                    case R.id.accessibilityActionScrollLeft:
                        return "ACTION_SCROLL_LEFT";
                    case R.id.accessibilityActionScrollDown:
                        return "ACTION_SCROLL_DOWN";
                    case R.id.accessibilityActionScrollRight:
                        return "ACTION_SCROLL_RIGHT";
                    case R.id.accessibilityActionContextClick:
                        return "ACTION_CONTEXT_CLICK";
                    case R.id.accessibilityActionSetProgress:
                        return "ACTION_SET_PROGRESS";
                    default:
                        switch (i3) {
                            case R.id.accessibilityActionShowTooltip:
                                return "ACTION_SHOW_TOOLTIP";
                            case R.id.accessibilityActionHideTooltip:
                                return "ACTION_HIDE_TOOLTIP";
                            case R.id.accessibilityActionPageUp:
                                return "ACTION_PAGE_UP";
                            case R.id.accessibilityActionPageDown:
                                return "ACTION_PAGE_DOWN";
                            case R.id.accessibilityActionPageLeft:
                                return "ACTION_PAGE_LEFT";
                            case R.id.accessibilityActionPageRight:
                                return "ACTION_PAGE_RIGHT";
                            case R.id.accessibilityActionPressAndHold:
                                return "ACTION_PRESS_AND_HOLD";
                            default:
                                switch (i3) {
                                    case R.id.accessibilityActionImeEnter:
                                        return "ACTION_IME_ENTER";
                                    case R.id.accessibilityActionDragStart:
                                        return "ACTION_DRAG_START";
                                    case R.id.accessibilityActionDragDrop:
                                        return "ACTION_DRAG_DROP";
                                    case R.id.accessibilityActionDragCancel:
                                        return "ACTION_DRAG_CANCEL";
                                    default:
                                        return "ACTION_UNKNOWN";
                                }
                        }
                }
        }
    }

    private void k0(int i3, boolean z3) {
        Bundle bundleW = w();
        if (bundleW != null) {
            int i4 = bundleW.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0) & (~i3);
            if (!z3) {
                i3 = 0;
            }
            bundleW.putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", i3 | i4);
        }
    }

    private boolean l(int i3) {
        Bundle bundleW = w();
        return bundleW != null && (bundleW.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0) & i3) == i3;
    }

    public static ClickableSpan[] r(CharSequence charSequence) {
        if (charSequence instanceof Spanned) {
            return (ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class);
        }
        return null;
    }

    private SparseArray z(View view) {
        SparseArray sparseArrayC = C(view);
        if (sparseArrayC != null) {
            return sparseArrayC;
        }
        SparseArray sparseArray = new SparseArray();
        view.setTag(AbstractC0596b.f9731I, sparseArray);
        return sparseArray;
    }

    public CharSequence A() {
        return this.f10434a.getPackageName();
    }

    public void A0(CharSequence charSequence) {
        this.f10434a.setPackageName(charSequence);
    }

    public g B() {
        AccessibilityNodeInfo.RangeInfo rangeInfo = this.f10434a.getRangeInfo();
        if (rangeInfo != null) {
            return new g(rangeInfo);
        }
        return null;
    }

    public void B0(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.f10434a.setPaneTitle(charSequence);
        } else {
            this.f10434a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY", charSequence);
        }
    }

    public void C0(View view) {
        this.f10435b = -1;
        this.f10434a.setParent(view);
    }

    public CharSequence D() {
        return Build.VERSION.SDK_INT >= 30 ? b.b(this.f10434a) : this.f10434a.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.STATE_DESCRIPTION_KEY");
    }

    public void D0(View view, int i3) {
        this.f10435b = i3;
        this.f10434a.setParent(view, i3);
    }

    public CharSequence E() {
        if (!I()) {
            return this.f10434a.getText();
        }
        List listH = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
        List listH2 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
        List listH3 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
        List listH4 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
        SpannableString spannableString = new SpannableString(TextUtils.substring(this.f10434a.getText(), 0, this.f10434a.getText().length()));
        for (int i3 = 0; i3 < listH.size(); i3++) {
            spannableString.setSpan(new C0658a(((Integer) listH4.get(i3)).intValue(), this, w().getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY")), ((Integer) listH.get(i3)).intValue(), ((Integer) listH2.get(i3)).intValue(), ((Integer) listH3.get(i3)).intValue());
        }
        return spannableString;
    }

    public void E0(g gVar) {
        this.f10434a.setRangeInfo((AccessibilityNodeInfo.RangeInfo) gVar.f10486a);
    }

    public CharSequence F() {
        return Build.VERSION.SDK_INT >= 28 ? this.f10434a.getTooltipText() : this.f10434a.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY");
    }

    public void F0(CharSequence charSequence) {
        this.f10434a.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", charSequence);
    }

    public String G() {
        return Build.VERSION.SDK_INT >= 33 ? c.g(this.f10434a) : this.f10434a.getExtras().getString("androidx.view.accessibility.AccessibilityNodeInfoCompat.UNIQUE_ID_KEY");
    }

    public void G0(boolean z3) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.f10434a.setScreenReaderFocusable(z3);
        } else {
            k0(1, z3);
        }
    }

    public String H() {
        return this.f10434a.getViewIdResourceName();
    }

    public void H0(boolean z3) {
        this.f10434a.setScrollable(z3);
    }

    public void I0(boolean z3) {
        this.f10434a.setSelected(z3);
    }

    public void J0(View view) {
        this.f10436c = -1;
        this.f10434a.setSource(view);
    }

    public boolean K() {
        return Build.VERSION.SDK_INT >= 34 ? d.f(this.f10434a) : l(64);
    }

    public void K0(View view, int i3) {
        this.f10436c = i3;
        this.f10434a.setSource(view, i3);
    }

    public boolean L() {
        return this.f10434a.isAccessibilityFocused();
    }

    public void L0(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 30) {
            b.c(this.f10434a, charSequence);
        } else {
            this.f10434a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.STATE_DESCRIPTION_KEY", charSequence);
        }
    }

    public boolean M() {
        return this.f10434a.isCheckable();
    }

    public void M0(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.f10434a.setTooltipText(charSequence);
        } else {
            this.f10434a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.TOOLTIP_TEXT_KEY", charSequence);
        }
    }

    public boolean N() {
        return this.f10434a.isChecked();
    }

    public void N0(String str) {
        this.f10434a.setViewIdResourceName(str);
    }

    public boolean O() {
        return this.f10434a.isClickable();
    }

    public void O0(boolean z3) {
        this.f10434a.setVisibleToUser(z3);
    }

    public boolean P() {
        return this.f10434a.isContextClickable();
    }

    public AccessibilityNodeInfo P0() {
        return this.f10434a;
    }

    public boolean Q() {
        return this.f10434a.isEnabled();
    }

    public boolean R() {
        return this.f10434a.isFocusable();
    }

    public boolean S() {
        return this.f10434a.isFocused();
    }

    public boolean T() {
        return l(67108864);
    }

    public boolean U() {
        return this.f10434a.isImportantForAccessibility();
    }

    public boolean V() {
        return this.f10434a.isLongClickable();
    }

    public boolean W() {
        return this.f10434a.isPassword();
    }

    public boolean X() {
        return Build.VERSION.SDK_INT >= 28 ? this.f10434a.isScreenReaderFocusable() : l(1);
    }

    public boolean Y() {
        return this.f10434a.isScrollable();
    }

    public boolean Z() {
        return this.f10434a.isSelected();
    }

    public void a(int i3) {
        this.f10434a.addAction(i3);
    }

    public boolean a0() {
        return Build.VERSION.SDK_INT >= 33 ? c.h(this.f10434a) : l(8388608);
    }

    public void b(a aVar) {
        this.f10434a.addAction((AccessibilityNodeInfo.AccessibilityAction) aVar.f10481a);
    }

    public boolean b0() {
        return this.f10434a.isVisibleToUser();
    }

    public void c(View view) {
        this.f10434a.addChild(view);
    }

    public void d(View view, int i3) {
        this.f10434a.addChild(view, i3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof v)) {
            return false;
        }
        v vVar = (v) obj;
        AccessibilityNodeInfo accessibilityNodeInfo = this.f10434a;
        if (accessibilityNodeInfo == null) {
            if (vVar.f10434a != null) {
                return false;
            }
        } else if (!accessibilityNodeInfo.equals(vVar.f10434a)) {
            return false;
        }
        return this.f10436c == vVar.f10436c && this.f10435b == vVar.f10435b;
    }

    public void f(CharSequence charSequence, View view) {
        if (Build.VERSION.SDK_INT < 26) {
            g();
            i0(view);
            ClickableSpan[] clickableSpanArrR = r(charSequence);
            if (clickableSpanArrR == null || clickableSpanArrR.length <= 0) {
                return;
            }
            w().putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY", AbstractC0596b.f9742a);
            SparseArray sparseArrayZ = z(view);
            for (int i3 = 0; i3 < clickableSpanArrR.length; i3++) {
                int iJ = J(clickableSpanArrR[i3], sparseArrayZ);
                sparseArrayZ.put(iJ, new WeakReference(clickableSpanArrR[i3]));
                e(clickableSpanArrR[i3], (Spanned) charSequence, iJ);
            }
        }
    }

    public boolean f0(int i3, Bundle bundle) {
        return this.f10434a.performAction(i3, bundle);
    }

    public boolean h0(a aVar) {
        return this.f10434a.removeAction((AccessibilityNodeInfo.AccessibilityAction) aVar.f10481a);
    }

    public int hashCode() {
        AccessibilityNodeInfo accessibilityNodeInfo = this.f10434a;
        if (accessibilityNodeInfo == null) {
            return 0;
        }
        return accessibilityNodeInfo.hashCode();
    }

    public List i() {
        List<AccessibilityNodeInfo.AccessibilityAction> actionList = this.f10434a.getActionList();
        if (actionList == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        int size = actionList.size();
        for (int i3 = 0; i3 < size; i3++) {
            arrayList.add(new a(actionList.get(i3)));
        }
        return arrayList;
    }

    public void j0(boolean z3) {
        this.f10434a.setAccessibilityFocused(z3);
    }

    public int k() {
        return this.f10434a.getActions();
    }

    public void l0(Rect rect) {
        this.f10434a.setBoundsInParent(rect);
    }

    public void m(Rect rect) {
        this.f10434a.getBoundsInParent(rect);
    }

    public void m0(Rect rect) {
        this.f10434a.setBoundsInScreen(rect);
    }

    public void n(Rect rect) {
        this.f10434a.getBoundsInScreen(rect);
    }

    public void n0(boolean z3) {
        this.f10434a.setCheckable(z3);
    }

    public void o(Rect rect) {
        if (Build.VERSION.SDK_INT >= 34) {
            d.b(this.f10434a, rect);
            return;
        }
        Rect rect2 = (Rect) this.f10434a.getExtras().getParcelable("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOUNDS_IN_WINDOW_KEY");
        if (rect2 != null) {
            rect.set(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }
    }

    public void o0(boolean z3) {
        this.f10434a.setChecked(z3);
    }

    public int p() {
        return this.f10434a.getChildCount();
    }

    public void p0(CharSequence charSequence) {
        this.f10434a.setClassName(charSequence);
    }

    public CharSequence q() {
        return this.f10434a.getClassName();
    }

    public void q0(boolean z3) {
        this.f10434a.setClickable(z3);
    }

    public void r0(Object obj) {
        this.f10434a.setCollectionInfo(obj == null ? null : (AccessibilityNodeInfo.CollectionInfo) ((e) obj).f10484a);
    }

    public e s() {
        AccessibilityNodeInfo.CollectionInfo collectionInfo = this.f10434a.getCollectionInfo();
        if (collectionInfo != null) {
            return new e(collectionInfo);
        }
        return null;
    }

    public void s0(Object obj) {
        this.f10434a.setCollectionItemInfo(obj == null ? null : (AccessibilityNodeInfo.CollectionItemInfo) ((f) obj).f10485a);
    }

    public CharSequence t() {
        return Build.VERSION.SDK_INT >= 34 ? d.c(this.f10434a) : this.f10434a.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.CONTAINER_TITLE_KEY");
    }

    public void t0(CharSequence charSequence) {
        this.f10434a.setContentDescription(charSequence);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        Rect rect = new Rect();
        m(rect);
        sb.append("; boundsInParent: " + rect);
        n(rect);
        sb.append("; boundsInScreen: " + rect);
        o(rect);
        sb.append("; boundsInWindow: " + rect);
        sb.append("; packageName: ");
        sb.append(A());
        sb.append("; className: ");
        sb.append(q());
        sb.append("; text: ");
        sb.append(E());
        sb.append("; error: ");
        sb.append(v());
        sb.append("; maxTextLength: ");
        sb.append(y());
        sb.append("; stateDescription: ");
        sb.append(D());
        sb.append("; contentDescription: ");
        sb.append(u());
        sb.append("; tooltipText: ");
        sb.append(F());
        sb.append("; viewIdResName: ");
        sb.append(H());
        sb.append("; uniqueId: ");
        sb.append(G());
        sb.append("; checkable: ");
        sb.append(M());
        sb.append("; checked: ");
        sb.append(N());
        sb.append("; focusable: ");
        sb.append(R());
        sb.append("; focused: ");
        sb.append(S());
        sb.append("; selected: ");
        sb.append(Z());
        sb.append("; clickable: ");
        sb.append(O());
        sb.append("; longClickable: ");
        sb.append(V());
        sb.append("; contextClickable: ");
        sb.append(P());
        sb.append("; enabled: ");
        sb.append(Q());
        sb.append("; password: ");
        sb.append(W());
        sb.append("; scrollable: " + Y());
        sb.append("; containerTitle: ");
        sb.append(t());
        sb.append("; granularScrollingSupported: ");
        sb.append(T());
        sb.append("; importantForAccessibility: ");
        sb.append(U());
        sb.append("; visible: ");
        sb.append(b0());
        sb.append("; isTextSelectable: ");
        sb.append(a0());
        sb.append("; accessibilityDataSensitive: ");
        sb.append(K());
        sb.append("; [");
        List listI = i();
        for (int i3 = 0; i3 < listI.size(); i3++) {
            a aVar = (a) listI.get(i3);
            String strJ = j(aVar.a());
            if (strJ.equals("ACTION_UNKNOWN") && aVar.b() != null) {
                strJ = aVar.b().toString();
            }
            sb.append(strJ);
            if (i3 != listI.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public CharSequence u() {
        return this.f10434a.getContentDescription();
    }

    public void u0(boolean z3) {
        this.f10434a.setEnabled(z3);
    }

    public CharSequence v() {
        return this.f10434a.getError();
    }

    public void v0(boolean z3) {
        this.f10434a.setFocusable(z3);
    }

    public Bundle w() {
        return this.f10434a.getExtras();
    }

    public void w0(boolean z3) {
        this.f10434a.setFocused(z3);
    }

    public CharSequence x() {
        return Build.VERSION.SDK_INT >= 26 ? this.f10434a.getHintText() : this.f10434a.getExtras().getCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY");
    }

    public void x0(boolean z3) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.f10434a.setHeading(z3);
        } else {
            k0(2, z3);
        }
    }

    public int y() {
        return this.f10434a.getMaxTextLength();
    }

    public void y0(View view) {
        this.f10434a.setLabeledBy(view);
    }

    public void z0(boolean z3) {
        this.f10434a.setLongClickable(z3);
    }

    public static class g {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Object f10486a;

        g(Object obj) {
            this.f10486a = obj;
        }

        public static g d(int i3, float f3, float f4, float f5) {
            return new g(AccessibilityNodeInfo.RangeInfo.obtain(i3, f3, f4, f5));
        }

        public float a() {
            return ((AccessibilityNodeInfo.RangeInfo) this.f10486a).getCurrent();
        }

        public float b() {
            return ((AccessibilityNodeInfo.RangeInfo) this.f10486a).getMax();
        }

        public float c() {
            return ((AccessibilityNodeInfo.RangeInfo) this.f10486a).getMin();
        }

        public g(int i3, float f3, float f4, float f5) {
            if (Build.VERSION.SDK_INT >= 30) {
                this.f10486a = b.a(i3, f3, f4, f5);
            } else {
                this.f10486a = AccessibilityNodeInfo.RangeInfo.obtain(i3, f3, f4, f5);
            }
        }
    }

    private v(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.f10434a = accessibilityNodeInfo;
    }

    public void g0() {
    }
}
