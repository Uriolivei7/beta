package H1;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a implements Runnable {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final String f337f = H1.b.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private int f338b = 1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Handler f339c = new Handler(Looper.getMainLooper());

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final Map f340d = new HashMap();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final Map f341e;

    /* JADX INFO: renamed from: H1.a$a, reason: collision with other inner class name */
    class C0005a extends g {
        C0005a() {
        }

        @Override // H1.f
        public void a(Object obj, h hVar) {
            JSONObject jSONObject;
            synchronized (a.this.f340d) {
                try {
                    try {
                        jSONObject = (JSONObject) obj;
                    } catch (Exception e4) {
                        hVar.b(e4.toString());
                    }
                    if (jSONObject == null) {
                        throw new Exception("params must be an object { mode: string, filename: string }");
                    }
                    String strOptString = jSONObject.optString("mode");
                    if (strOptString == null) {
                        throw new Exception("missing params.mode");
                    }
                    String strOptString2 = jSONObject.optString("filename");
                    if (strOptString2 == null) {
                        throw new Exception("missing params.filename");
                    }
                    if (!strOptString.equals("r")) {
                        throw new IllegalArgumentException("unsupported mode: " + strOptString);
                    }
                    hVar.a(Integer.valueOf(a.this.c(strOptString2)));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    class b extends g {
        b() {
        }

        @Override // H1.f
        public void a(Object obj, h hVar) {
            synchronized (a.this.f340d) {
                try {
                    try {
                    } catch (Exception e4) {
                        hVar.b(e4.toString());
                    }
                    if (!(obj instanceof Number)) {
                        throw new Exception("params must be a file handle");
                    }
                    d dVar = (d) a.this.f340d.get(obj);
                    if (dVar == null) {
                        throw new Exception("invalid file handle, it might have timed out");
                    }
                    a.this.f340d.remove(obj);
                    dVar.a();
                    hVar.a("");
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    class c extends g {
        c() {
        }

        @Override // H1.f
        public void a(Object obj, h hVar) {
            JSONObject jSONObject;
            synchronized (a.this.f340d) {
                try {
                    try {
                        jSONObject = (JSONObject) obj;
                    } catch (Exception e4) {
                        hVar.b(e4.toString());
                    }
                    if (jSONObject == null) {
                        throw new Exception("params must be an object { file: handle, size: number }");
                    }
                    int iOptInt = jSONObject.optInt("file");
                    if (iOptInt == 0) {
                        throw new Exception("invalid or missing file handle");
                    }
                    int iOptInt2 = jSONObject.optInt("size");
                    if (iOptInt2 == 0) {
                        throw new Exception("invalid or missing read size");
                    }
                    d dVar = (d) a.this.f340d.get(Integer.valueOf(iOptInt));
                    if (dVar == null) {
                        throw new Exception("invalid file handle, it might have timed out");
                    }
                    hVar.a(dVar.d(iOptInt2));
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final FileInputStream f345a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private long f346b = System.currentTimeMillis() + 30000;

        public d(String str) {
            this.f345a = new FileInputStream(str);
        }

        private void c() {
            this.f346b = System.currentTimeMillis() + 30000;
        }

        public void a() throws IOException {
            this.f345a.close();
        }

        public boolean b() {
            return System.currentTimeMillis() >= this.f346b;
        }

        public String d(int i3) {
            c();
            byte[] bArr = new byte[i3];
            return Base64.encodeToString(bArr, 0, this.f345a.read(bArr), 0);
        }
    }

    public a() {
        HashMap map = new HashMap();
        this.f341e = map;
        map.put("fopen", new C0005a());
        map.put("fclose", new b());
        map.put("fread", new c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int c(String str) {
        int i3 = this.f338b;
        this.f338b = i3 + 1;
        this.f340d.put(Integer.valueOf(i3), new d(str));
        if (this.f340d.size() == 1) {
            this.f339c.postDelayed(this, 30000L);
        }
        return i3;
    }

    public Map d() {
        return this.f341e;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.f340d) {
            Iterator it = this.f340d.values().iterator();
            while (it.hasNext()) {
                d dVar = (d) it.next();
                if (dVar.b()) {
                    it.remove();
                    try {
                        dVar.a();
                    } catch (IOException e4) {
                        Y.a.m(f337f, "closing expired file failed: " + e4.toString());
                    }
                }
            }
            if (!this.f340d.isEmpty()) {
                this.f339c.postDelayed(this, 30000L);
            }
        }
    }
}
