package U1;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.AbstractC0430g;
import com.facebook.react.uimanager.BaseViewManager;
import com.facebook.react.uimanager.U;

/* JADX INFO: loaded from: classes.dex */
public class k extends AbstractC0430g {
    public k(BaseViewManager<Object, ? extends U> baseViewManager) {
        super(baseViewManager);
    }

    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void a(View view, String str, ReadableArray readableArray) {
        str.hashCode();
        switch (str) {
            case "clearElementsHighlights":
                ((l) this.f7477a).clearElementsHighlights(view);
                break;
            case "highlightTraceUpdates":
                ((l) this.f7477a).highlightTraceUpdates(view, readableArray.getArray(0));
                break;
            case "highlightElements":
                ((l) this.f7477a).highlightElements(view, readableArray.getArray(0));
                break;
        }
    }

    @Override // com.facebook.react.uimanager.AbstractC0430g, com.facebook.react.uimanager.Q0
    public void b(View view, String str, Object obj) {
        super.b(view, str, obj);
    }
}
