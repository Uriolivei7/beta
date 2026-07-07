package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class e0 extends ContextWrapper {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final Object f4207c = new Object();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static ArrayList f4208d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Resources f4209a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Resources.Theme f4210b;

    private e0(Context context) {
        super(context);
        if (!r0.c()) {
            this.f4209a = new g0(this, context.getResources());
            this.f4210b = null;
            return;
        }
        r0 r0Var = new r0(this, context.getResources());
        this.f4209a = r0Var;
        Resources.Theme themeNewTheme = r0Var.newTheme();
        this.f4210b = themeNewTheme;
        themeNewTheme.setTo(context.getTheme());
    }

    private static boolean a(Context context) {
        if ((context instanceof e0) || (context.getResources() instanceof g0) || (context.getResources() instanceof r0)) {
            return false;
        }
        return r0.c();
    }

    public static Context b(Context context) {
        if (!a(context)) {
            return context;
        }
        synchronized (f4207c) {
            try {
                ArrayList arrayList = f4208d;
                if (arrayList == null) {
                    f4208d = new ArrayList();
                } else {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        WeakReference weakReference = (WeakReference) f4208d.get(size);
                        if (weakReference == null || weakReference.get() == null) {
                            f4208d.remove(size);
                        }
                    }
                    for (int size2 = f4208d.size() - 1; size2 >= 0; size2--) {
                        WeakReference weakReference2 = (WeakReference) f4208d.get(size2);
                        e0 e0Var = weakReference2 != null ? (e0) weakReference2.get() : null;
                        if (e0Var != null && e0Var.getBaseContext() == context) {
                            return e0Var;
                        }
                    }
                }
                e0 e0Var2 = new e0(context);
                f4208d.add(new WeakReference(e0Var2));
                return e0Var2;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return this.f4209a.getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return this.f4209a;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        Resources.Theme theme = this.f4210b;
        return theme == null ? super.getTheme() : theme;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i3) {
        Resources.Theme theme = this.f4210b;
        if (theme == null) {
            super.setTheme(i3);
        } else {
            theme.applyStyle(i3, true);
        }
    }
}
