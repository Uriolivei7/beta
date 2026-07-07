package androidx.core.view;

import android.content.ClipData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContentInfo;
import java.util.Objects;

/* JADX INFO: renamed from: androidx.core.view.d, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public final class C0243d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final f f4601a;

    /* JADX INFO: renamed from: androidx.core.view.d$c */
    private interface c {
        C0243d a();

        void b(Bundle bundle);

        void c(Uri uri);

        void d(int i3);
    }

    /* JADX INFO: renamed from: androidx.core.view.d$e */
    private static final class e implements f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ContentInfo f4609a;

        e(ContentInfo contentInfo) {
            this.f4609a = AbstractC0241c.a(q.g.g(contentInfo));
        }

        @Override // androidx.core.view.C0243d.f
        public Bundle a() {
            return this.f4609a.getExtras();
        }

        @Override // androidx.core.view.C0243d.f
        public Uri b() {
            return this.f4609a.getLinkUri();
        }

        @Override // androidx.core.view.C0243d.f
        public ClipData c() {
            return this.f4609a.getClip();
        }

        @Override // androidx.core.view.C0243d.f
        public int d() {
            return this.f4609a.getFlags();
        }

        @Override // androidx.core.view.C0243d.f
        public ContentInfo e() {
            return this.f4609a;
        }

        @Override // androidx.core.view.C0243d.f
        public int f() {
            return this.f4609a.getSource();
        }

        public String toString() {
            return "ContentInfoCompat{" + this.f4609a + "}";
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.d$f */
    private interface f {
        Bundle a();

        Uri b();

        ClipData c();

        int d();

        ContentInfo e();

        int f();
    }

    /* JADX INFO: renamed from: androidx.core.view.d$g */
    private static final class g implements f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ClipData f4610a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f4611b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f4612c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final Uri f4613d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final Bundle f4614e;

        g(C0068d c0068d) {
            this.f4610a = (ClipData) q.g.g(c0068d.f4604a);
            this.f4611b = q.g.c(c0068d.f4605b, 0, 5, "source");
            this.f4612c = q.g.f(c0068d.f4606c, 1);
            this.f4613d = c0068d.f4607d;
            this.f4614e = c0068d.f4608e;
        }

        @Override // androidx.core.view.C0243d.f
        public Bundle a() {
            return this.f4614e;
        }

        @Override // androidx.core.view.C0243d.f
        public Uri b() {
            return this.f4613d;
        }

        @Override // androidx.core.view.C0243d.f
        public ClipData c() {
            return this.f4610a;
        }

        @Override // androidx.core.view.C0243d.f
        public int d() {
            return this.f4612c;
        }

        @Override // androidx.core.view.C0243d.f
        public ContentInfo e() {
            return null;
        }

        @Override // androidx.core.view.C0243d.f
        public int f() {
            return this.f4611b;
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("ContentInfoCompat{clip=");
            sb.append(this.f4610a.getDescription());
            sb.append(", source=");
            sb.append(C0243d.g(this.f4611b));
            sb.append(", flags=");
            sb.append(C0243d.a(this.f4612c));
            if (this.f4613d == null) {
                str = "";
            } else {
                str = ", hasLinkUri(" + this.f4613d.toString().length() + ")";
            }
            sb.append(str);
            sb.append(this.f4614e != null ? ", hasExtras" : "");
            sb.append("}");
            return sb.toString();
        }
    }

    C0243d(f fVar) {
        this.f4601a = fVar;
    }

    static String a(int i3) {
        return (i3 & 1) != 0 ? "FLAG_CONVERT_TO_PLAIN_TEXT" : String.valueOf(i3);
    }

    static String g(int i3) {
        return i3 != 0 ? i3 != 1 ? i3 != 2 ? i3 != 3 ? i3 != 4 ? i3 != 5 ? String.valueOf(i3) : "SOURCE_PROCESS_TEXT" : "SOURCE_AUTOFILL" : "SOURCE_DRAG_AND_DROP" : "SOURCE_INPUT_METHOD" : "SOURCE_CLIPBOARD" : "SOURCE_APP";
    }

    public static C0243d i(ContentInfo contentInfo) {
        return new C0243d(new e(contentInfo));
    }

    public ClipData b() {
        return this.f4601a.c();
    }

    public Bundle c() {
        return this.f4601a.a();
    }

    public int d() {
        return this.f4601a.d();
    }

    public Uri e() {
        return this.f4601a.b();
    }

    public int f() {
        return this.f4601a.f();
    }

    public ContentInfo h() {
        ContentInfo contentInfoE = this.f4601a.e();
        Objects.requireNonNull(contentInfoE);
        return AbstractC0241c.a(contentInfoE);
    }

    public String toString() {
        return this.f4601a.toString();
    }

    /* JADX INFO: renamed from: androidx.core.view.d$b */
    private static final class b implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final ContentInfo.Builder f4603a;

        b(ClipData clipData, int i3) {
            this.f4603a = AbstractC0249g.a(clipData, i3);
        }

        @Override // androidx.core.view.C0243d.c
        public C0243d a() {
            return new C0243d(new e(this.f4603a.build()));
        }

        @Override // androidx.core.view.C0243d.c
        public void b(Bundle bundle) {
            this.f4603a.setExtras(bundle);
        }

        @Override // androidx.core.view.C0243d.c
        public void c(Uri uri) {
            this.f4603a.setLinkUri(uri);
        }

        @Override // androidx.core.view.C0243d.c
        public void d(int i3) {
            this.f4603a.setFlags(i3);
        }

        b(C0243d c0243d) {
            AbstractC0253i.a();
            this.f4603a = AbstractC0251h.a(c0243d.h());
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.d$d, reason: collision with other inner class name */
    private static final class C0068d implements c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        ClipData f4604a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f4605b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        int f4606c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        Uri f4607d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        Bundle f4608e;

        C0068d(ClipData clipData, int i3) {
            this.f4604a = clipData;
            this.f4605b = i3;
        }

        @Override // androidx.core.view.C0243d.c
        public C0243d a() {
            return new C0243d(new g(this));
        }

        @Override // androidx.core.view.C0243d.c
        public void b(Bundle bundle) {
            this.f4608e = bundle;
        }

        @Override // androidx.core.view.C0243d.c
        public void c(Uri uri) {
            this.f4607d = uri;
        }

        @Override // androidx.core.view.C0243d.c
        public void d(int i3) {
            this.f4606c = i3;
        }

        C0068d(C0243d c0243d) {
            this.f4604a = c0243d.b();
            this.f4605b = c0243d.f();
            this.f4606c = c0243d.d();
            this.f4607d = c0243d.e();
            this.f4608e = c0243d.c();
        }
    }

    /* JADX INFO: renamed from: androidx.core.view.d$a */
    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final c f4602a;

        public a(C0243d c0243d) {
            if (Build.VERSION.SDK_INT >= 31) {
                this.f4602a = new b(c0243d);
            } else {
                this.f4602a = new C0068d(c0243d);
            }
        }

        public C0243d a() {
            return this.f4602a.a();
        }

        public a b(Bundle bundle) {
            this.f4602a.b(bundle);
            return this;
        }

        public a c(int i3) {
            this.f4602a.d(i3);
            return this;
        }

        public a d(Uri uri) {
            this.f4602a.c(uri);
            return this;
        }

        public a(ClipData clipData, int i3) {
            if (Build.VERSION.SDK_INT >= 31) {
                this.f4602a = new b(clipData, i3);
            } else {
                this.f4602a = new C0068d(clipData, i3);
            }
        }
    }
}
