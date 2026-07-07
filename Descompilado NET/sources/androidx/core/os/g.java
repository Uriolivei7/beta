package androidx.core.os;

import android.os.LocaleList;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
final class g implements f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final LocaleList f4523a;

    g(Object obj) {
        this.f4523a = (LocaleList) obj;
    }

    @Override // androidx.core.os.f
    public String a() {
        return this.f4523a.toLanguageTags();
    }

    @Override // androidx.core.os.f
    public Object b() {
        return this.f4523a;
    }

    public boolean equals(Object obj) {
        return this.f4523a.equals(((f) obj).b());
    }

    @Override // androidx.core.os.f
    public Locale get(int i3) {
        return this.f4523a.get(i3);
    }

    public int hashCode() {
        return this.f4523a.hashCode();
    }

    @Override // androidx.core.os.f
    public boolean isEmpty() {
        return this.f4523a.isEmpty();
    }

    @Override // androidx.core.os.f
    public int size() {
        return this.f4523a.size();
    }

    public String toString() {
        return this.f4523a.toString();
    }
}
