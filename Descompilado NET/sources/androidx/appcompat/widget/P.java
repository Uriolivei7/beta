package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.core.view.C0254i0;
import d.AbstractC0487a;
import f.C0529a;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class P extends ListView {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Rect f3811b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f3812c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f3813d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private int f3814e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private int f3815f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private int f3816g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private d f3817h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f3818i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f3819j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private boolean f3820k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private C0254i0 f3821l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private androidx.core.widget.f f3822m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    f f3823n;

    static class a {
        static void a(View view, float f3, float f4) {
            view.drawableHotspotChanged(f3, f4);
        }
    }

    static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static Method f3824a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private static Method f3825b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private static Method f3826c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private static boolean f3827d;

        static {
            try {
                Class cls = Integer.TYPE;
                Class cls2 = Boolean.TYPE;
                Class cls3 = Float.TYPE;
                Method declaredMethod = AbsListView.class.getDeclaredMethod("positionSelector", cls, View.class, cls2, cls3, cls3);
                f3824a = declaredMethod;
                declaredMethod.setAccessible(true);
                Method declaredMethod2 = AdapterView.class.getDeclaredMethod("setSelectedPositionInt", cls);
                f3825b = declaredMethod2;
                declaredMethod2.setAccessible(true);
                Method declaredMethod3 = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt", cls);
                f3826c = declaredMethod3;
                declaredMethod3.setAccessible(true);
                f3827d = true;
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
            }
        }

        static boolean a() {
            return f3827d;
        }

        static void b(P p3, int i3, View view) {
            try {
                f3824a.invoke(p3, Integer.valueOf(i3), view, Boolean.FALSE, -1, -1);
                f3825b.invoke(p3, Integer.valueOf(i3));
                f3826c.invoke(p3, Integer.valueOf(i3));
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        }
    }

    static class c {
        static boolean a(AbsListView absListView) {
            return absListView.isSelectedChildViewEnabled();
        }

        static void b(AbsListView absListView, boolean z3) {
            absListView.setSelectedChildViewEnabled(z3);
        }
    }

    private static class d extends C0529a {

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private boolean f3828c;

        d(Drawable drawable) {
            super(drawable);
            this.f3828c = true;
        }

        void b(boolean z3) {
            this.f3828c = z3;
        }

        @Override // f.C0529a, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.f3828c) {
                super.draw(canvas);
            }
        }

        @Override // f.C0529a, android.graphics.drawable.Drawable
        public void setHotspot(float f3, float f4) {
            if (this.f3828c) {
                super.setHotspot(f3, f4);
            }
        }

        @Override // f.C0529a, android.graphics.drawable.Drawable
        public void setHotspotBounds(int i3, int i4, int i5, int i6) {
            if (this.f3828c) {
                super.setHotspotBounds(i3, i4, i5, i6);
            }
        }

        @Override // f.C0529a, android.graphics.drawable.Drawable
        public boolean setState(int[] iArr) {
            if (this.f3828c) {
                return super.setState(iArr);
            }
            return false;
        }

        @Override // f.C0529a, android.graphics.drawable.Drawable
        public boolean setVisible(boolean z3, boolean z4) {
            if (this.f3828c) {
                return super.setVisible(z3, z4);
            }
            return false;
        }
    }

    static class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final Field f3829a;

        static {
            Field declaredField = null;
            try {
                declaredField = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e4) {
                e4.printStackTrace();
            }
            f3829a = declaredField;
        }

        static boolean a(AbsListView absListView) {
            Field field = f3829a;
            if (field == null) {
                return false;
            }
            try {
                return field.getBoolean(absListView);
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
                return false;
            }
        }

        static void b(AbsListView absListView, boolean z3) {
            Field field = f3829a;
            if (field != null) {
                try {
                    field.set(absListView, Boolean.valueOf(z3));
                } catch (IllegalAccessException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    private class f implements Runnable {
        f() {
        }

        public void a() {
            P p3 = P.this;
            p3.f3823n = null;
            p3.removeCallbacks(this);
        }

        public void b() {
            P.this.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            P p3 = P.this;
            p3.f3823n = null;
            p3.drawableStateChanged();
        }
    }

    P(Context context, boolean z3) {
        super(context, null, AbstractC0487a.f8657B);
        this.f3811b = new Rect();
        this.f3812c = 0;
        this.f3813d = 0;
        this.f3814e = 0;
        this.f3815f = 0;
        this.f3819j = z3;
        setCacheColorHint(0);
    }

    private void a() {
        this.f3820k = false;
        setPressed(false);
        drawableStateChanged();
        View childAt = getChildAt(this.f3816g - getFirstVisiblePosition());
        if (childAt != null) {
            childAt.setPressed(false);
        }
        C0254i0 c0254i0 = this.f3821l;
        if (c0254i0 != null) {
            c0254i0.c();
            this.f3821l = null;
        }
    }

    private void b(View view, int i3) {
        performItemClick(view, i3, getItemIdAtPosition(i3));
    }

    private void c(Canvas canvas) {
        Drawable selector;
        if (this.f3811b.isEmpty() || (selector = getSelector()) == null) {
            return;
        }
        selector.setBounds(this.f3811b);
        selector.draw(canvas);
    }

    private void f(int i3, View view) {
        Rect rect = this.f3811b;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        rect.left -= this.f3812c;
        rect.top -= this.f3813d;
        rect.right += this.f3814e;
        rect.bottom += this.f3815f;
        boolean zK = k();
        if (view.isEnabled() != zK) {
            l(!zK);
            if (i3 != -1) {
                refreshDrawableState();
            }
        }
    }

    private void g(int i3, View view) {
        Drawable selector = getSelector();
        boolean z3 = (selector == null || i3 == -1) ? false : true;
        if (z3) {
            selector.setVisible(false, false);
        }
        f(i3, view);
        if (z3) {
            Rect rect = this.f3811b;
            float fExactCenterX = rect.exactCenterX();
            float fExactCenterY = rect.exactCenterY();
            selector.setVisible(getVisibility() == 0, false);
            androidx.core.graphics.drawable.a.c(selector, fExactCenterX, fExactCenterY);
        }
    }

    private void h(int i3, View view, float f3, float f4) {
        g(i3, view);
        Drawable selector = getSelector();
        if (selector == null || i3 == -1) {
            return;
        }
        androidx.core.graphics.drawable.a.c(selector, f3, f4);
    }

    private void i(View view, int i3, float f3, float f4) {
        View childAt;
        this.f3820k = true;
        a.a(this, f3, f4);
        if (!isPressed()) {
            setPressed(true);
        }
        layoutChildren();
        int i4 = this.f3816g;
        if (i4 != -1 && (childAt = getChildAt(i4 - getFirstVisiblePosition())) != null && childAt != view && childAt.isPressed()) {
            childAt.setPressed(false);
        }
        this.f3816g = i3;
        a.a(view, f3 - view.getLeft(), f4 - view.getTop());
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        h(i3, view, f3, f4);
        j(false);
        refreshDrawableState();
    }

    private void j(boolean z3) {
        d dVar = this.f3817h;
        if (dVar != null) {
            dVar.b(z3);
        }
    }

    private boolean k() {
        return Build.VERSION.SDK_INT >= 33 ? c.a(this) : e.a(this);
    }

    private void l(boolean z3) {
        if (Build.VERSION.SDK_INT >= 33) {
            c.b(this, z3);
        } else {
            e.b(this, z3);
        }
    }

    private boolean m() {
        return this.f3820k;
    }

    private void n() {
        Drawable selector = getSelector();
        if (selector != null && m() && isPressed()) {
            selector.setState(getDrawableState());
        }
    }

    public int d(int i3, int i4, int i5, int i6, int i7) {
        int listPaddingTop = getListPaddingTop();
        int listPaddingBottom = getListPaddingBottom();
        int dividerHeight = getDividerHeight();
        Drawable divider = getDivider();
        ListAdapter adapter = getAdapter();
        if (adapter == null) {
            return listPaddingTop + listPaddingBottom;
        }
        int measuredHeight = listPaddingTop + listPaddingBottom;
        if (dividerHeight <= 0 || divider == null) {
            dividerHeight = 0;
        }
        int count = adapter.getCount();
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        View view = null;
        while (i8 < count) {
            int itemViewType = adapter.getItemViewType(i8);
            if (itemViewType != i9) {
                view = null;
                i9 = itemViewType;
            }
            view = adapter.getView(i8, view, this);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
                view.setLayoutParams(layoutParams);
            }
            int i11 = layoutParams.height;
            view.measure(i3, i11 > 0 ? View.MeasureSpec.makeMeasureSpec(i11, 1073741824) : View.MeasureSpec.makeMeasureSpec(0, 0));
            view.forceLayout();
            if (i8 > 0) {
                measuredHeight += dividerHeight;
            }
            measuredHeight += view.getMeasuredHeight();
            if (measuredHeight >= i6) {
                return (i7 < 0 || i8 <= i7 || i10 <= 0 || measuredHeight == i6) ? i6 : i10;
            }
            if (i7 >= 0 && i8 >= i7) {
                i10 = measuredHeight;
            }
            i8++;
        }
        return measuredHeight;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        c(canvas);
        super.dispatchDraw(canvas);
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        if (this.f3823n != null) {
            return;
        }
        super.drawableStateChanged();
        j(true);
        n();
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean e(android.view.MotionEvent r8, int r9) {
        /*
            r7 = this;
            int r0 = r8.getActionMasked()
            r1 = 1
            r2 = 0
            if (r0 == r1) goto L16
            r3 = 2
            if (r0 == r3) goto L14
            r9 = 3
            if (r0 == r9) goto L11
        Le:
            r3 = r1
            r9 = r2
            goto L46
        L11:
            r9 = r2
            r3 = r9
            goto L46
        L14:
            r3 = r1
            goto L17
        L16:
            r3 = r2
        L17:
            int r9 = r8.findPointerIndex(r9)
            if (r9 >= 0) goto L1e
            goto L11
        L1e:
            float r4 = r8.getX(r9)
            int r4 = (int) r4
            float r9 = r8.getY(r9)
            int r9 = (int) r9
            int r5 = r7.pointToPosition(r4, r9)
            r6 = -1
            if (r5 != r6) goto L31
            r9 = r1
            goto L46
        L31:
            int r3 = r7.getFirstVisiblePosition()
            int r3 = r5 - r3
            android.view.View r3 = r7.getChildAt(r3)
            float r4 = (float) r4
            float r9 = (float) r9
            r7.i(r3, r5, r4, r9)
            if (r0 != r1) goto Le
            r7.b(r3, r5)
            goto Le
        L46:
            if (r3 == 0) goto L4a
            if (r9 == 0) goto L4d
        L4a:
            r7.a()
        L4d:
            if (r3 == 0) goto L65
            androidx.core.widget.f r9 = r7.f3822m
            if (r9 != 0) goto L5a
            androidx.core.widget.f r9 = new androidx.core.widget.f
            r9.<init>(r7)
            r7.f3822m = r9
        L5a:
            androidx.core.widget.f r9 = r7.f3822m
            r9.m(r1)
            androidx.core.widget.f r9 = r7.f3822m
            r9.onTouch(r7, r8)
            goto L6c
        L65:
            androidx.core.widget.f r8 = r7.f3822m
            if (r8 == 0) goto L6c
            r8.m(r2)
        L6c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.P.e(android.view.MotionEvent, int):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean hasFocus() {
        return this.f3819j || super.hasFocus();
    }

    @Override // android.view.View
    public boolean hasWindowFocus() {
        return this.f3819j || super.hasWindowFocus();
    }

    @Override // android.view.View
    public boolean isFocused() {
        return this.f3819j || super.isFocused();
    }

    @Override // android.view.View
    public boolean isInTouchMode() {
        return (this.f3819j && this.f3818i) || super.isInTouchMode();
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.f3823n = null;
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 26) {
            return super.onHoverEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 10 && this.f3823n == null) {
            f fVar = new f();
            this.f3823n = fVar;
            fVar.b();
        }
        boolean zOnHoverEvent = super.onHoverEvent(motionEvent);
        if (actionMasked == 9 || actionMasked == 7) {
            int iPointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
            if (iPointToPosition != -1 && iPointToPosition != getSelectedItemPosition()) {
                View childAt = getChildAt(iPointToPosition - getFirstVisiblePosition());
                if (childAt.isEnabled()) {
                    requestFocus();
                    if (i3 < 30 || !b.a()) {
                        setSelectionFromTop(iPointToPosition, childAt.getTop() - getTop());
                    } else {
                        b.b(this, iPointToPosition, childAt);
                    }
                }
                n();
            }
        } else {
            setSelection(-1);
        }
        return zOnHoverEvent;
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.f3816g = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
        }
        f fVar = this.f3823n;
        if (fVar != null) {
            fVar.a();
        }
        return super.onTouchEvent(motionEvent);
    }

    void setListSelectionHidden(boolean z3) {
        this.f3818i = z3;
    }

    @Override // android.widget.AbsListView
    public void setSelector(Drawable drawable) {
        d dVar = drawable != null ? new d(drawable) : null;
        this.f3817h = dVar;
        super.setSelector(dVar);
        Rect rect = new Rect();
        if (drawable != null) {
            drawable.getPadding(rect);
        }
        this.f3812c = rect.left;
        this.f3813d = rect.top;
        this.f3814e = rect.right;
        this.f3815f = rect.bottom;
    }
}
