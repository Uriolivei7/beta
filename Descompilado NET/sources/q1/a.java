package Q1;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.P;
import e1.C0527d;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
abstract class a {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final Map f1799e = C0527d.g(d.f1813c, new LinearInterpolator(), d.f1814d, new AccelerateInterpolator(), d.f1815e, new DecelerateInterpolator(), d.f1816f, new AccelerateDecelerateInterpolator());

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Interpolator f1800a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f1801b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected b f1802c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected int f1803d;

    a() {
    }

    private static Interpolator c(d dVar, ReadableMap readableMap) {
        Interpolator nVar = dVar.equals(d.f1817g) ? new n(n.a(readableMap)) : (Interpolator) f1799e.get(dVar);
        if (nVar != null) {
            return nVar;
        }
        throw new IllegalArgumentException("Missing interpolator for type : " + dVar);
    }

    public final Animation a(View view, int i3, int i4, int i5, int i6) {
        if (!e()) {
            return null;
        }
        Animation animationB = b(view, i3, i4, i5, i6);
        if (animationB != null) {
            animationB.setDuration(this.f1803d);
            animationB.setStartOffset(this.f1801b);
            animationB.setInterpolator(this.f1800a);
        }
        return animationB;
    }

    abstract Animation b(View view, int i3, int i4, int i5, int i6);

    public void d(ReadableMap readableMap, int i3) {
        this.f1802c = readableMap.hasKey("property") ? b.b(readableMap.getString("property")) : null;
        if (readableMap.hasKey("duration")) {
            i3 = readableMap.getInt("duration");
        }
        this.f1803d = i3;
        this.f1801b = readableMap.hasKey("delay") ? readableMap.getInt("delay") : 0;
        if (!readableMap.hasKey("type")) {
            throw new IllegalArgumentException("Missing interpolation type.");
        }
        this.f1800a = c(d.b(readableMap.getString("type")), readableMap);
        if (e()) {
            return;
        }
        throw new P("Invalid layout animation : " + readableMap);
    }

    abstract boolean e();

    public void f() {
        this.f1802c = null;
        this.f1803d = 0;
        this.f1801b = 0;
        this.f1800a = null;
    }
}
