package androidx.emoji2.text;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private static final Object f4755o = new Object();

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static final Object f4756p = new Object();

    /* JADX INFO: renamed from: q, reason: collision with root package name */
    private static volatile f f4757q;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Set f4759b;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final b f4762e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    final h f4763f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final j f4764g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    final boolean f4765h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    final boolean f4766i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    final int[] f4767j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final boolean f4768k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private final int f4769l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private final int f4770m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private final e f4771n;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final ReadWriteLock f4758a = new ReentrantReadWriteLock();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile int f4760c = 3;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Handler f4761d = new Handler(Looper.getMainLooper());

    private static final class a extends b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private volatile androidx.emoji2.text.i f4772b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private volatile o f4773c;

        /* JADX INFO: renamed from: androidx.emoji2.text.f$a$a, reason: collision with other inner class name */
        class C0071a extends i {
            C0071a() {
            }

            @Override // androidx.emoji2.text.f.i
            public void a(Throwable th) {
                a.this.f4775a.n(th);
            }

            @Override // androidx.emoji2.text.f.i
            public void b(o oVar) {
                a.this.d(oVar);
            }
        }

        a(f fVar) {
            super(fVar);
        }

        @Override // androidx.emoji2.text.f.b
        void a() {
            try {
                this.f4775a.f4763f.a(new C0071a());
            } catch (Throwable th) {
                this.f4775a.n(th);
            }
        }

        @Override // androidx.emoji2.text.f.b
        CharSequence b(CharSequence charSequence, int i3, int i4, int i5, boolean z3) {
            return this.f4772b.h(charSequence, i3, i4, i5, z3);
        }

        @Override // androidx.emoji2.text.f.b
        void c(EditorInfo editorInfo) {
            editorInfo.extras.putInt("android.support.text.emoji.emojiCompat_metadataVersion", this.f4773c.e());
            editorInfo.extras.putBoolean("android.support.text.emoji.emojiCompat_replaceAll", this.f4775a.f4765h);
        }

        void d(o oVar) {
            if (oVar == null) {
                this.f4775a.n(new IllegalArgumentException("metadataRepo cannot be null"));
                return;
            }
            this.f4773c = oVar;
            o oVar2 = this.f4773c;
            j jVar = this.f4775a.f4764g;
            e eVar = this.f4775a.f4771n;
            f fVar = this.f4775a;
            this.f4772b = new androidx.emoji2.text.i(oVar2, jVar, eVar, fVar.f4766i, fVar.f4767j, androidx.emoji2.text.h.a());
            this.f4775a.o();
        }
    }

    private static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final f f4775a;

        b(f fVar) {
            this.f4775a = fVar;
        }

        abstract void a();

        abstract CharSequence b(CharSequence charSequence, int i3, int i4, int i5, boolean z3);

        abstract void c(EditorInfo editorInfo);
    }

    public static abstract class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final h f4776a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        j f4777b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        boolean f4778c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        boolean f4779d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        int[] f4780e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Set f4781f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        boolean f4782g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        int f4783h = -16711936;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        int f4784i = 0;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        e f4785j = new androidx.emoji2.text.e();

        protected c(h hVar) {
            q.g.h(hVar, "metadataLoader cannot be null.");
            this.f4776a = hVar;
        }

        protected final h a() {
            return this.f4776a;
        }

        public c b(int i3) {
            this.f4784i = i3;
            return this;
        }
    }

    public static class d implements j {
        @Override // androidx.emoji2.text.f.j
        public androidx.emoji2.text.j a(q qVar) {
            return new r(qVar);
        }
    }

    public interface e {
        boolean a(CharSequence charSequence, int i3, int i4, int i5);
    }

    /* JADX INFO: renamed from: androidx.emoji2.text.f$f, reason: collision with other inner class name */
    public static abstract class AbstractC0072f {
        public void a(Throwable th) {
        }

        public void b() {
        }
    }

    private static class g implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final List f4786b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Throwable f4787c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final int f4788d;

        g(AbstractC0072f abstractC0072f, int i3) {
            this(Arrays.asList((AbstractC0072f) q.g.h(abstractC0072f, "initCallback cannot be null")), i3, null);
        }

        @Override // java.lang.Runnable
        public void run() {
            int size = this.f4786b.size();
            int i3 = 0;
            if (this.f4788d != 1) {
                while (i3 < size) {
                    ((AbstractC0072f) this.f4786b.get(i3)).a(this.f4787c);
                    i3++;
                }
            } else {
                while (i3 < size) {
                    ((AbstractC0072f) this.f4786b.get(i3)).b();
                    i3++;
                }
            }
        }

        g(Collection collection, int i3) {
            this(collection, i3, null);
        }

        g(Collection collection, int i3, Throwable th) {
            q.g.h(collection, "initCallbacks cannot be null");
            this.f4786b = new ArrayList(collection);
            this.f4788d = i3;
            this.f4787c = th;
        }
    }

    public interface h {
        void a(i iVar);
    }

    public static abstract class i {
        public abstract void a(Throwable th);

        public abstract void b(o oVar);
    }

    public interface j {
        androidx.emoji2.text.j a(q qVar);
    }

    private f(c cVar) {
        this.f4765h = cVar.f4778c;
        this.f4766i = cVar.f4779d;
        this.f4767j = cVar.f4780e;
        this.f4768k = cVar.f4782g;
        this.f4769l = cVar.f4783h;
        this.f4763f = cVar.f4776a;
        this.f4770m = cVar.f4784i;
        this.f4771n = cVar.f4785j;
        l.b bVar = new l.b();
        this.f4759b = bVar;
        j jVar = cVar.f4777b;
        this.f4764g = jVar == null ? new d() : jVar;
        Set set = cVar.f4781f;
        if (set != null && !set.isEmpty()) {
            bVar.addAll(cVar.f4781f);
        }
        this.f4762e = new a(this);
        m();
    }

    public static f c() {
        f fVar;
        synchronized (f4755o) {
            fVar = f4757q;
            q.g.i(fVar != null, "EmojiCompat is not initialized.\n\nYou must initialize EmojiCompat prior to referencing the EmojiCompat instance.\n\nThe most likely cause of this error is disabling the EmojiCompatInitializer\neither explicitly in AndroidManifest.xml, or by including\nandroidx.emoji2:emoji2-bundled.\n\nAutomatic initialization is typically performed by EmojiCompatInitializer. If\nyou are not expecting to initialize EmojiCompat manually in your application,\nplease check to ensure it has not been removed from your APK's manifest. You can\ndo this in Android Studio using Build > Analyze APK.\n\nIn the APK Analyzer, ensure that the startup entry for\nEmojiCompatInitializer and InitializationProvider is present in\n AndroidManifest.xml. If it is missing or contains tools:node=\"remove\", and you\nintend to use automatic configuration, verify:\n\n  1. Your application does not include emoji2-bundled\n  2. All modules do not contain an exclusion manifest rule for\n     EmojiCompatInitializer or InitializationProvider. For more information\n     about manifest exclusions see the documentation for the androidx startup\n     library.\n\nIf you intend to use emoji2-bundled, please call EmojiCompat.init. You can\nlearn more in the documentation for BundledEmojiCompatConfig.\n\nIf you intended to perform manual configuration, it is recommended that you call\nEmojiCompat.init immediately on application startup.\n\nIf you still cannot resolve this issue, please open a bug with your specific\nconfiguration to help improve error message.");
        }
        return fVar;
    }

    public static boolean f(InputConnection inputConnection, Editable editable, int i3, int i4, boolean z3) {
        return androidx.emoji2.text.i.b(inputConnection, editable, i3, i4, z3);
    }

    public static boolean g(Editable editable, int i3, KeyEvent keyEvent) {
        return androidx.emoji2.text.i.c(editable, i3, keyEvent);
    }

    public static f h(c cVar) {
        f fVar = f4757q;
        if (fVar == null) {
            synchronized (f4755o) {
                try {
                    fVar = f4757q;
                    if (fVar == null) {
                        fVar = new f(cVar);
                        f4757q = fVar;
                    }
                } finally {
                }
            }
        }
        return fVar;
    }

    public static boolean i() {
        return f4757q != null;
    }

    private boolean k() {
        return e() == 1;
    }

    private void m() {
        this.f4758a.writeLock().lock();
        try {
            if (this.f4770m == 0) {
                this.f4760c = 0;
            }
            this.f4758a.writeLock().unlock();
            if (e() == 0) {
                this.f4762e.a();
            }
        } catch (Throwable th) {
            this.f4758a.writeLock().unlock();
            throw th;
        }
    }

    public int d() {
        return this.f4769l;
    }

    public int e() {
        this.f4758a.readLock().lock();
        try {
            return this.f4760c;
        } finally {
            this.f4758a.readLock().unlock();
        }
    }

    public boolean j() {
        return this.f4768k;
    }

    public void l() {
        q.g.i(this.f4770m == 1, "Set metadataLoadStrategy to LOAD_STRATEGY_MANUAL to execute manual loading");
        if (k()) {
            return;
        }
        this.f4758a.writeLock().lock();
        try {
            if (this.f4760c == 0) {
                return;
            }
            this.f4760c = 0;
            this.f4758a.writeLock().unlock();
            this.f4762e.a();
        } finally {
            this.f4758a.writeLock().unlock();
        }
    }

    void n(Throwable th) {
        ArrayList arrayList = new ArrayList();
        this.f4758a.writeLock().lock();
        try {
            this.f4760c = 2;
            arrayList.addAll(this.f4759b);
            this.f4759b.clear();
            this.f4758a.writeLock().unlock();
            this.f4761d.post(new g(arrayList, this.f4760c, th));
        } catch (Throwable th2) {
            this.f4758a.writeLock().unlock();
            throw th2;
        }
    }

    void o() {
        ArrayList arrayList = new ArrayList();
        this.f4758a.writeLock().lock();
        try {
            this.f4760c = 1;
            arrayList.addAll(this.f4759b);
            this.f4759b.clear();
            this.f4758a.writeLock().unlock();
            this.f4761d.post(new g(arrayList, this.f4760c));
        } catch (Throwable th) {
            this.f4758a.writeLock().unlock();
            throw th;
        }
    }

    public CharSequence p(CharSequence charSequence) {
        return q(charSequence, 0, charSequence == null ? 0 : charSequence.length());
    }

    public CharSequence q(CharSequence charSequence, int i3, int i4) {
        return r(charSequence, i3, i4, Integer.MAX_VALUE);
    }

    public CharSequence r(CharSequence charSequence, int i3, int i4, int i5) {
        return s(charSequence, i3, i4, i5, 0);
    }

    public CharSequence s(CharSequence charSequence, int i3, int i4, int i5, int i6) {
        boolean z3;
        q.g.i(k(), "Not initialized yet");
        q.g.e(i3, "start cannot be negative");
        q.g.e(i4, "end cannot be negative");
        q.g.e(i5, "maxEmojiCount cannot be negative");
        q.g.b(i3 <= i4, "start should be <= than end");
        if (charSequence == null) {
            return null;
        }
        q.g.b(i3 <= charSequence.length(), "start should be < than charSequence length");
        q.g.b(i4 <= charSequence.length(), "end should be < than charSequence length");
        if (charSequence.length() == 0 || i3 == i4) {
            return charSequence;
        }
        if (i6 != 1) {
            z3 = i6 != 2 ? this.f4765h : false;
        } else {
            z3 = true;
        }
        return this.f4762e.b(charSequence, i3, i4, i5, z3);
    }

    public void t(AbstractC0072f abstractC0072f) {
        q.g.h(abstractC0072f, "initCallback cannot be null");
        this.f4758a.writeLock().lock();
        try {
            if (this.f4760c == 1 || this.f4760c == 2) {
                this.f4761d.post(new g(abstractC0072f, this.f4760c));
            } else {
                this.f4759b.add(abstractC0072f);
            }
            this.f4758a.writeLock().unlock();
        } catch (Throwable th) {
            this.f4758a.writeLock().unlock();
            throw th;
        }
    }

    public void u(AbstractC0072f abstractC0072f) {
        q.g.h(abstractC0072f, "initCallback cannot be null");
        this.f4758a.writeLock().lock();
        try {
            this.f4759b.remove(abstractC0072f);
        } finally {
            this.f4758a.writeLock().unlock();
        }
    }

    public void v(EditorInfo editorInfo) {
        if (!k() || editorInfo == null) {
            return;
        }
        if (editorInfo.extras == null) {
            editorInfo.extras = new Bundle();
        }
        this.f4762e.c(editorInfo);
    }
}
