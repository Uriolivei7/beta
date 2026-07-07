package com.facebook.soloader;

import android.content.Context;
import com.facebook.soloader.G;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class k extends G {

    private final class a extends G.e {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final b[] f8241b;

        /* JADX WARN: Code restructure failed: missing block: B:29:0x00f7, code lost:
        
            throw new java.lang.RuntimeException("illegal line in exopackage metadata: [" + r12 + "]");
         */
        /* JADX WARN: Removed duplicated region for block: B:6:0x004c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        a(com.facebook.soloader.G r20) throws java.io.IOException {
            /*
                Method dump skipped, instruction units count: 318
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.k.a.<init>(com.facebook.soloader.k, com.facebook.soloader.G):void");
        }

        @Override // com.facebook.soloader.G.e
        public G.c[] i() {
            return this.f8241b;
        }

        @Override // com.facebook.soloader.G.e
        public void o(File file) throws IOException {
            byte[] bArr = new byte[32768];
            for (b bVar : this.f8241b) {
                FileInputStream fileInputStream = new FileInputStream(bVar.f8243d);
                try {
                    G.d dVar = new G.d(bVar, fileInputStream);
                    fileInputStream = null;
                    try {
                        a(dVar, bArr, file);
                        dVar.close();
                    } finally {
                    }
                } catch (Throwable th) {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            }
        }
    }

    private static final class b extends G.c {

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final File f8243d;

        b(String str, String str2, File file) {
            super(str, str2);
            this.f8243d = file;
        }
    }

    public k(Context context, String str) {
        super(context, str);
    }

    @Override // com.facebook.soloader.C0485f, com.facebook.soloader.E
    public String c() {
        return "ExoSoSource";
    }

    @Override // com.facebook.soloader.G
    protected G.e q() {
        return new a(this);
    }
}
