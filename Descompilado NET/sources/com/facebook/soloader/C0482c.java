package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import android.os.StrictMode;
import com.facebook.soloader.G;
import com.facebook.soloader.m;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/* JADX INFO: renamed from: com.facebook.soloader.c, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0482c extends G implements w {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final ArrayList f8220f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    protected boolean f8221g;

    /* JADX INFO: renamed from: com.facebook.soloader.c$a */
    protected class a extends G.e {
        protected a() {
        }

        @Override // com.facebook.soloader.G.e
        public G.c[] i() {
            ArrayList arrayList = new ArrayList();
            Iterator it = C0482c.this.f8220f.iterator();
            while (it.hasNext()) {
                G.e eVarQ = ((m) it.next()).q();
                try {
                    arrayList.addAll(Arrays.asList(eVarQ.i()));
                    eVarQ.close();
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
            return (G.c[]) arrayList.toArray(new G.c[arrayList.size()]);
        }

        @Override // com.facebook.soloader.G.e
        public void o(File file) throws IOException {
            Iterator it = C0482c.this.f8220f.iterator();
            while (it.hasNext()) {
                m.b bVar = (m.b) ((m) it.next()).q();
                try {
                    bVar.o(file);
                    bVar.close();
                } catch (Throwable th) {
                    if (bVar != null) {
                        try {
                            bVar.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
        }
    }

    public C0482c(Context context, String str, boolean z3) {
        super(context, str, z3);
        ArrayList arrayList = new ArrayList();
        this.f8220f = arrayList;
        this.f8221g = false;
        arrayList.add(new m(context, str, new File(context.getApplicationInfo().sourceDir), "^lib/([^/]+)/([^/]+\\.so)$"));
        w(context, str);
    }

    private void w(Context context, String str) {
        if (context.getApplicationInfo().splitSourceDirs == null) {
            return;
        }
        try {
            for (String str2 : context.getApplicationInfo().splitSourceDirs) {
                m mVar = new m(context, str, new File(str2), "^lib/([^/]+)/([^/]+\\.so)$");
                if (mVar.v()) {
                    p.g("BackupSoSource", "adding backup source from split: " + mVar.toString());
                    this.f8220f.add(mVar);
                }
            }
        } catch (IOException e4) {
            p.h("BackupSoSource", "failed to read split apks", e4);
        }
    }

    @Override // com.facebook.soloader.w
    public E a(Context context) {
        C0482c c0482c = new C0482c(context, this.f8229a.getName());
        try {
            c0482c.e(0);
            return c0482c;
        } catch (IOException e4) {
            throw new RuntimeException(e4);
        }
    }

    @Override // com.facebook.soloader.C0485f, com.facebook.soloader.E
    public String c() {
        return "BackupSoSource";
    }

    @Override // com.facebook.soloader.C0485f, com.facebook.soloader.E
    public int d(String str, int i3, StrictMode.ThreadPolicy threadPolicy) {
        if (this.f8221g) {
            return super.d(str, i3, threadPolicy);
        }
        return 0;
    }

    @Override // com.facebook.soloader.G, com.facebook.soloader.E
    public void e(int i3) throws IOException {
        if ((i3 & 8) != 0) {
            return;
        }
        super.e(i3);
        this.f8221g = true;
    }

    @Override // com.facebook.soloader.G
    protected byte[] n() {
        Parcel parcelObtain = Parcel.obtain();
        try {
            parcelObtain.writeByte((byte) 3);
            parcelObtain.writeInt(SysUtil.g(this.f8192d));
            parcelObtain.writeInt(this.f8220f.size());
            Iterator it = this.f8220f.iterator();
            while (it.hasNext()) {
                parcelObtain.writeByteArray(((m) it.next()).n());
            }
            String str = this.f8192d.getApplicationInfo().sourceDir;
            if (str == null) {
                parcelObtain.writeByte((byte) 1);
                byte[] bArrMarshall = parcelObtain.marshall();
                parcelObtain.recycle();
                return bArrMarshall;
            }
            File canonicalFile = new File(str).getCanonicalFile();
            if (!canonicalFile.exists()) {
                parcelObtain.writeByte((byte) 1);
                byte[] bArrMarshall2 = parcelObtain.marshall();
                parcelObtain.recycle();
                return bArrMarshall2;
            }
            parcelObtain.writeByte((byte) 2);
            parcelObtain.writeString(canonicalFile.getPath());
            parcelObtain.writeLong(canonicalFile.lastModified());
            byte[] bArrMarshall3 = parcelObtain.marshall();
            parcelObtain.recycle();
            return bArrMarshall3;
        } catch (Throwable th) {
            parcelObtain.recycle();
            throw th;
        }
    }

    @Override // com.facebook.soloader.G
    public G.c[] o() {
        G.e eVarQ = ((m) this.f8220f.get(0)).q();
        try {
            G.c[] cVarArrI = eVarQ.i();
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

    @Override // com.facebook.soloader.G
    protected G.e q() {
        return new a();
    }

    @Override // com.facebook.soloader.C0485f, com.facebook.soloader.E
    public String toString() {
        String name;
        try {
            name = String.valueOf(this.f8229a.getCanonicalPath());
        } catch (IOException unused) {
            name = this.f8229a.getName();
        }
        return c() + "[root = " + name + " flags = " + this.f8230b + " apks = " + this.f8220f.toString() + "]";
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003a, code lost:
    
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001a, code lost:
    
        com.facebook.soloader.p.b("SoLoader", "Found " + r9 + " in " + c());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean x(java.lang.String r9, int r10) throws java.io.IOException {
        /*
            r8 = this;
            com.facebook.soloader.G$e r0 = r8.q()
            com.facebook.soloader.G$c[] r1 = r0.i()     // Catch: java.lang.Throwable -> L3c
            int r2 = r1.length     // Catch: java.lang.Throwable -> L3c
            r3 = 0
            r4 = r3
        Lb:
            java.lang.String r5 = "SoLoader"
            r6 = 1
            if (r4 >= r2) goto L41
            r7 = r1[r4]     // Catch: java.lang.Throwable -> L3c
            java.lang.String r7 = r7.f8199b     // Catch: java.lang.Throwable -> L3c
            boolean r7 = r7.equals(r9)     // Catch: java.lang.Throwable -> L3c
            if (r7 == 0) goto L3e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L3c
            r1.<init>()     // Catch: java.lang.Throwable -> L3c
            java.lang.String r2 = "Found "
            r1.append(r2)     // Catch: java.lang.Throwable -> L3c
            r1.append(r9)     // Catch: java.lang.Throwable -> L3c
            java.lang.String r9 = " in "
            r1.append(r9)     // Catch: java.lang.Throwable -> L3c
            java.lang.String r9 = r8.c()     // Catch: java.lang.Throwable -> L3c
            r1.append(r9)     // Catch: java.lang.Throwable -> L3c
            java.lang.String r9 = r1.toString()     // Catch: java.lang.Throwable -> L3c
            com.facebook.soloader.p.b(r5, r9)     // Catch: java.lang.Throwable -> L3c
            r9 = r6
            goto L42
        L3c:
            r9 = move-exception
            goto L64
        L3e:
            int r4 = r4 + 1
            goto Lb
        L41:
            r9 = r3
        L42:
            r0.close()
            if (r9 != 0) goto L48
            return r3
        L48:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r0 = "Preparing "
            r9.append(r0)
            java.lang.String r0 = r8.c()
            r9.append(r0)
            java.lang.String r9 = r9.toString()
            com.facebook.soloader.p.b(r5, r9)
            r8.e(r10)
            return r6
        L64:
            if (r0 == 0) goto L6e
            r0.close()     // Catch: java.lang.Throwable -> L6a
            goto L6e
        L6a:
            r10 = move-exception
            r9.addSuppressed(r10)
        L6e:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.C0482c.x(java.lang.String, int):boolean");
    }

    public C0482c(Context context, String str) {
        this(context, str, true);
    }
}
