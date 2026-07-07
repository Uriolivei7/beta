package U1;

import android.view.View;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.W0;

/* JADX INFO: loaded from: classes.dex */
public interface n extends W0 {
    void setAnimated(View view, boolean z3);

    void setAnimationType(View view, String str);

    void setHardwareAccelerated(View view, boolean z3);

    void setIdentifier(View view, int i3);

    void setNavigationBarTranslucent(View view, boolean z3);

    void setPresentationStyle(View view, String str);

    void setStatusBarTranslucent(View view, boolean z3);

    void setSupportedOrientations(View view, ReadableArray readableArray);

    void setTransparent(View view, boolean z3);

    void setVisible(View view, boolean z3);
}
