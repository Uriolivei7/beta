package com.facebook.imagepipeline.producers;

import android.net.Uri;
import android.util.Base64;
import java.io.ByteArrayInputStream;

/* JADX INFO: renamed from: com.facebook.imagepipeline.producers.p, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0356p extends M {
    public C0356p(a0.i iVar) {
        super(V.a.b(), iVar);
    }

    static byte[] g(String str) {
        X.k.b(Boolean.valueOf(str.substring(0, 5).equals("data:")));
        int iIndexOf = str.indexOf(44);
        String strSubstring = str.substring(iIndexOf + 1, str.length());
        if (h(str.substring(0, iIndexOf))) {
            return Base64.decode(strSubstring, 0);
        }
        String strDecode = Uri.decode(strSubstring);
        X.k.g(strDecode);
        return strDecode.getBytes();
    }

    static boolean h(String str) {
        if (!str.contains(";")) {
            return false;
        }
        return str.split(";")[r2.length - 1].equals("base64");
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected O0.j d(U0.b bVar) {
        byte[] bArrG = g(bVar.v().toString());
        return c(new ByteArrayInputStream(bArrG), bArrG.length);
    }

    @Override // com.facebook.imagepipeline.producers.M
    protected String f() {
        return "DataFetchProducer";
    }
}
