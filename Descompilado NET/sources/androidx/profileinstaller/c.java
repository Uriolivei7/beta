package androidx.profileinstaller;

import android.content.res.AssetManager;
import android.os.Build;
import androidx.profileinstaller.i;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final AssetManager f5386a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Executor f5387b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final i.c f5388c;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final File f5390e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final String f5391f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final String f5392g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private final String f5393h;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private d[] f5395j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private byte[] f5396k;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private boolean f5394i = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final byte[] f5389d = d();

    public c(AssetManager assetManager, Executor executor, i.c cVar, String str, String str2, String str3, File file) {
        this.f5386a = assetManager;
        this.f5387b = executor;
        this.f5388c = cVar;
        this.f5391f = str;
        this.f5392g = str2;
        this.f5393h = str3;
        this.f5390e = file;
    }

    private c b(d[] dVarArr, byte[] bArr) {
        InputStream inputStreamH;
        try {
            inputStreamH = h(this.f5386a, this.f5393h);
        } catch (FileNotFoundException e4) {
            this.f5388c.b(9, e4);
        } catch (IOException e5) {
            this.f5388c.b(7, e5);
        } catch (IllegalStateException e6) {
            this.f5395j = null;
            this.f5388c.b(8, e6);
        }
        if (inputStreamH == null) {
            if (inputStreamH != null) {
                inputStreamH.close();
            }
            return null;
        }
        try {
            this.f5395j = n.q(inputStreamH, n.o(inputStreamH, n.f5424b), bArr, dVarArr);
            inputStreamH.close();
            return this;
        } catch (Throwable th) {
            try {
                inputStreamH.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void c() {
        if (!this.f5394i) {
            throw new IllegalStateException("This device doesn't support aot. Did you call deviceSupportsAotProfile()?");
        }
    }

    private static byte[] d() {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 > 34) {
            return null;
        }
        switch (i3) {
        }
        return null;
    }

    private InputStream f(AssetManager assetManager) {
        try {
            return h(assetManager, this.f5392g);
        } catch (FileNotFoundException e4) {
            this.f5388c.b(6, e4);
            return null;
        } catch (IOException e5) {
            this.f5388c.b(7, e5);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(int i3, Object obj) {
        this.f5388c.b(i3, obj);
    }

    private InputStream h(AssetManager assetManager, String str) {
        try {
            return assetManager.openFd(str).createInputStream();
        } catch (FileNotFoundException e4) {
            String message = e4.getMessage();
            if (message != null && message.contains("compressed")) {
                this.f5388c.a(5, null);
            }
            return null;
        }
    }

    private d[] j(InputStream inputStream) {
        try {
            try {
                try {
                    try {
                        d[] dVarArrW = n.w(inputStream, n.o(inputStream, n.f5423a), this.f5391f);
                        try {
                            inputStream.close();
                            return dVarArrW;
                        } catch (IOException e4) {
                            this.f5388c.b(7, e4);
                            return dVarArrW;
                        }
                    } catch (IOException e5) {
                        this.f5388c.b(7, e5);
                        return null;
                    }
                } catch (IllegalStateException e6) {
                    this.f5388c.b(8, e6);
                    inputStream.close();
                    return null;
                }
            } catch (IOException e7) {
                this.f5388c.b(7, e7);
                inputStream.close();
                return null;
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (IOException e8) {
                this.f5388c.b(7, e8);
            }
            throw th;
        }
    }

    private static boolean k() {
        int i3 = Build.VERSION.SDK_INT;
        if (i3 > 34) {
            return false;
        }
        if (i3 != 24 && i3 != 25) {
            switch (i3) {
            }
            return false;
        }
        return true;
    }

    private void l(final int i3, final Object obj) {
        this.f5387b.execute(new Runnable() { // from class: androidx.profileinstaller.b
            @Override // java.lang.Runnable
            public final void run() {
                this.f5383b.g(i3, obj);
            }
        });
    }

    public boolean e() {
        if (this.f5389d == null) {
            l(3, Integer.valueOf(Build.VERSION.SDK_INT));
            return false;
        }
        if (!this.f5390e.exists()) {
            try {
                this.f5390e.createNewFile();
            } catch (IOException unused) {
                l(4, null);
                return false;
            }
        } else if (!this.f5390e.canWrite()) {
            l(4, null);
            return false;
        }
        this.f5394i = true;
        return true;
    }

    public c i() {
        c cVarB;
        c();
        if (this.f5389d == null) {
            return this;
        }
        InputStream inputStreamF = f(this.f5386a);
        if (inputStreamF != null) {
            this.f5395j = j(inputStreamF);
        }
        d[] dVarArr = this.f5395j;
        return (dVarArr == null || !k() || (cVarB = b(dVarArr, this.f5389d)) == null) ? this : cVarB;
    }

    public c m() {
        ByteArrayOutputStream byteArrayOutputStream;
        d[] dVarArr = this.f5395j;
        byte[] bArr = this.f5389d;
        if (dVarArr != null && bArr != null) {
            c();
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    n.E(byteArrayOutputStream, bArr);
                } catch (Throwable th) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e4) {
                this.f5388c.b(7, e4);
            } catch (IllegalStateException e5) {
                this.f5388c.b(8, e5);
            }
            if (!n.B(byteArrayOutputStream, bArr, dVarArr)) {
                this.f5388c.b(5, null);
                this.f5395j = null;
                byteArrayOutputStream.close();
                return this;
            }
            this.f5396k = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            this.f5395j = null;
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean n() {
        byte[] bArr = this.f5396k;
        if (bArr == null) {
            return false;
        }
        c();
        try {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(this.f5390e);
                    try {
                        e.l(byteArrayInputStream, fileOutputStream);
                        l(1, null);
                        fileOutputStream.close();
                        byteArrayInputStream.close();
                        return true;
                    } finally {
                    }
                } catch (Throwable th) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } finally {
                this.f5396k = null;
                this.f5395j = null;
            }
        } catch (FileNotFoundException e4) {
            l(6, e4);
            return false;
        } catch (IOException e5) {
            l(7, e5);
            return false;
        }
    }
}
