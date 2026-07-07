package com.facebook.react.views.text;

import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import com.facebook.react.common.mapbuffer.ReadableMapBuffer;
import com.facebook.react.uimanager.A0;
import com.facebook.react.uimanager.B0;
import com.facebook.react.uimanager.C0454s0;
import com.facebook.react.uimanager.O;
import com.facebook.react.views.text.n;
import d1.AbstractC0505m;
import e1.C0527d;
import java.util.HashMap;
import java.util.Map;
import q1.C0652c;
import r1.C0670b;
import v1.InterfaceC0756a;

/* JADX INFO: loaded from: classes.dex */
@InterfaceC0756a(name = ReactTextViewManager.REACT_CLASS)
public class ReactTextViewManager extends ReactTextAnchorViewManager<m, h> implements O {
    public static final String REACT_CLASS = "RCTText";
    private static final String TAG = "ReactTextViewManager";
    private static final short TX_STATE_KEY_ATTRIBUTED_STRING = 0;
    private static final short TX_STATE_KEY_HASH = 2;
    private static final short TX_STATE_KEY_MOST_RECENT_EVENT_COUNT = 3;
    private static final short TX_STATE_KEY_PARAGRAPH_ATTRIBUTES = 1;
    protected o mReactTextViewManagerCallback;

    public ReactTextViewManager() {
        this(null);
    }

    private Object getReactTextUpdate(m mVar, C0454s0 c0454s0, com.facebook.react.common.mapbuffer.a aVar) {
        com.facebook.react.common.mapbuffer.a aVarD = aVar.d(0);
        com.facebook.react.common.mapbuffer.a aVarD2 = aVar.d(1);
        Spannable spannableG = t.g(mVar.getContext(), aVarD, null);
        mVar.setSpanned(spannableG);
        try {
            mVar.setMinimumFontSize((float) aVarD2.getDouble(6));
            return new i(spannableG, -1, false, t.j(aVarD, spannableG, mVar.getGravityHorizontal()), r.m(aVarD2.getString(2)), r.h(c0454s0, Build.VERSION.SDK_INT >= 26 ? mVar.getJustificationMode() : 0));
        } catch (IllegalArgumentException e4) {
            Y.a.o(TAG, "Paragraph Attributes: %s", aVarD2 != null ? aVarD2.toString() : "<empty>");
            throw e4;
        }
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Object> exportedCustomDirectEventTypeConstants = super.getExportedCustomDirectEventTypeConstants();
        if (exportedCustomDirectEventTypeConstants == null) {
            exportedCustomDirectEventTypeConstants = new HashMap<>();
        }
        exportedCustomDirectEventTypeConstants.putAll(C0527d.e("topTextLayout", C0527d.d("registrationName", "onTextLayout"), "topInlineViewLayout", C0527d.d("registrationName", "onInlineViewLayout")));
        return exportedCustomDirectEventTypeConstants;
    }

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Class<h> getShadowNodeClass() {
        return h.class;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public long measure(Context context, com.facebook.react.common.mapbuffer.a aVar, com.facebook.react.common.mapbuffer.a aVar2, com.facebook.react.common.mapbuffer.a aVar3, float f3, com.facebook.yoga.p pVar, float f4, com.facebook.yoga.p pVar2, float[] fArr) {
        return t.n(context, aVar, aVar2, f3, pVar, f4, pVar2, null, fArr);
    }

    @Override // com.facebook.react.uimanager.O
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @L1.a(name = "overflow")
    public void setOverflow(m mVar, String str) {
        mVar.setOverflow(str);
    }

    public ReactTextViewManager(o oVar) {
        if (C0670b.k()) {
            setupViewRecycling();
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public h createShadowNodeInstance() {
        return new h(null);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public m createViewInstance(B0 b02) {
        return new m(b02);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(m mVar) {
        super.onAfterUpdateTransaction(mVar);
        mVar.y();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public m prepareToRecycleView(B0 b02, m mVar) {
        m mVar2 = (m) super.prepareToRecycleView(b02, mVar);
        if (mVar2 != null) {
            mVar2.w();
            setSelectionColor(mVar2, null);
        }
        return mVar;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void setPadding(m mVar, int i3, int i4, int i5, int i6) {
        mVar.setPadding(i3, i4, i5, i6);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void updateExtraData(m mVar, Object obj) {
        C0652c c0652c = new C0652c("ReactTextViewManager.updateExtraData");
        try {
            i iVar = (i) obj;
            Spannable spannableI = iVar.i();
            if (iVar.b()) {
                Z1.p.g(spannableI, mVar);
            }
            mVar.setText(iVar);
            Z1.f[] fVarArr = (Z1.f[]) spannableI.getSpans(0, iVar.i().length(), Z1.f.class);
            mVar.setTag(AbstractC0505m.f9233f, fVarArr.length > 0 ? new n.a(fVarArr, spannableI) : null);
            n.f8002y.a(mVar, mVar.isFocusable(), mVar.getImportantForAccessibility());
            c0652c.close();
        } catch (Throwable th) {
            try {
                c0652c.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(m mVar, C0454s0 c0454s0, A0 a02) {
        C0652c c0652c = new C0652c("ReactTextViewManager.updateState");
        try {
            ReadableMapBuffer readableMapBufferE = a02.e();
            if (readableMapBufferE == null) {
                c0652c.close();
                return null;
            }
            Object reactTextUpdate = getReactTextUpdate(mVar, c0454s0, readableMapBufferE);
            c0652c.close();
            return reactTextUpdate;
        } catch (Throwable th) {
            try {
                c0652c.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.react.uimanager.BaseViewManager
    public void updateViewAccessibility(m mVar) {
        n.f8002y.b(mVar, mVar.isFocusable(), mVar.getImportantForAccessibility());
    }

    public h createShadowNodeInstance(o oVar) {
        return new h(oVar);
    }
}
