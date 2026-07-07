package com.facebook.react.uimanager;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public class P0 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ViewGroup f7355a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f7356b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int[] f7357c;

    class a implements Comparator {
        a() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(View view, View view2) {
            Integer viewZIndex = ViewGroupManager.getViewZIndex(view);
            if (viewZIndex == null) {
                viewZIndex = num;
            }
            Integer viewZIndex2 = ViewGroupManager.getViewZIndex(view2);
            return viewZIndex.intValue() - (viewZIndex2 != null ? viewZIndex2 : 0).intValue();
        }
    }

    public P0(ViewGroup viewGroup) {
        this.f7355a = viewGroup;
    }

    public int a(int i3, int i4) {
        int[] iArr = this.f7357c;
        if (iArr != null && (i4 >= iArr.length || iArr[i4] >= i3)) {
            Y.a.K("ReactNative", "getChildDrawingOrder index out of bounds! Please check any custom view manipulations you may have done. childCount = %d, index = %d", Integer.valueOf(i3), Integer.valueOf(i4));
            e();
        }
        if (this.f7357c == null) {
            ArrayList arrayList = new ArrayList();
            for (int i5 = 0; i5 < i3; i5++) {
                arrayList.add(this.f7355a.getChildAt(i5));
            }
            Collections.sort(arrayList, new a());
            this.f7357c = new int[i3];
            for (int i6 = 0; i6 < i3; i6++) {
                this.f7357c[i6] = this.f7355a.indexOfChild((View) arrayList.get(i6));
            }
        }
        return this.f7357c[i4];
    }

    public void b(View view) {
        if (ViewGroupManager.getViewZIndex(view) != null) {
            this.f7356b++;
        }
        this.f7357c = null;
    }

    public void c(View view) {
        if (ViewGroupManager.getViewZIndex(view) != null) {
            this.f7356b--;
        }
        this.f7357c = null;
    }

    public boolean d() {
        return this.f7356b > 0;
    }

    public void e() {
        this.f7356b = 0;
        for (int i3 = 0; i3 < this.f7355a.getChildCount(); i3++) {
            if (ViewGroupManager.getViewZIndex(this.f7355a.getChildAt(i3)) != null) {
                this.f7356b++;
            }
        }
        this.f7357c = null;
    }
}
