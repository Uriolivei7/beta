package com.facebook.react.uimanager;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.bridge.UiThreadUtil;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class C0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final float[] f7231a = new float[2];

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final PointF f7232b = new PointF();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final float[] f7233c = new float[2];

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final Matrix f7234d = new Matrix();

    private enum a {
        SELF,
        CHILD
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f7238a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final View f7239b;

        public View a() {
            return this.f7239b;
        }

        public int b() {
            return this.f7238a;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return (obj instanceof b) && ((b) obj).b() == this.f7238a;
        }

        public int hashCode() {
            return Integer.valueOf(this.f7238a).hashCode();
        }

        private b(int i3, View view) {
            this.f7238a = i3;
            this.f7239b = view;
        }
    }

    private static View a(View view) {
        while (view != null && view.getId() <= 0) {
            view = (View) view.getParent();
        }
        return view;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static List b(float f3, float f4, ViewGroup viewGroup, float[] fArr) {
        UiThreadUtil.assertOnUiThread();
        fArr[0] = f3;
        fArr[1] = f4;
        List arrayList = new ArrayList();
        View viewF = f(fArr, viewGroup, arrayList);
        if (viewF != null) {
            int i3 = 0;
            while (viewF != null && viewF.getId() <= 0) {
                viewF = (View) viewF.getParent();
                i3++;
            }
            if (i3 > 0 && i3 <= arrayList.size()) {
                arrayList = arrayList.subList(i3, arrayList.size());
            }
            int iH = h(viewF, fArr[0], fArr[1]);
            if (iH != viewF.getId()) {
                arrayList.add(0, new b(iH, null));
            }
        }
        return arrayList;
    }

    public static int c(float f3, float f4, ViewGroup viewGroup, float[] fArr, int[] iArr) {
        View viewA;
        UiThreadUtil.assertOnUiThread();
        int id = viewGroup.getId();
        fArr[0] = f3;
        fArr[1] = f4;
        View viewF = f(fArr, viewGroup, null);
        if (viewF == null || (viewA = a(viewF)) == null) {
            return id;
        }
        if (iArr != null) {
            iArr[0] = viewA.getId();
        }
        return h(viewA, fArr[0], fArr[1]);
    }

    public static int d(float f3, float f4, ViewGroup viewGroup) {
        return c(f3, f4, viewGroup, f7231a, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static View e(float[] fArr, View view, EnumSet enumSet, List list) {
        if (enumSet.contains(a.CHILD) && (view instanceof ViewGroup)) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (!i(fArr[0], fArr[1], view)) {
                if (view instanceof InterfaceC0443m0) {
                    if (M1.a.a(view.getId()) == 2 && !j(fArr[0], fArr[1], view)) {
                        return null;
                    }
                    String overflow = ((InterfaceC0443m0) view).getOverflow();
                    if ("hidden".equals(overflow) || "scroll".equals(overflow)) {
                        return null;
                    }
                }
                if (viewGroup.getClipChildren()) {
                    return null;
                }
            }
            int childCount = viewGroup.getChildCount();
            InterfaceC0460v0 interfaceC0460v0 = viewGroup instanceof InterfaceC0460v0 ? (InterfaceC0460v0) viewGroup : null;
            for (int i3 = childCount - 1; i3 >= 0; i3--) {
                View childAt = viewGroup.getChildAt(interfaceC0460v0 != null ? interfaceC0460v0.a(i3) : i3);
                PointF pointF = f7232b;
                g(fArr[0], fArr[1], viewGroup, childAt, pointF);
                float f3 = fArr[0];
                float f4 = fArr[1];
                fArr[0] = pointF.x;
                fArr[1] = pointF.y;
                View viewF = f(fArr, childAt, list);
                if (viewF != null) {
                    return viewF;
                }
                fArr[0] = f3;
                fArr[1] = f4;
            }
        }
        if (enumSet.contains(a.SELF) && i(fArr[0], fArr[1], view)) {
            return view;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static View f(float[] fArr, View view, List list) {
        EnumC0431g0 pointerEvents = view instanceof InterfaceC0445n0 ? ((InterfaceC0445n0) view).getPointerEvents() : EnumC0431g0.f7482f;
        if (!view.isEnabled()) {
            if (pointerEvents == EnumC0431g0.f7482f) {
                pointerEvents = EnumC0431g0.f7480d;
            } else if (pointerEvents == EnumC0431g0.f7481e) {
                pointerEvents = EnumC0431g0.f7479c;
            }
        }
        if (pointerEvents == EnumC0431g0.f7479c) {
            return null;
        }
        if (pointerEvents == EnumC0431g0.f7481e) {
            View viewE = e(fArr, view, EnumSet.of(a.SELF), list);
            if (viewE != null && list != null) {
                list.add(new b(view.getId(), view));
            }
            return viewE;
        }
        if (pointerEvents != EnumC0431g0.f7480d) {
            if (pointerEvents != EnumC0431g0.f7482f) {
                Y.a.I("ReactNative", "Unknown pointer event type: " + pointerEvents.toString());
            }
            View viewE2 = e(fArr, view, EnumSet.of(a.SELF, a.CHILD), list);
            if (viewE2 != null && list != null) {
                list.add(new b(view.getId(), view));
            }
            return viewE2;
        }
        View viewE3 = e(fArr, view, EnumSet.of(a.CHILD), list);
        if (viewE3 != null) {
            if (list != null) {
                list.add(new b(view.getId(), view));
            }
            return viewE3;
        }
        if (!(view instanceof InterfaceC0439k0) || !i(fArr[0], fArr[1], view) || ((InterfaceC0439k0) view).c(fArr[0], fArr[1]) == view.getId()) {
            return null;
        }
        if (list != null) {
            list.add(new b(view.getId(), view));
        }
        return view;
    }

    private static void g(float f3, float f4, ViewGroup viewGroup, View view, PointF pointF) {
        float scrollX = (f3 + viewGroup.getScrollX()) - view.getLeft();
        float scrollY = (f4 + viewGroup.getScrollY()) - view.getTop();
        Matrix matrix = view.getMatrix();
        if (!matrix.isIdentity()) {
            float[] fArr = f7233c;
            fArr[0] = scrollX;
            fArr[1] = scrollY;
            Matrix matrix2 = f7234d;
            matrix.invert(matrix2);
            matrix2.mapPoints(fArr);
            float f5 = fArr[0];
            scrollY = fArr[1];
            scrollX = f5;
        }
        pointF.set(scrollX, scrollY);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int h(View view, float f3, float f4) {
        return view instanceof InterfaceC0439k0 ? ((InterfaceC0439k0) view).c(f3, f4) : view.getId();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean i(float f3, float f4, View view) {
        if (view instanceof K1.c) {
            K1.c cVar = (K1.c) view;
            if (cVar.getHitSlopRect() != null) {
                Rect hitSlopRect = cVar.getHitSlopRect();
                return f3 >= ((float) (-hitSlopRect.left)) && f3 < ((float) (view.getWidth() + hitSlopRect.right)) && f4 >= ((float) (-hitSlopRect.top)) && f4 < ((float) (view.getHeight() + hitSlopRect.bottom));
            }
        }
        return f3 >= 0.0f && f3 < ((float) view.getWidth()) && f4 >= 0.0f && f4 < ((float) view.getHeight());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean j(float f3, float f4, View view) {
        if (!(view instanceof InterfaceC0443m0)) {
            return false;
        }
        Rect overflowInset = ((InterfaceC0443m0) view).getOverflowInset();
        return f3 >= ((float) overflowInset.left) && f3 < ((float) (view.getWidth() - overflowInset.right)) && f4 >= ((float) overflowInset.top) && f4 < ((float) (view.getHeight() - overflowInset.bottom));
    }
}
