package com.facebook.react.devsupport;

import M2.B;
import M2.InterfaceC0194e;
import M2.InterfaceC0195f;
import a1.C0210a;
import com.facebook.react.devsupport.V;
import e1.C0526c;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import k1.InterfaceC0584b;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: renamed from: com.facebook.react.devsupport.b, reason: case insensitive filesystem */
/* JADX INFO: loaded from: classes.dex */
public class C0369b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final M2.z f6673a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private InterfaceC0194e f6674b;

    /* JADX INFO: renamed from: com.facebook.react.devsupport.b$a */
    class a implements InterfaceC0195f {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ InterfaceC0584b f6675a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ File f6676b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ c f6677c;

        a(InterfaceC0584b interfaceC0584b, File file, c cVar) {
            this.f6675a = interfaceC0584b;
            this.f6676b = file;
            this.f6677c = cVar;
        }

        @Override // M2.InterfaceC0195f
        public void a(InterfaceC0194e interfaceC0194e, IOException iOException) {
            if (C0369b.this.f6674b == null || C0369b.this.f6674b.q()) {
                C0369b.this.f6674b = null;
                return;
            }
            C0369b.this.f6674b = null;
            String string = interfaceC0194e.i().l().toString();
            this.f6675a.c(C0526c.b(string, "Could not connect to development server.", "URL: " + string, iOException));
        }

        @Override // M2.InterfaceC0195f
        public void b(InterfaceC0194e interfaceC0194e, M2.D d4) {
            try {
                if (C0369b.this.f6674b != null && !C0369b.this.f6674b.q()) {
                    C0369b.this.f6674b = null;
                    String string = d4.y0().l().toString();
                    Matcher matcher = Pattern.compile("multipart/mixed;.*boundary=\"([^\"]+)\"").matcher(d4.X("content-type"));
                    if (matcher.find()) {
                        C0369b.this.i(string, d4, matcher.group(1), this.f6676b, this.f6677c, this.f6675a);
                    } else {
                        M2.E eQ = d4.q();
                        try {
                            C0369b.this.h(string, d4.A(), d4.d0(), d4.q().z(), this.f6676b, this.f6677c, this.f6675a);
                            if (eQ != null) {
                                eQ.close();
                            }
                        } finally {
                        }
                    }
                    d4.close();
                    return;
                }
                C0369b.this.f6674b = null;
                if (d4 != null) {
                    d4.close();
                }
            } catch (Throwable th) {
                if (d4 != null) {
                    try {
                        d4.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.b$b, reason: collision with other inner class name */
    class C0101b implements V.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ M2.D f6679a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f6680b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final /* synthetic */ File f6681c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ c f6682d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ InterfaceC0584b f6683e;

        C0101b(M2.D d4, String str, File file, c cVar, InterfaceC0584b interfaceC0584b) {
            this.f6679a = d4;
            this.f6680b = str;
            this.f6681c = file;
            this.f6682d = cVar;
            this.f6683e = interfaceC0584b;
        }

        @Override // com.facebook.react.devsupport.V.a
        public void a(Map map, long j3, long j4) {
            if ("application/javascript".equals(map.get("Content-Type"))) {
                this.f6683e.b("Downloading", Integer.valueOf((int) (j3 / 1024)), Integer.valueOf((int) (j4 / 1024)));
            }
        }

        @Override // com.facebook.react.devsupport.V.a
        public void b(Map map, b3.i iVar, boolean z3) throws IOException {
            if (z3) {
                int iA = this.f6679a.A();
                if (map.containsKey("X-Http-Status")) {
                    iA = Integer.parseInt((String) map.get("X-Http-Status"));
                }
                C0369b.this.h(this.f6680b, iA, M2.t.f(map), iVar, this.f6681c, this.f6682d, this.f6683e);
                return;
            }
            if (map.containsKey("Content-Type") && ((String) map.get("Content-Type")).equals("application/json")) {
                try {
                    JSONObject jSONObject = new JSONObject(iVar.O());
                    this.f6683e.b(jSONObject.has("status") ? jSONObject.getString("status") : "Bundling", jSONObject.has("done") ? Integer.valueOf(jSONObject.getInt("done")) : null, jSONObject.has("total") ? Integer.valueOf(jSONObject.getInt("total")) : null);
                } catch (JSONException e4) {
                    Y.a.m("ReactNative", "Error parsing progress JSON. " + e4.toString());
                }
            }
        }
    }

    /* JADX INFO: renamed from: com.facebook.react.devsupport.b$c */
    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f6685a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f6686b;

        public String c() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("url", this.f6685a);
                jSONObject.put("filesChangedCount", this.f6686b);
                return jSONObject.toString();
            } catch (JSONException e4) {
                Y.a.n("BundleDownloader", "Can't serialize bundle info: ", e4);
                return null;
            }
        }
    }

    public C0369b(M2.z zVar) {
        this.f6673a = zVar;
    }

    private static void g(String str, M2.t tVar, c cVar) {
        cVar.f6685a = str;
        String strA = tVar.a("X-Metro-Files-Changed-Count");
        if (strA != null) {
            try {
                cVar.f6686b = Integer.parseInt(strA);
            } catch (NumberFormatException unused) {
                cVar.f6686b = -2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(String str, int i3, M2.t tVar, b3.k kVar, File file, c cVar, InterfaceC0584b interfaceC0584b) throws IOException {
        if (i3 != 200) {
            String strO = kVar.O();
            C0526c c0526cD = C0526c.d(str, strO);
            if (c0526cD != null) {
                interfaceC0584b.c(c0526cD);
                return;
            }
            interfaceC0584b.c(new C0526c("The development server returned response error code: " + i3 + "\n\nURL: " + str + "\n\nBody:\n" + strO));
            return;
        }
        if (cVar != null) {
            g(str, tVar, cVar);
        }
        File file2 = new File(file.getPath() + ".tmp");
        if (!j(kVar, file2) || file2.renameTo(file)) {
            interfaceC0584b.a();
            return;
        }
        throw new IOException("Couldn't rename " + file2 + " to " + file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i(String str, M2.D d4, String str2, File file, c cVar, InterfaceC0584b interfaceC0584b) {
        if (new V(d4.q().z(), str2).d(new C0101b(d4, str, file, cVar, interfaceC0584b))) {
            return;
        }
        interfaceC0584b.c(new C0526c("Error while reading multipart response.\n\nResponse code: " + d4.A() + "\n\nURL: " + str.toString() + "\n\n"));
    }

    private static boolean j(b3.k kVar, File file) throws Throwable {
        b3.D dF;
        try {
            dF = b3.t.f(file);
        } catch (Throwable th) {
            th = th;
            dF = null;
        }
        try {
            kVar.S(dF);
            if (dF == null) {
                return true;
            }
            dF.close();
            return true;
        } catch (Throwable th2) {
            th = th2;
            if (dF != null) {
                dF.close();
            }
            throw th;
        }
    }

    public void e(InterfaceC0584b interfaceC0584b, File file, String str, c cVar) {
        f(interfaceC0584b, file, str, cVar, new B.a());
    }

    public void f(InterfaceC0584b interfaceC0584b, File file, String str, c cVar, B.a aVar) {
        InterfaceC0194e interfaceC0194e = (InterfaceC0194e) C0210a.c(this.f6673a.b(aVar.m(str).a("Accept", "multipart/mixed").b()));
        this.f6674b = interfaceC0194e;
        interfaceC0194e.o(new a(interfaceC0584b, file, cVar));
    }
}
