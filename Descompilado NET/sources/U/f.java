package u;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.view.inputmethod.InputContentInfo;

/* JADX INFO: loaded from: classes.dex */
public final class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final c f10799a;

    private interface c {
        Object a();

        Uri b();

        Uri c();

        void d();

        ClipDescription getDescription();
    }

    public f(Uri uri, ClipDescription clipDescription, Uri uri2) {
        if (Build.VERSION.SDK_INT >= 25) {
            this.f10799a = new a(uri, clipDescription, uri2);
        } else {
            this.f10799a = new b(uri, clipDescription, uri2);
        }
    }

    public static f f(Object obj) {
        if (obj != null && Build.VERSION.SDK_INT >= 25) {
            return new f(new a(obj));
        }
        return null;
    }

    public Uri a() {
        return this.f10799a.c();
    }

    public ClipDescription b() {
        return this.f10799a.getDescription();
    }

    public Uri c() {
        return this.f10799a.b();
    }

    public void d() {
        this.f10799a.d();
    }

    public Object e() {
        return this.f10799a.a();
    }

    private static final class a implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final InputContentInfo f10800a;

        a(Object obj) {
            this.f10800a = (InputContentInfo) obj;
        }

        @Override // u.f.c
        public Object a() {
            return this.f10800a;
        }

        @Override // u.f.c
        public Uri b() {
            return this.f10800a.getLinkUri();
        }

        @Override // u.f.c
        public Uri c() {
            return this.f10800a.getContentUri();
        }

        @Override // u.f.c
        public void d() {
            this.f10800a.requestPermission();
        }

        @Override // u.f.c
        public ClipDescription getDescription() {
            return this.f10800a.getDescription();
        }

        a(Uri uri, ClipDescription clipDescription, Uri uri2) {
            this.f10800a = new InputContentInfo(uri, clipDescription, uri2);
        }
    }

    private f(c cVar) {
        this.f10799a = cVar;
    }

    private static final class b implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Uri f10801a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final ClipDescription f10802b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final Uri f10803c;

        b(Uri uri, ClipDescription clipDescription, Uri uri2) {
            this.f10801a = uri;
            this.f10802b = clipDescription;
            this.f10803c = uri2;
        }

        @Override // u.f.c
        public Object a() {
            return null;
        }

        @Override // u.f.c
        public Uri b() {
            return this.f10803c;
        }

        @Override // u.f.c
        public Uri c() {
            return this.f10801a;
        }

        @Override // u.f.c
        public ClipDescription getDescription() {
            return this.f10802b;
        }

        @Override // u.f.c
        public void d() {
        }
    }
}
