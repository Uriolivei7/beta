package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.SyncFailedException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class G extends C0485f implements InterfaceC0481b {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected final Context f8192d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private String[] f8193e;

    class a implements FilenameFilter {
        a() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            return (str.equals("dso_state") || str.equals("dso_lock") || str.equals("dso_deps")) ? false : true;
        }
    }

    class b implements Runnable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean f8195b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ File f8196c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ n f8197d;

        b(boolean z3, File file, n nVar) {
            this.f8195b = z3;
            this.f8196c = file;
            this.f8197d = nVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            p.f("fb-UnpackingSoSource", "starting syncer worker");
            try {
                try {
                    if (this.f8195b) {
                        SysUtil.f(G.this.f8229a);
                    }
                    G.u(this.f8196c, (byte) 1, this.f8195b);
                    p.f("fb-UnpackingSoSource", "releasing dso store lock for " + G.this.f8229a + " (from syncer thread)");
                    this.f8197d.close();
                } catch (Throwable th) {
                    p.f("fb-UnpackingSoSource", "releasing dso store lock for " + G.this.f8229a + " (from syncer thread)");
                    this.f8197d.close();
                    throw th;
                }
            } catch (IOException e4) {
                throw new RuntimeException(e4);
            }
        }
    }

    public static class c {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f8199b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f8200c;

        public c(String str, String str2) {
            this.f8199b = str;
            this.f8200c = str2;
        }
    }

    protected static final class d implements Closeable {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final c f8201b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final InputStream f8202c;

        public d(c cVar, InputStream inputStream) {
            this.f8201b = cVar;
            this.f8202c = inputStream;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.f8202c.close();
        }

        public int i() {
            return this.f8202c.available();
        }

        public c o() {
            return this.f8201b;
        }
    }

    protected static abstract class e implements Closeable {
        protected e() {
        }

        public void a(d dVar, byte[] bArr, File file) {
            p.d("fb-UnpackingSoSource", "extracting DSO " + dVar.o().f8199b);
            File file2 = new File(file, dVar.o().f8199b);
            try {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
                    try {
                        int i3 = dVar.i();
                        if (i3 > 1) {
                            SysUtil.d(randomAccessFile.getFD(), i3);
                        }
                        SysUtil.a(randomAccessFile, dVar.f8202c, Integer.MAX_VALUE, bArr);
                        randomAccessFile.setLength(randomAccessFile.getFilePointer());
                        if (file2.setExecutable(true, false)) {
                            randomAccessFile.close();
                        } else {
                            throw new IOException("cannot make file executable: " + file2);
                        }
                    } finally {
                    }
                } catch (IOException e4) {
                    p.b("fb-UnpackingSoSource", "error extracting dso  " + file2 + " due to: " + e4);
                    SysUtil.c(file2);
                    throw e4;
                }
            } finally {
                if (file2.exists() && !file2.setWritable(false)) {
                    p.b("SoLoader", "Error removing " + file2 + " write permission from directory " + file + " (writable: " + file.canWrite() + ")");
                }
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        public abstract c[] i();

        public abstract void o(File file);
    }

    protected G(Context context, String str, boolean z3) {
        super(p(context, str), z3 ? 1 : 0);
        this.f8192d = context;
    }

    private void j() throws IOException {
        File[] fileArrListFiles = this.f8229a.listFiles(new a());
        if (fileArrListFiles == null) {
            throw new IOException("unable to list directory " + this.f8229a);
        }
        for (File file : fileArrListFiles) {
            p.f("fb-UnpackingSoSource", "Deleting " + file);
            SysUtil.c(file);
        }
    }

    private static boolean m(int i3) {
        return (i3 & 2) != 0;
    }

    public static File p(Context context, String str) {
        return new File(context.getApplicationInfo().dataDir + "/" + str);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0073  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean r(com.facebook.soloader.n r13, int r14) throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 259
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.G.r(com.facebook.soloader.n, int):boolean");
    }

    private static boolean s(int i3) {
        return (i3 & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void u(File file, byte b4, boolean z3) throws IOException {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            try {
                randomAccessFile.seek(0L);
                randomAccessFile.write(b4);
                randomAccessFile.setLength(randomAccessFile.getFilePointer());
                if (z3) {
                    randomAccessFile.getFD().sync();
                }
                randomAccessFile.close();
            } finally {
            }
        } catch (SyncFailedException e4) {
            p.h("fb-UnpackingSoSource", "state file sync failed", e4);
        }
    }

    @Override // com.facebook.soloader.InterfaceC0481b
    public void b() throws Throwable {
        try {
            n nVarI = SysUtil.i(this.f8229a, new File(this.f8229a, "dso_lock"));
            if (nVarI != null) {
                nVarI.close();
            }
        } catch (Exception e4) {
            p.c("fb-UnpackingSoSource", "Encountered exception during wait for unpacking trying to acquire file lock for " + getClass().getName() + " (" + this.f8229a + "): ", e4);
        }
    }

    @Override // com.facebook.soloader.E
    public void e(int i3) throws IOException {
        SysUtil.m(this.f8229a);
        if (!this.f8229a.canWrite() && !this.f8229a.setWritable(true)) {
            throw new IOException("error adding " + this.f8229a.getCanonicalPath() + " write permission");
        }
        n nVar = null;
        try {
            try {
                n nVarI = SysUtil.i(this.f8229a, new File(this.f8229a, "dso_lock"));
                try {
                    p.f("fb-UnpackingSoSource", "locked dso store " + this.f8229a);
                    if (!this.f8229a.canWrite() && !this.f8229a.setWritable(true)) {
                        throw new IOException("error adding " + this.f8229a.getCanonicalPath() + " write permission");
                    }
                    if (!r(nVarI, i3)) {
                        p.d("fb-UnpackingSoSource", "dso store is up-to-date: " + this.f8229a);
                        nVar = nVarI;
                    }
                    if (nVar != null) {
                        p.f("fb-UnpackingSoSource", "releasing dso store lock for " + this.f8229a);
                        nVar.close();
                    } else {
                        p.f("fb-UnpackingSoSource", "not releasing dso store lock for " + this.f8229a + " (syncer thread started)");
                    }
                    if (!this.f8229a.canWrite() || this.f8229a.setWritable(false)) {
                        return;
                    }
                    throw new IOException("error removing " + this.f8229a.getCanonicalPath() + " write permission");
                } catch (Throwable th) {
                    th = th;
                    nVar = nVarI;
                    if (nVar != null) {
                        p.f("fb-UnpackingSoSource", "releasing dso store lock for " + this.f8229a);
                        nVar.close();
                    } else {
                        p.f("fb-UnpackingSoSource", "not releasing dso store lock for " + this.f8229a + " (syncer thread started)");
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                if (!this.f8229a.canWrite() || this.f8229a.setWritable(false)) {
                    throw th2;
                }
                throw new IOException("error removing " + this.f8229a.getCanonicalPath() + " write permission");
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    protected boolean k(byte[] bArr) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(this.f8229a, "dso_deps"), "rw");
            try {
                if (randomAccessFile.length() == 0) {
                    randomAccessFile.close();
                    return true;
                }
                int length = (int) randomAccessFile.length();
                byte[] bArr2 = new byte[length];
                if (randomAccessFile.read(bArr2) != length) {
                    p.f("fb-UnpackingSoSource", "short read of so store deps file: marking unclean");
                    randomAccessFile.close();
                    return true;
                }
                boolean zL = l(bArr2, bArr);
                randomAccessFile.close();
                return zL;
            } finally {
            }
        } catch (IOException e4) {
            p.h("fb-UnpackingSoSource", "failed to compare whether deps changed", e4);
            return true;
        }
    }

    protected boolean l(byte[] bArr, byte[] bArr2) {
        return !Arrays.equals(bArr, bArr2);
    }

    protected byte[] n() {
        Parcel parcelObtain = Parcel.obtain();
        e eVarQ = q();
        try {
            c[] cVarArrI = eVarQ.i();
            parcelObtain.writeInt(cVarArrI.length);
            for (c cVar : cVarArrI) {
                parcelObtain.writeString(cVar.f8199b);
                parcelObtain.writeString(cVar.f8200c);
            }
            eVarQ.close();
            byte[] bArrMarshall = parcelObtain.marshall();
            parcelObtain.recycle();
            return bArrMarshall;
        } catch (Throwable th) {
            if (eVarQ != null) {
                try {
                    eVarQ.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public c[] o() {
        e eVarQ = q();
        try {
            c[] cVarArrI = eVarQ.i();
            eVarQ.close();
            return cVarArrI;
        } catch (Throwable th) {
            if (eVarQ != null) {
                try {
                    eVarQ.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    protected abstract e q();

    public void t(String[] strArr) {
        this.f8193e = strArr;
    }

    protected G(Context context, String str) {
        this(context, str, true);
    }

    protected G(Context context, File file, boolean z3) {
        super(file, z3 ? 1 : 0);
        this.f8192d = context;
    }

    protected G(Context context, File file) {
        this(context, file, true);
    }
}
