package com.facebook.react.views.scroll;

import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.C0237a;
import com.facebook.react.bridge.AssertionException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.C0433h0;
import d1.AbstractC0505m;
import r.v;

/* JADX INFO: loaded from: classes.dex */
public final class h extends C0237a {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f7852d;

    public h() {
        String simpleName = h.class.getSimpleName();
        D2.h.e(simpleName, "getSimpleName(...)");
        this.f7852d = simpleName;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void n(View view, AccessibilityEvent accessibilityEvent) {
        Object tag = view.getTag(AbstractC0505m.f9229b);
        Integer numValueOf = null;
        ReadableMap readableMap = tag instanceof ReadableMap ? (ReadableMap) tag : null;
        if (readableMap == null) {
            return;
        }
        accessibilityEvent.setItemCount(readableMap.getInt("itemCount"));
        ViewGroup viewGroup = view instanceof ViewGroup ? (ViewGroup) view : null;
        View childAt = viewGroup != null ? viewGroup.getChildAt(0) : null;
        ViewGroup viewGroup2 = childAt instanceof ViewGroup ? (ViewGroup) childAt : null;
        if (viewGroup2 == null) {
            return;
        }
        int childCount = viewGroup2.getChildCount();
        Integer numValueOf2 = null;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt2 = viewGroup2.getChildAt(i3);
            if (!(view instanceof d)) {
                return;
            }
            D2.h.c(childAt2);
            boolean zC = ((d) view).c(childAt2);
            Object tag2 = childAt2.getTag(AbstractC0505m.f9230c);
            D2.h.d(tag2, "null cannot be cast to non-null type com.facebook.react.bridge.ReadableMap");
            ReadableMap readableMap2 = (ReadableMap) tag2;
            if (!(childAt2 instanceof ViewGroup)) {
                return;
            }
            ((ViewGroup) childAt2).getChildCount();
            if (zC) {
                if (numValueOf == null) {
                    numValueOf = Integer.valueOf(readableMap2.getInt("itemIndex"));
                }
                numValueOf2 = Integer.valueOf(readableMap2.getInt("itemIndex"));
            }
            if (numValueOf != null && numValueOf2 != null) {
                accessibilityEvent.setFromIndex(numValueOf.intValue());
                accessibilityEvent.setToIndex(numValueOf2.intValue());
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void o(View view, v vVar) {
        C0433h0.d dVarD = C0433h0.d.d(view);
        if (dVarD != null) {
            C0433h0.h0(vVar, dVarD, view.getContext());
        }
        Object tag = view.getTag(AbstractC0505m.f9229b);
        ReadableMap readableMap = tag instanceof ReadableMap ? (ReadableMap) tag : null;
        if (readableMap != null) {
            vVar.r0(v.e.a(readableMap.getInt("rowCount"), readableMap.getInt("columnCount"), readableMap.getBoolean("hierarchical")));
        }
        if (view instanceof d) {
            vVar.H0(((d) view).getScrollEnabled());
        }
    }

    @Override // androidx.core.view.C0237a
    public void f(View view, AccessibilityEvent accessibilityEvent) {
        D2.h.f(view, "host");
        D2.h.f(accessibilityEvent, "event");
        super.f(view, accessibilityEvent);
        if (view instanceof d) {
            n(view, accessibilityEvent);
            return;
        }
        ReactSoftExceptionLogger.logSoftException(this.f7852d, new AssertionException("ReactScrollViewAccessibilityDelegate should only be used with ReactAccessibleScrollView, not with class: " + view.getClass().getSimpleName()));
    }

    @Override // androidx.core.view.C0237a
    public void g(View view, v vVar) {
        D2.h.f(view, "host");
        D2.h.f(vVar, "info");
        super.g(view, vVar);
        if (view instanceof d) {
            o(view, vVar);
            return;
        }
        ReactSoftExceptionLogger.logSoftException(this.f7852d, new AssertionException("ReactScrollViewAccessibilityDelegate should only be used with ReactAccessibleScrollView, not with class: " + view.getClass().getSimpleName()));
    }
}
