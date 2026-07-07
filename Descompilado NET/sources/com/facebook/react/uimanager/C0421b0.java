package com.facebook.react.uimanager;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.UiThreadUtil;
import d2.C0518a;
import d2.C0519b;
import g1.C0542a;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: renamed from: com.facebook.react.uimanager.b0, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0421b0 {

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private static final String f7446l = "b0";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final boolean f7447a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SparseArray f7448b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final SparseArray f7449c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final SparseBooleanArray f7450d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final U0 f7451e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final K1.a f7452f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final RootViewManager f7453g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final Q1.e f7454h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private final RectF f7455i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private volatile boolean f7456j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private HashMap f7457k;

    /* JADX INFO: renamed from: com.facebook.react.uimanager.b0$a */
    class a implements Q1.f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroupManager f7458a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewGroup f7459b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f7460c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ Set f7461d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f7462e;

        a(ViewGroupManager viewGroupManager, ViewGroup viewGroup, View view, Set set, int i3) {
            this.f7458a = viewGroupManager;
            this.f7459b = viewGroup;
            this.f7460c = view;
            this.f7461d = set;
            this.f7462e = i3;
        }

        @Override // Q1.f
        public void a() {
            UiThreadUtil.assertOnUiThread();
            this.f7458a.removeView(this.f7459b, this.f7460c);
            C0421b0.this.m(this.f7460c);
            this.f7461d.remove(Integer.valueOf(this.f7460c.getId()));
            if (this.f7461d.isEmpty()) {
                C0421b0.this.f7457k.remove(Integer.valueOf(this.f7462e));
            }
        }
    }

    public C0421b0(U0 u02) {
        this(u02, new RootViewManager());
    }

    private void B(View view, int i3, int i4, int i5, int i6) {
        if (this.f7456j && this.f7454h.h(view)) {
            this.f7454h.b(view, i3, i4, i5, i6);
        } else {
            view.layout(i3, i4, i5 + i3, i6 + i4);
        }
    }

    private boolean d(int[] iArr, int i3) {
        if (iArr == null) {
            return false;
        }
        for (int i4 : iArr) {
            if (i4 == i3) {
                return true;
            }
        }
        return false;
    }

    private void g(View view, int[] iArr) {
        this.f7455i.set(0.0f, 0.0f, view.getWidth(), view.getHeight());
        r(view, this.f7455i);
        iArr[0] = Math.round(this.f7455i.left);
        iArr[1] = Math.round(this.f7455i.top);
        RectF rectF = this.f7455i;
        iArr[2] = Math.round(rectF.right - rectF.left);
        RectF rectF2 = this.f7455i;
        iArr[3] = Math.round(rectF2.bottom - rectF2.top);
    }

    private static String i(ViewGroup viewGroup, ViewGroupManager viewGroupManager, int[] iArr, O0[] o0Arr, int[] iArr2) {
        StringBuilder sb = new StringBuilder();
        if (viewGroup != null) {
            sb.append("View tag:" + viewGroup.getId() + " View Type:" + viewGroup.getClass().toString() + "\n");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  children(");
            sb2.append(viewGroupManager.getChildCount(viewGroup));
            sb2.append("): [\n");
            sb.append(sb2.toString());
            for (int i3 = 0; viewGroupManager.getChildAt(viewGroup, i3) != null; i3 += 16) {
                int i4 = 0;
                while (true) {
                    int i5 = i3 + i4;
                    if (viewGroupManager.getChildAt(viewGroup, i5) == null || i4 >= 16) {
                        break;
                    }
                    sb.append(viewGroupManager.getChildAt(viewGroup, i5).getId() + ",");
                    i4++;
                }
                sb.append("\n");
            }
            sb.append(" ],\n");
        }
        if (iArr != null) {
            sb.append("  indicesToRemove(" + iArr.length + "): [\n");
            for (int i6 = 0; i6 < iArr.length; i6 += 16) {
                int i7 = 0;
                while (true) {
                    int i8 = i6 + i7;
                    if (i8 >= iArr.length || i7 >= 16) {
                        break;
                    }
                    sb.append(iArr[i8] + ",");
                    i7++;
                }
                sb.append("\n");
            }
            sb.append(" ],\n");
        }
        if (o0Arr != null) {
            sb.append("  viewsToAdd(" + o0Arr.length + "): [\n");
            for (int i9 = 0; i9 < o0Arr.length; i9 += 16) {
                int i10 = 0;
                while (true) {
                    int i11 = i9 + i10;
                    if (i11 >= o0Arr.length || i10 >= 16) {
                        break;
                    }
                    sb.append("[" + o0Arr[i11].f7353b + "," + o0Arr[i11].f7352a + "],");
                    i10++;
                }
                sb.append("\n");
            }
            sb.append(" ],\n");
        }
        if (iArr2 != null) {
            sb.append("  tagsToDelete(" + iArr2.length + "): [\n");
            for (int i12 = 0; i12 < iArr2.length; i12 += 16) {
                int i13 = 0;
                while (true) {
                    int i14 = i12 + i13;
                    if (i14 >= iArr2.length || i13 >= 16) {
                        break;
                    }
                    sb.append(iArr2[i14] + ",");
                    i13++;
                }
                sb.append("\n");
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }

    private Set o(int i3) {
        if (this.f7457k == null) {
            this.f7457k = new HashMap();
        }
        if (!this.f7457k.containsKey(Integer.valueOf(i3))) {
            this.f7457k.put(Integer.valueOf(i3), new HashSet());
        }
        return (Set) this.f7457k.get(Integer.valueOf(i3));
    }

    private void r(View view, RectF rectF) {
        Matrix matrix = view.getMatrix();
        if (!matrix.isIdentity()) {
            matrix.mapRect(rectF);
        }
        rectF.offset(view.getLeft(), view.getTop());
        Object parent = view.getParent();
        while (parent instanceof View) {
            View view2 = (View) parent;
            rectF.offset(-view2.getScrollX(), -view2.getScrollY());
            Matrix matrix2 = view2.getMatrix();
            if (!matrix2.isIdentity()) {
                matrix2.mapRect(rectF);
            }
            rectF.offset(view2.getLeft(), view2.getTop());
            parent = view2.getParent();
        }
    }

    public synchronized void A(int i3, int i4, int i5, int i6, int i7, int i8, com.facebook.yoga.h hVar) {
        try {
            if (this.f7447a) {
                Y.a.h(f7446l, "updateLayout[%d]->[%d]: %d %d %d %d", Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8));
            }
            UiThreadUtil.assertOnUiThread();
            C0519b.a(0L, "NativeViewHierarchyManager_updateLayout").a("parentTag", i3).a("tag", i4).c();
            try {
                View viewV = v(i4);
                viewV.setLayoutDirection(T.a(hVar));
                viewV.measure(View.MeasureSpec.makeMeasureSpec(i7, 1073741824), View.MeasureSpec.makeMeasureSpec(i8, 1073741824));
                ViewParent parent = viewV.getParent();
                if (parent instanceof InterfaceC0462w0) {
                    parent.requestLayout();
                }
                if (this.f7450d.get(i3)) {
                    B(viewV, i5, i6, i7, i8);
                } else {
                    NativeModule nativeModule = (ViewManager) this.f7449c.get(i3);
                    if (!(nativeModule instanceof O)) {
                        throw new P("Trying to use view with tag " + i3 + " as a parent, but its Manager doesn't implement IViewManagerWithChildren");
                    }
                    O o3 = (O) nativeModule;
                    if (o3 != null && !o3.needsCustomLayoutForChildren()) {
                        B(viewV, i5, i6, i7, i8);
                    }
                }
                C0518a.i(0L);
            } catch (Throwable th) {
                C0518a.i(0L);
                throw th;
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    public synchronized void C(int i3, C0454s0 c0454s0) {
        try {
            if (this.f7447a) {
                Y.a.d(f7446l, "updateProperties[%d]: %s", Integer.valueOf(i3), c0454s0.toString());
            }
            UiThreadUtil.assertOnUiThread();
            try {
                ViewManager viewManagerW = w(i3);
                View viewV = v(i3);
                if (c0454s0 != null) {
                    viewManagerW.updateProperties(viewV, c0454s0);
                }
            } catch (P e4) {
                Y.a.n(f7446l, "Unable to update properties for view tag " + i3, e4);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void D(int i3, Object obj) {
        try {
            if (this.f7447a) {
                Y.a.d(f7446l, "updateViewExtraData[%d]: %s", Integer.valueOf(i3), obj.toString());
            }
            UiThreadUtil.assertOnUiThread();
            w(i3).updateExtraData(v(i3), obj);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void b(int i3, View view) {
        c(i3, view);
    }

    protected final synchronized void c(int i3, View view) {
        try {
            if (this.f7447a) {
                Y.a.d(f7446l, "addRootViewGroup[%d]: %s", Integer.valueOf(i3), view != null ? view.toString() : "<null>");
            }
            if (view.getId() != -1) {
                Y.a.m(f7446l, "Trying to add a root view with an explicit id (" + view.getId() + ") already set. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID before calling addRootView.");
            }
            this.f7448b.put(i3, view);
            this.f7449c.put(i3, this.f7453g);
            this.f7450d.put(i3, true);
            view.setId(i3);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void e() {
        this.f7452f.b();
    }

    synchronized void f() {
        this.f7454h.f();
    }

    synchronized void h(ReadableMap readableMap, Callback callback) {
        this.f7454h.e(readableMap, callback);
    }

    public synchronized void j(B0 b02, int i3, String str, C0454s0 c0454s0) {
        try {
            if (this.f7447a) {
                Y.a.e(f7446l, "createView[%d]: %s %s", Integer.valueOf(i3), str, c0454s0 != null ? c0454s0.toString() : "<null>");
            }
            UiThreadUtil.assertOnUiThread();
            C0519b.a(0L, "NativeViewHierarchyManager_createView").a("tag", i3).b("className", str).c();
            try {
                ViewManager viewManagerC = this.f7451e.c(str);
                this.f7448b.put(i3, viewManagerC.createView(i3, b02, c0454s0, null, this.f7452f));
                this.f7449c.put(i3, viewManagerC);
            } finally {
                C0518a.i(0L);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void k(int i3, int i4, ReadableArray readableArray) {
        try {
            if (this.f7447a) {
                Y.a.e(f7446l, "dispatchCommand[%d]: %d %s", Integer.valueOf(i3), Integer.valueOf(i4), readableArray != null ? readableArray.toString() : "<null>");
            }
            UiThreadUtil.assertOnUiThread();
            View view = (View) this.f7448b.get(i3);
            if (view == null) {
                throw new RetryableMountingLayerException("Trying to send command to a non-existing view with tag [" + i3 + "] and command " + i4);
            }
            w(i3).receiveCommand(view, i4, readableArray);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void l(int i3, String str, ReadableArray readableArray) {
        try {
            if (this.f7447a) {
                Y.a.e(f7446l, "dispatchCommand[%d]: %s %s", Integer.valueOf(i3), str, readableArray != null ? readableArray.toString() : "<null>");
            }
            UiThreadUtil.assertOnUiThread();
            View view = (View) this.f7448b.get(i3);
            if (view == null) {
                throw new RetryableMountingLayerException("Trying to send command to a non-existing view with tag [" + i3 + "] and command " + str);
            }
            w(i3).receiveCommand(view, str, readableArray);
        } catch (Throwable th) {
            throw th;
        }
    }

    protected synchronized void m(View view) {
        try {
            if (this.f7447a) {
                Y.a.c(f7446l, "dropView[%d]", Integer.valueOf(view != null ? view.getId() : -1));
            }
            UiThreadUtil.assertOnUiThread();
            if (view == null) {
                return;
            }
            if (this.f7449c.get(view.getId()) == null) {
                return;
            }
            if (!this.f7450d.get(view.getId())) {
                w(view.getId()).onDropViewInstance(view);
            }
            ViewManager viewManager = (ViewManager) this.f7449c.get(view.getId());
            if ((view instanceof ViewGroup) && (viewManager instanceof ViewGroupManager)) {
                ViewGroup viewGroup = (ViewGroup) view;
                ViewGroupManager viewGroupManager = (ViewGroupManager) viewManager;
                for (int childCount = viewGroupManager.getChildCount(viewGroup) - 1; childCount >= 0; childCount--) {
                    View childAt = viewGroupManager.getChildAt(viewGroup, childCount);
                    if (childAt == null) {
                        Y.a.m(f7446l, "Unable to drop null child view");
                    } else if (this.f7448b.get(childAt.getId()) != null) {
                        m(childAt);
                    }
                }
                viewGroupManager.removeAllViews(viewGroup);
            }
            this.f7448b.remove(view.getId());
            this.f7449c.remove(view.getId());
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized int n(int i3, float f3, float f4) {
        View view;
        try {
            if (this.f7447a) {
                Y.a.e(f7446l, "findTargetTagForTouch[%d]: %f %f", Integer.valueOf(i3), Float.valueOf(f3), Float.valueOf(f4));
            }
            UiThreadUtil.assertOnUiThread();
            view = (View) this.f7448b.get(i3);
            if (view == null) {
                throw new JSApplicationIllegalArgumentException("Could not find view with tag " + i3);
            }
        } catch (Throwable th) {
            throw th;
        }
        return C0.d(f3, f4, (ViewGroup) view);
    }

    public synchronized int p() {
        return this.f7450d.size();
    }

    public synchronized void q(int i3, int[] iArr, O0[] o0Arr, int[] iArr2) {
        int i4;
        int[] iArr3 = iArr;
        synchronized (this) {
            try {
                if (this.f7447a) {
                    Y.a.f(f7446l, "createView[%d]: %s %s %s", Integer.valueOf(i3), iArr3 != null ? iArr.toString() : "<null>", o0Arr != null ? o0Arr.toString() : "<null>", iArr2 != null ? iArr2.toString() : "<null>");
                }
                UiThreadUtil.assertOnUiThread();
                Set setO = o(i3);
                ViewGroup viewGroup = (ViewGroup) this.f7448b.get(i3);
                ViewGroupManager viewGroupManager = (ViewGroupManager) w(i3);
                if (viewGroup == null) {
                    throw new P("Trying to manageChildren view with tag " + i3 + " which doesn't exist\n detail: " + i(viewGroup, viewGroupManager, iArr3, o0Arr, iArr2));
                }
                int childCount = viewGroupManager.getChildCount(viewGroup);
                if (iArr3 != null) {
                    int length = iArr3.length - 1;
                    while (length >= 0) {
                        int i5 = iArr3[length];
                        if (i5 < 0) {
                            throw new P("Trying to remove a negative view index:" + i5 + " view tag: " + i3 + "\n detail: " + i(viewGroup, viewGroupManager, iArr3, o0Arr, iArr2));
                        }
                        if (viewGroupManager.getChildAt(viewGroup, i5) == null) {
                            if (this.f7450d.get(i3) && viewGroupManager.getChildCount(viewGroup) == 0) {
                                return;
                            }
                            throw new P("Trying to remove a view index above child count " + i5 + " view tag: " + i3 + "\n detail: " + i(viewGroup, viewGroupManager, iArr3, o0Arr, iArr2));
                        }
                        if (i5 >= childCount) {
                            throw new P("Trying to remove an out of order view index:" + i5 + " view tag: " + i3 + "\n detail: " + i(viewGroup, viewGroupManager, iArr3, o0Arr, iArr2));
                        }
                        View childAt = viewGroupManager.getChildAt(viewGroup, i5);
                        if (!this.f7456j || !this.f7454h.h(childAt) || !d(iArr2, childAt.getId())) {
                            viewGroupManager.removeViewAt(viewGroup, i5);
                        }
                        length--;
                        childCount = i5;
                    }
                }
                if (iArr2 != null) {
                    int i6 = 0;
                    while (i6 < iArr2.length) {
                        int i7 = iArr2[i6];
                        View view = (View) this.f7448b.get(i7);
                        if (view == null) {
                            throw new P("Trying to destroy unknown view tag: " + i7 + "\n detail: " + i(viewGroup, viewGroupManager, iArr, o0Arr, iArr2));
                        }
                        if (this.f7456j && this.f7454h.h(view)) {
                            setO.add(Integer.valueOf(i7));
                            i4 = i6;
                            this.f7454h.c(view, new a(viewGroupManager, viewGroup, view, setO, i3));
                        } else {
                            i4 = i6;
                            m(view);
                        }
                        i6 = i4 + 1;
                        iArr3 = iArr;
                    }
                }
                int[] iArr4 = iArr3;
                if (o0Arr != null) {
                    for (O0 o02 : o0Arr) {
                        View view2 = (View) this.f7448b.get(o02.f7352a);
                        if (view2 == null) {
                            throw new P("Trying to add unknown view tag: " + o02.f7352a + "\n detail: " + i(viewGroup, viewGroupManager, iArr4, o0Arr, iArr2));
                        }
                        int i8 = o02.f7353b;
                        if (!setO.isEmpty()) {
                            i8 = 0;
                            int i9 = 0;
                            while (i8 < viewGroup.getChildCount() && i9 != o02.f7353b) {
                                if (!setO.contains(Integer.valueOf(viewGroup.getChildAt(i8).getId()))) {
                                    i9++;
                                }
                                i8++;
                            }
                        }
                        viewGroupManager.addView(viewGroup, view2, i8);
                    }
                }
                if (setO.isEmpty()) {
                    this.f7457k.remove(Integer.valueOf(i3));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void s(int i3, int[] iArr) {
        try {
            if (this.f7447a) {
                Y.a.c(f7446l, "measure[%d]", Integer.valueOf(i3));
            }
            UiThreadUtil.assertOnUiThread();
            View view = (View) this.f7448b.get(i3);
            if (view == null) {
                throw new C0425d0("No native view for " + i3 + " currently exists");
            }
            View view2 = (View) C0464x0.a(view);
            if (view2 == null) {
                throw new C0425d0("Native view " + i3 + " is no longer on screen");
            }
            g(view2, iArr);
            int i4 = iArr[0];
            int i5 = iArr[1];
            g(view, iArr);
            iArr[0] = iArr[0] - i4;
            iArr[1] = iArr[1] - i5;
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void t(int i3, int[] iArr) {
        try {
            if (this.f7447a) {
                Y.a.c(f7446l, "measureInWindow[%d]", Integer.valueOf(i3));
            }
            UiThreadUtil.assertOnUiThread();
            View view = (View) this.f7448b.get(i3);
            if (view == null) {
                throw new C0425d0("No native view for " + i3 + " currently exists");
            }
            view.getLocationOnScreen(iArr);
            Rect rect = new Rect();
            view.getWindowVisibleDisplayFrame(rect);
            iArr[0] = iArr[0] - rect.left;
            iArr[1] = iArr[1] - rect.top;
            iArr[2] = view.getWidth();
            iArr[3] = view.getHeight();
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void u(int i3) {
        try {
            if (this.f7447a) {
                Y.a.c(f7446l, "removeRootView[%d]", Integer.valueOf(i3));
            }
            UiThreadUtil.assertOnUiThread();
            if (!this.f7450d.get(i3)) {
                SoftAssertions.assertUnreachable("View with tag " + i3 + " is not registered as a root view");
            }
            View view = (View) this.f7448b.get(i3);
            m(view);
            this.f7450d.delete(i3);
            if (view != null) {
                view.setId(-1);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized View v(int i3) {
        View view;
        view = (View) this.f7448b.get(i3);
        if (view == null) {
            throw new P("Trying to resolve view with tag " + i3 + " which doesn't exist");
        }
        return view;
    }

    public final synchronized ViewManager w(int i3) {
        ViewManager viewManager;
        viewManager = (ViewManager) this.f7449c.get(i3);
        if (viewManager == null) {
            throw new P("ViewManager for tag " + i3 + " could not be found.\n");
        }
        return viewManager;
    }

    public synchronized void x(int i3, int i4) {
        View view = (View) this.f7448b.get(i3);
        if (view == null) {
            throw new RetryableMountingLayerException("Could not find view with tag " + i3);
        }
        view.sendAccessibilityEvent(i4);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized void y(int i3, int i4, boolean z3) {
        if (!z3) {
            this.f7452f.d(i4, null);
            return;
        }
        View view = (View) this.f7448b.get(i3);
        if (i4 != i3 && (view instanceof ViewParent)) {
            this.f7452f.d(i4, (ViewParent) view);
            return;
        }
        if (this.f7450d.get(i3)) {
            SoftAssertions.assertUnreachable("Cannot block native responder on " + i3 + " that is a root view");
        }
        this.f7452f.d(i4, view.getParent());
    }

    public void z(boolean z3) {
        this.f7456j = z3;
    }

    public C0421b0(U0 u02, RootViewManager rootViewManager) {
        C0542a c0542a = C0542a.f9422a;
        this.f7447a = false;
        this.f7452f = new K1.a();
        this.f7454h = new Q1.e();
        this.f7455i = new RectF();
        this.f7451e = u02;
        this.f7448b = new SparseArray();
        this.f7449c = new SparseArray();
        this.f7450d = new SparseBooleanArray();
        this.f7453g = rootViewManager;
    }
}
