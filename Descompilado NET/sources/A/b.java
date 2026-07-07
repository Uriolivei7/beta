package A;

import android.text.Editable;
import androidx.emoji2.text.p;

/* JADX INFO: loaded from: classes.dex */
final class b extends Editable.Factory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Object f5a = new Object();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static volatile Editable.Factory f6b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Class f7c;

    private b() {
        try {
            f7c = Class.forName("android.text.DynamicLayout$ChangeWatcher", false, b.class.getClassLoader());
        } catch (Throwable unused) {
        }
    }

    public static Editable.Factory getInstance() {
        if (f6b == null) {
            synchronized (f5a) {
                try {
                    if (f6b == null) {
                        f6b = new b();
                    }
                } finally {
                }
            }
        }
        return f6b;
    }

    @Override // android.text.Editable.Factory
    public Editable newEditable(CharSequence charSequence) {
        Class cls = f7c;
        return cls != null ? p.c(cls, charSequence) : super.newEditable(charSequence);
    }
}
