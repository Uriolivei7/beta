package R1;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Shader;
import com.facebook.react.bridge.ReadableMap;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final l f1997a;

    public a(ReadableMap readableMap, Context context) {
        D2.h.f(context, "context");
        l lVar = null;
        if (readableMap != null) {
            try {
                lVar = new l(readableMap, context);
            } catch (IllegalArgumentException unused) {
            }
        }
        this.f1997a = lVar;
    }

    public final Shader a(Rect rect) {
        D2.h.f(rect, "bounds");
        l lVar = this.f1997a;
        if (lVar != null) {
            return lVar.a(rect);
        }
        return null;
    }
}
