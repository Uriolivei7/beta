package com.facebook.react.views.scroll;

import R1.n;
import android.view.View;
import androidx.core.view.Z;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0418a;
import com.facebook.react.uimanager.C0429f0;
import com.facebook.react.uimanager.C0454s0;
import com.facebook.react.uimanager.EnumC0431g0;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.W;
import com.facebook.react.uimanager.X;
import com.facebook.react.views.scroll.b;
import com.facebook.react.views.scroll.i;
import java.util.ArrayList;
import org.chromium.support_lib_boundary.WebSettingsBoundaryInterface;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactHorizontalScrollViewManager.REACT_CLASS)
public class ReactHorizontalScrollViewManager extends ViewGroupManager<f> implements i.b {
    public static final String REACT_CLASS = "AndroidHorizontalScrollView";
    private static final int[] SPACING_TYPES = {8, 0, 2, 1, 3};
    private a mFpsListener;

    public ReactHorizontalScrollViewManager() {
        this(null);
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.N
    public /* bridge */ /* synthetic */ void removeAllViews(View view) {
        super.removeAllViews(view);
    }

    @L1.b(customType = "Color", names = {"borderColor", "borderLeftColor", "borderRightColor", "borderTopColor", "borderBottomColor"})
    public void setBorderColor(f fVar, int i3, Integer num) {
        C0418a.p(fVar, n.f2075c, num);
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderRadius", "borderTopLeftRadius", "borderTopRightRadius", "borderBottomRightRadius", "borderBottomLeftRadius"})
    public void setBorderRadius(f fVar, int i3, float f3) {
        C0418a.q(fVar, R1.d.values()[i3], Float.isNaN(f3) ? null : new W(f3, X.f7408b));
    }

    @L1.a(name = "borderStyle")
    public void setBorderStyle(f fVar, String str) {
        C0418a.r(fVar, str == null ? null : R1.f.b(str));
    }

    @L1.b(defaultFloat = Float.NaN, names = {"borderWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth"})
    public void setBorderWidth(f fVar, int i3, float f3) {
        C0418a.s(fVar, n.values()[i3], Float.valueOf(f3));
    }

    @L1.a(customType = "Color", defaultInt = WebSettingsBoundaryInterface.ForceDarkBehavior.FORCE_DARK_ONLY, name = "endFillColor")
    public void setBottomFillColor(f fVar, int i3) {
        fVar.setEndFillColor(i3);
    }

    @L1.a(name = "contentOffset")
    public void setContentOffset(f fVar, ReadableMap readableMap) {
        if (readableMap != null) {
            fVar.scrollTo((int) C0429f0.g(readableMap.hasKey("x") ? readableMap.getDouble("x") : 0.0d), (int) C0429f0.g(readableMap.hasKey("y") ? readableMap.getDouble("y") : 0.0d));
        } else {
            fVar.scrollTo(0, 0);
        }
    }

    @L1.a(name = "decelerationRate")
    public void setDecelerationRate(f fVar, float f3) {
        fVar.setDecelerationRate(f3);
    }

    @L1.a(name = "disableIntervalMomentum")
    public void setDisableIntervalMomentum(f fVar, boolean z3) {
        fVar.setDisableIntervalMomentum(z3);
    }

    @L1.a(name = "fadingEdgeLength")
    public void setFadingEdgeLength(f fVar, int i3) {
        if (i3 > 0) {
            fVar.setHorizontalFadingEdgeEnabled(true);
            fVar.setFadingEdgeLength(i3);
        } else {
            fVar.setHorizontalFadingEdgeEnabled(false);
            fVar.setFadingEdgeLength(0);
        }
    }

    @L1.a(name = "horizontal")
    public void setHorizontal(f fVar, boolean z3) {
    }

    @L1.a(name = "maintainVisibleContentPosition")
    public void setMaintainVisibleContentPosition(f fVar, ReadableMap readableMap) {
        if (readableMap != null) {
            fVar.setMaintainVisibleContentPosition(b.C0115b.a(readableMap));
        } else {
            fVar.setMaintainVisibleContentPosition(null);
        }
    }

    @L1.a(name = "nestedScrollEnabled")
    public void setNestedScrollEnabled(f fVar, boolean z3) {
        Z.h0(fVar, z3);
    }

    @L1.a(name = "overScrollMode")
    public void setOverScrollMode(f fVar, String str) {
        fVar.setOverScrollMode(j.n(str));
    }

    @L1.a(name = "overflow")
    public void setOverflow(f fVar, String str) {
        fVar.setOverflow(str);
    }

    @L1.a(name = "pagingEnabled")
    public void setPagingEnabled(f fVar, boolean z3) {
        fVar.setPagingEnabled(z3);
    }

    @L1.a(name = "persistentScrollbar")
    public void setPersistentScrollbar(f fVar, boolean z3) {
        fVar.setScrollbarFadingEnabled(!z3);
    }

    @L1.a(name = "pointerEvents")
    public void setPointerEvents(f fVar, String str) {
        fVar.setPointerEvents(EnumC0431g0.d(str));
    }

    @L1.a(name = "removeClippedSubviews")
    public void setRemoveClippedSubviews(f fVar, boolean z3) {
        fVar.setRemoveClippedSubviews(z3);
    }

    @L1.a(defaultBoolean = true, name = "scrollEnabled")
    public void setScrollEnabled(f fVar, boolean z3) {
        fVar.setScrollEnabled(z3);
    }

    @L1.a(name = "scrollEventThrottle")
    public void setScrollEventThrottle(f fVar, int i3) {
        fVar.setScrollEventThrottle(i3);
    }

    @L1.a(name = "scrollPerfTag")
    public void setScrollPerfTag(f fVar, String str) {
        fVar.setScrollPerfTag(str);
    }

    @L1.a(name = "sendMomentumEvents")
    public void setSendMomentumEvents(f fVar, boolean z3) {
        fVar.setSendMomentumEvents(z3);
    }

    @L1.a(defaultBoolean = true, name = "showsHorizontalScrollIndicator")
    public void setShowsHorizontalScrollIndicator(f fVar, boolean z3) {
        fVar.setHorizontalScrollBarEnabled(z3);
    }

    @L1.a(name = "snapToAlignment")
    public void setSnapToAlignment(f fVar, String str) {
        fVar.setSnapToAlignment(j.o(str));
    }

    @L1.a(name = "snapToEnd")
    public void setSnapToEnd(f fVar, boolean z3) {
        fVar.setSnapToEnd(z3);
    }

    @L1.a(name = "snapToInterval")
    public void setSnapToInterval(f fVar, float f3) {
        fVar.setSnapInterval((int) (f3 * C0429f0.c()));
    }

    @L1.a(name = "snapToOffsets")
    public void setSnapToOffsets(f fVar, ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() == 0) {
            fVar.setSnapOffsets(null);
            return;
        }
        float fC = C0429f0.c();
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < readableArray.size(); i3++) {
            arrayList.add(Integer.valueOf((int) (readableArray.getDouble(i3) * ((double) fC))));
        }
        fVar.setSnapOffsets(arrayList);
    }

    @L1.a(name = "snapToStart")
    public void setSnapToStart(f fVar, boolean z3) {
        fVar.setSnapToStart(z3);
    }

    public ReactHorizontalScrollViewManager(a aVar) {
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public f createViewInstance(B0 b02) {
        return new f(b02, null);
    }

    @Override // com.facebook.react.views.scroll.i.b
    public void flashScrollIndicators(f fVar) {
        fVar.v();
    }

    @Override // com.facebook.react.views.scroll.i.b
    public void scrollTo(f fVar, i.c cVar) {
        fVar.o();
        if (cVar.f7856c) {
            fVar.f(cVar.f7854a, cVar.f7855b);
        } else {
            fVar.scrollTo(cVar.f7854a, cVar.f7855b);
        }
    }

    @Override // com.facebook.react.views.scroll.i.b
    public void scrollToEnd(f fVar, i.d dVar) {
        View childAt = fVar.getChildAt(0);
        if (childAt == null) {
            throw new RetryableMountingLayerException("scrollToEnd called on HorizontalScrollView without child");
        }
        int width = childAt.getWidth() + fVar.getPaddingRight();
        fVar.o();
        if (dVar.f7857a) {
            fVar.f(width, fVar.getScrollY());
        } else {
            fVar.scrollTo(width, fVar.getScrollY());
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(f fVar, C0454s0 c0454s0, A0 a02) {
        fVar.setStateWrapper(a02);
        return null;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(f fVar, int i3, ReadableArray readableArray) {
        i.b(this, fVar, i3, readableArray);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(f fVar, String str, ReadableArray readableArray) {
        i.c(this, fVar, str, readableArray);
    }
}
