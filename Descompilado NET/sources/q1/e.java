package Q1;

import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;

/* JADX INFO: loaded from: classes.dex */
public class e {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f1824e;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private Runnable f1826g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Q1.a f1820a = new h();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Q1.a f1821b = new k();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Q1.a f1822c = new i();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final SparseArray f1823d = new SparseArray(0);

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private long f1825f = -1;

    class a implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Callback f1827b;

        a(Callback callback) {
            this.f1827b = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f1827b.invoke(Boolean.TRUE);
        }
    }

    private void d(View view) {
        view.setClickable(false);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                d(viewGroup.getChildAt(i3));
            }
        }
    }

    private void g(long j3) {
        if (this.f1826g != null) {
            Handler uiThreadHandler = UiThreadUtil.getUiThreadHandler();
            uiThreadHandler.removeCallbacks(this.f1826g);
            uiThreadHandler.postDelayed(this.f1826g, j3);
        }
    }

    public void b(View view, int i3, int i4, int i5, int i6) {
        UiThreadUtil.assertOnUiThread();
        int id = view.getId();
        j jVar = (j) this.f1823d.get(id);
        if (jVar != null) {
            jVar.b(i3, i4, i5, i6);
            return;
        }
        Animation animationA = ((view.getWidth() == 0 || view.getHeight() == 0) ? this.f1820a : this.f1821b).a(view, i3, i4, i5, i6);
        if (animationA instanceof j) {
            animationA.setAnimationListener(new b(id));
        } else {
            view.layout(i3, i4, i5 + i3, i6 + i4);
        }
        if (animationA != null) {
            long duration = animationA.getDuration();
            if (duration > this.f1825f) {
                this.f1825f = duration;
                g(duration);
            }
            view.startAnimation(animationA);
        }
    }

    public void c(View view, f fVar) {
        UiThreadUtil.assertOnUiThread();
        Animation animationA = this.f1822c.a(view, view.getLeft(), view.getTop(), view.getWidth(), view.getHeight());
        if (animationA == null) {
            fVar.a();
            return;
        }
        d(view);
        animationA.setAnimationListener(new c(fVar));
        long duration = animationA.getDuration();
        if (duration > this.f1825f) {
            g(duration);
            this.f1825f = duration;
        }
        view.startAnimation(animationA);
    }

    public void e(ReadableMap readableMap, Callback callback) {
        if (readableMap == null) {
            f();
            return;
        }
        this.f1824e = false;
        int i3 = readableMap.hasKey("duration") ? readableMap.getInt("duration") : 0;
        g gVar = g.f1834c;
        if (readableMap.hasKey(g.b(gVar))) {
            this.f1820a.d(readableMap.getMap(g.b(gVar)), i3);
            this.f1824e = true;
        }
        g gVar2 = g.f1835d;
        if (readableMap.hasKey(g.b(gVar2))) {
            this.f1821b.d(readableMap.getMap(g.b(gVar2)), i3);
            this.f1824e = true;
        }
        g gVar3 = g.f1836e;
        if (readableMap.hasKey(g.b(gVar3))) {
            this.f1822c.d(readableMap.getMap(g.b(gVar3)), i3);
            this.f1824e = true;
        }
        if (!this.f1824e || callback == null) {
            return;
        }
        this.f1826g = new a(callback);
    }

    public void f() {
        this.f1820a.f();
        this.f1821b.f();
        this.f1822c.f();
        this.f1826g = null;
        this.f1824e = false;
        this.f1825f = -1L;
    }

    public boolean h(View view) {
        if (view == null) {
            return false;
        }
        return (this.f1824e && view.getParent() != null) || this.f1823d.get(view.getId()) != null;
    }

    class b implements Animation.AnimationListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f1829a;

        b(int i3) {
            this.f1829a = i3;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            e.this.f1823d.remove(this.f1829a);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            e.this.f1823d.put(this.f1829a, (j) animation);
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }
    }

    class c implements Animation.AnimationListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ f f1831a;

        c(f fVar) {
            this.f1831a = fVar;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            this.f1831a.a();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }
}
