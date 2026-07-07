package H1;

import H1.e;
import android.net.Uri;
import b3.l;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class b implements e.c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f347c = "b";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private e f348a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map f349b;

    private class a implements h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Object f350a;

        public a(Object obj) {
            this.f350a = obj;
        }

        @Override // H1.h
        public void a(Object obj) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("version", 2);
                jSONObject.put("id", this.f350a);
                jSONObject.put("result", obj);
                b.this.f348a.n(jSONObject.toString());
            } catch (Exception e4) {
                Y.a.n(b.f347c, "Responding failed", e4);
            }
        }

        @Override // H1.h
        public void b(Object obj) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("version", 2);
                jSONObject.put("id", this.f350a);
                jSONObject.put("error", obj);
                b.this.f348a.n(jSONObject.toString());
            } catch (Exception e4) {
                Y.a.n(b.f347c, "Responding with error failed", e4);
            }
        }
    }

    public b(String str, d dVar, Map<String, f> map) {
        this(str, dVar, map, null);
    }

    private void d(Object obj, String str) {
        if (obj != null) {
            new a(obj).b(str);
        }
        Y.a.m(f347c, "Handling the message failed with reason: " + str);
    }

    @Override // H1.e.c
    public void a(l lVar) {
        Y.a.I(f347c, "Websocket received message with payload of unexpected type binary");
    }

    public void e() {
        this.f348a.i();
    }

    public void f() {
        this.f348a.k();
    }

    @Override // H1.e.c
    public void onMessage(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            int iOptInt = jSONObject.optInt("version");
            String strOptString = jSONObject.optString("method");
            Object objOpt = jSONObject.opt("id");
            Object objOpt2 = jSONObject.opt("params");
            if (iOptInt != 2) {
                Y.a.m(f347c, "Message with incompatible or missing version of protocol received: " + iOptInt);
                return;
            }
            if (strOptString == null) {
                d(objOpt, "No method provided");
                return;
            }
            f fVar = (f) this.f349b.get(strOptString);
            if (fVar == null) {
                d(objOpt, "No request handler for method: " + strOptString);
                return;
            }
            if (objOpt == null) {
                fVar.b(objOpt2);
            } else {
                fVar.a(objOpt2, new a(objOpt));
            }
        } catch (Exception e4) {
            Y.a.n(f347c, "Handling the message failed", e4);
        }
    }

    public b(String str, d dVar, Map<String, f> map, e.b bVar) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("ws").encodedAuthority(dVar.b()).appendPath("message").appendQueryParameter("device", com.facebook.react.modules.systeminfo.a.d()).appendQueryParameter("app", dVar.c()).appendQueryParameter("clientid", str);
        this.f348a = new e(builder.build().toString(), this, bVar);
        this.f349b = map;
    }
}
