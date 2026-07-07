package p;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;

/* JADX INFO: loaded from: classes.dex */
public abstract class g {

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f10249a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final b[] f10250b;

        @Deprecated
        public a(int i3, b[] bVarArr) {
            this.f10249a = i3;
            this.f10250b = bVarArr;
        }

        static a a(int i3, b[] bVarArr) {
            return new a(i3, bVarArr);
        }

        public b[] b() {
            return this.f10250b;
        }

        public int c() {
            return this.f10249a;
        }
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Uri f10251a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f10252b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final int f10253c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final boolean f10254d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private final int f10255e;

        @Deprecated
        public b(Uri uri, int i3, int i4, boolean z3, int i5) {
            this.f10251a = (Uri) q.g.g(uri);
            this.f10252b = i3;
            this.f10253c = i4;
            this.f10254d = z3;
            this.f10255e = i5;
        }

        static b a(Uri uri, int i3, int i4, boolean z3, int i5) {
            return new b(uri, i3, i4, z3, i5);
        }

        public int b() {
            return this.f10255e;
        }

        public int c() {
            return this.f10252b;
        }

        public Uri d() {
            return this.f10251a;
        }

        public int e() {
            return this.f10253c;
        }

        public boolean f() {
            return this.f10254d;
        }
    }

    public static Typeface a(Context context, CancellationSignal cancellationSignal, b[] bVarArr) {
        return androidx.core.graphics.d.b(context, cancellationSignal, bVarArr, 0);
    }

    public static a b(Context context, CancellationSignal cancellationSignal, e eVar) {
        return d.e(context, eVar, cancellationSignal);
    }

    public static Typeface c(Context context, e eVar, int i3, boolean z3, int i4, Handler handler, c cVar) {
        C0632a c0632a = new C0632a(cVar, handler);
        return z3 ? f.e(context, eVar, c0632a, i3, i4) : f.d(context, eVar, i3, null, c0632a);
    }

    public static class c {
        public void a(int i3) {
        }

        public void b(Typeface typeface) {
        }
    }
}
