package androidx.profileinstaller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public abstract class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final androidx.concurrent.futures.c f5425a = androidx.concurrent.futures.c.o();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final Object f5426b = new Object();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static c f5427c = null;

    private static class a {
        static PackageInfo a(PackageManager packageManager, Context context) {
            return packageManager.getPackageInfo(context.getPackageName(), PackageManager.PackageInfoFlags.of(0L));
        }
    }

    static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final int f5428a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final int f5429b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final long f5430c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final long f5431d;

        b(int i3, int i4, long j3, long j4) {
            this.f5428a = i3;
            this.f5429b = i4;
            this.f5430c = j3;
            this.f5431d = j4;
        }

        static b a(File file) throws IOException {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            try {
                b bVar = new b(dataInputStream.readInt(), dataInputStream.readInt(), dataInputStream.readLong(), dataInputStream.readLong());
                dataInputStream.close();
                return bVar;
            } catch (Throwable th) {
                try {
                    dataInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        void b(File file) throws IOException {
            file.delete();
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            try {
                dataOutputStream.writeInt(this.f5428a);
                dataOutputStream.writeInt(this.f5429b);
                dataOutputStream.writeLong(this.f5430c);
                dataOutputStream.writeLong(this.f5431d);
                dataOutputStream.close();
            } catch (Throwable th) {
                try {
                    dataOutputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof b)) {
                return false;
            }
            b bVar = (b) obj;
            return this.f5429b == bVar.f5429b && this.f5430c == bVar.f5430c && this.f5428a == bVar.f5428a && this.f5431d == bVar.f5431d;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.f5429b), Long.valueOf(this.f5430c), Integer.valueOf(this.f5428a), Long.valueOf(this.f5431d));
        }
    }

    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final int f5432a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final boolean f5433b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final boolean f5434c;

        c(int i3, boolean z3, boolean z4) {
            this.f5432a = i3;
            this.f5434c = z4;
            this.f5433b = z3;
        }
    }

    private static long a(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        return Build.VERSION.SDK_INT >= 33 ? a.a(packageManager, context).lastUpdateTime : packageManager.getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
    }

    private static c b(int i3, boolean z3, boolean z4) {
        c cVar = new c(i3, z3, z4);
        f5427c = cVar;
        f5425a.m(cVar);
        return f5427c;
    }

    static c c(Context context, boolean z3) {
        b bVarA;
        int i3;
        c cVar;
        if (!z3 && (cVar = f5427c) != null) {
            return cVar;
        }
        synchronized (f5426b) {
            if (!z3) {
                try {
                    c cVar2 = f5427c;
                    if (cVar2 != null) {
                        return cVar2;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            int i4 = Build.VERSION.SDK_INT;
            int i5 = 0;
            if (i4 >= 28 && i4 != 30) {
                File file = new File(new File("/data/misc/profiles/ref/", context.getPackageName()), "primary.prof");
                long length = file.length();
                boolean z4 = file.exists() && length > 0;
                File file2 = new File(new File("/data/misc/profiles/cur/0/", context.getPackageName()), "primary.prof");
                long length2 = file2.length();
                boolean z5 = file2.exists() && length2 > 0;
                try {
                    long jA = a(context);
                    File file3 = new File(context.getFilesDir(), "profileInstalled");
                    if (file3.exists()) {
                        try {
                            bVarA = b.a(file3);
                        } catch (IOException unused) {
                            return b(131072, z4, z5);
                        }
                    } else {
                        bVarA = null;
                    }
                    if (bVarA != null && bVarA.f5430c == jA && (i3 = bVarA.f5429b) != 2) {
                        i5 = i3;
                    } else if (z4) {
                        i5 = 1;
                    } else if (z5) {
                        i5 = 2;
                    }
                    if (z3 && z5 && i5 != 1) {
                        i5 = 2;
                    }
                    if (bVarA != null && bVarA.f5429b == 2 && i5 == 1 && length < bVarA.f5431d) {
                        i5 = 3;
                    }
                    b bVar = new b(1, i5, jA, length2);
                    if (bVarA == null || !bVarA.equals(bVar)) {
                        try {
                            bVar.b(file3);
                        } catch (IOException unused2) {
                            i5 = 196608;
                        }
                    }
                    return b(i5, z4, z5);
                } catch (PackageManager.NameNotFoundException unused3) {
                    return b(65536, z4, z5);
                }
            }
            return b(262144, false, false);
        }
    }
}
