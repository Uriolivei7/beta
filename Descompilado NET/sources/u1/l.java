package U1;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.W0;

/* JADX INFO: loaded from: classes.dex */
public interface l extends W0 {
    void clearElementsHighlights(View view);

    void highlightElements(View view, ReadableArray readableArray);

    void highlightTraceUpdates(View view, ReadableArray readableArray);
}
