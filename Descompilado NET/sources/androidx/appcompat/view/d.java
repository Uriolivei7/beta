package androidx.appcompat.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;

/* JADX INFO: loaded from: classes.dex */
public class d extends ContextWrapper {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static Configuration f3361f;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f3362a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Resources.Theme f3363b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private LayoutInflater f3364c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Configuration f3365d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Resources f3366e;

    public d() {
        super(null);
    }

    private Resources b() {
        if (this.f3366e == null) {
            Configuration configuration = this.f3365d;
            if (configuration == null || (Build.VERSION.SDK_INT >= 26 && e(configuration))) {
                this.f3366e = super.getResources();
            } else {
                this.f3366e = createConfigurationContext(this.f3365d).getResources();
            }
        }
        return this.f3366e;
    }

    private void d() {
        boolean z3 = this.f3363b == null;
        if (z3) {
            this.f3363b = getResources().newTheme();
            Resources.Theme theme = getBaseContext().getTheme();
            if (theme != null) {
                this.f3363b.setTo(theme);
            }
        }
        f(this.f3363b, this.f3362a, z3);
    }

    private static boolean e(Configuration configuration) {
        if (configuration == null) {
            return true;
        }
        if (f3361f == null) {
            Configuration configuration2 = new Configuration();
            configuration2.fontScale = 0.0f;
            f3361f = configuration2;
        }
        return configuration.equals(f3361f);
    }

    public void a(Configuration configuration) {
        if (this.f3366e != null) {
            throw new IllegalStateException("getResources() or getAssets() has already been called");
        }
        if (this.f3365d != null) {
            throw new IllegalStateException("Override configuration has already been set");
        }
        this.f3365d = new Configuration(configuration);
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public int c() {
        return this.f3362a;
    }

    protected void f(Resources.Theme theme, int i3, boolean z3) {
        theme.applyStyle(i3, true);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return b();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String str) {
        if (!"layout_inflater".equals(str)) {
            return getBaseContext().getSystemService(str);
        }
        if (this.f3364c == null) {
            this.f3364c = LayoutInflater.from(getBaseContext()).cloneInContext(this);
        }
        return this.f3364c;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        Resources.Theme theme = this.f3363b;
        if (theme != null) {
            return theme;
        }
        if (this.f3362a == 0) {
            this.f3362a = d.i.f8847d;
        }
        d();
        return this.f3363b;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i3) {
        if (this.f3362a != i3) {
            this.f3362a = i3;
            d();
        }
    }

    public d(Context context, int i3) {
        super(context);
        this.f3362a = i3;
    }

    public d(Context context, Resources.Theme theme) {
        super(context);
        this.f3363b = theme;
    }
}
