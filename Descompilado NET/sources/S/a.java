package S;

import R.a;
import S.f;
import W.c;
import android.os.Environment;
import e0.InterfaceC0522a;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class a implements S.f {

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static final Class f2208f = a.class;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    static final long f2209g = TimeUnit.MINUTES.toMillis(30);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final File f2210a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final boolean f2211b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final File f2212c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final R.a f2213d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final InterfaceC0522a f2214e;

    /* JADX INFO: renamed from: S.a$a, reason: collision with other inner class name */
    private class C0031a implements W.b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final List f2215a;

        @Override // W.b
        public void c(File file) {
            c cVarW = a.this.w(file);
            if (cVarW == null || cVarW.f2221a != ".cnt") {
                return;
            }
            this.f2215a.add(new b(cVarW.f2222b, file));
        }

        public List d() {
            return Collections.unmodifiableList(this.f2215a);
        }

        private C0031a() {
            this.f2215a = new ArrayList();
        }

        @Override // W.b
        public void a(File file) {
        }

        @Override // W.b
        public void b(File file) {
        }
    }

    static class b implements f.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f2217a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final Q.b f2218b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private long f2219c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private long f2220d;

        @Override // S.f.a
        public long a() {
            if (this.f2220d < 0) {
                this.f2220d = this.f2218b.d().lastModified();
            }
            return this.f2220d;
        }

        public Q.b b() {
            return this.f2218b;
        }

        @Override // S.f.a
        public String getId() {
            return this.f2217a;
        }

        @Override // S.f.a
        public long i() {
            if (this.f2219c < 0) {
                this.f2219c = this.f2218b.size();
            }
            return this.f2219c;
        }

        private b(String str, File file) {
            X.k.g(file);
            this.f2217a = (String) X.k.g(str);
            this.f2218b = Q.b.b(file);
            this.f2219c = -1L;
            this.f2220d = -1L;
        }
    }

    private static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f2221a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f2222b;

        public static c b(File file) {
            String strU;
            String name = file.getName();
            int iLastIndexOf = name.lastIndexOf(46);
            if (iLastIndexOf <= 0 || (strU = a.u(name.substring(iLastIndexOf))) == null) {
                return null;
            }
            String strSubstring = name.substring(0, iLastIndexOf);
            if (strU.equals(".tmp")) {
                int iLastIndexOf2 = strSubstring.lastIndexOf(46);
                if (iLastIndexOf2 <= 0) {
                    return null;
                }
                strSubstring = strSubstring.substring(0, iLastIndexOf2);
            }
            return new c(strU, strSubstring);
        }

        public File a(File file) {
            return File.createTempFile(this.f2222b + ".", ".tmp", file);
        }

        public String c(String str) {
            return str + File.separator + this.f2222b + this.f2221a;
        }

        public String toString() {
            return this.f2221a + "(" + this.f2222b + ")";
        }

        private c(String str, String str2) {
            this.f2221a = str;
            this.f2222b = str2;
        }
    }

    private static class d extends IOException {
        public d(long j3, long j4) {
            super("File was not written completely. Expected: " + j3 + ", found: " + j4);
        }
    }

    class e implements f.b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f2223a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final File f2224b;

        public e(String str, File file) {
            this.f2223a = str;
            this.f2224b = file;
        }

        @Override // S.f.b
        public boolean a() {
            return !this.f2224b.exists() || this.f2224b.delete();
        }

        @Override // S.f.b
        public void b(R.j jVar, Object obj) throws IOException {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.f2224b);
                try {
                    X.c cVar = new X.c(fileOutputStream);
                    jVar.a(cVar);
                    cVar.flush();
                    long jA = cVar.a();
                    fileOutputStream.close();
                    if (this.f2224b.length() != jA) {
                        throw new d(jA, this.f2224b.length());
                    }
                } catch (Throwable th) {
                    fileOutputStream.close();
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                a.this.f2213d.a(a.EnumC0028a.WRITE_UPDATE_FILE_NOT_FOUND, a.f2208f, "updateResource", e4);
                throw e4;
            }
        }

        @Override // S.f.b
        public Q.a c(Object obj) {
            return d(obj, a.this.f2214e.now());
        }

        public Q.a d(Object obj, long j3) throws c.d {
            File fileS = a.this.s(this.f2223a);
            try {
                W.c.b(this.f2224b, fileS);
                if (fileS.exists()) {
                    fileS.setLastModified(j3);
                }
                return Q.b.b(fileS);
            } catch (c.d e4) {
                Throwable cause = e4.getCause();
                a.this.f2213d.a(cause != null ? !(cause instanceof c.C0040c) ? cause instanceof FileNotFoundException ? a.EnumC0028a.WRITE_RENAME_FILE_TEMPFILE_NOT_FOUND : a.EnumC0028a.WRITE_RENAME_FILE_OTHER : a.EnumC0028a.WRITE_RENAME_FILE_TEMPFILE_PARENT_NOT_FOUND : a.EnumC0028a.WRITE_RENAME_FILE_OTHER, a.f2208f, "commit", e4);
                throw e4;
            }
        }
    }

    private class f implements W.b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private boolean f2226a;

        private boolean d(File file) {
            c cVarW = a.this.w(file);
            if (cVarW == null) {
                return false;
            }
            String str = cVarW.f2221a;
            if (str == ".tmp") {
                return e(file);
            }
            X.k.i(str == ".cnt");
            return true;
        }

        private boolean e(File file) {
            return file.lastModified() > a.this.f2214e.now() - a.f2209g;
        }

        @Override // W.b
        public void a(File file) {
            if (this.f2226a || !file.equals(a.this.f2212c)) {
                return;
            }
            this.f2226a = true;
        }

        @Override // W.b
        public void b(File file) {
            if (!a.this.f2210a.equals(file) && !this.f2226a) {
                file.delete();
            }
            if (this.f2226a && file.equals(a.this.f2212c)) {
                this.f2226a = false;
            }
        }

        @Override // W.b
        public void c(File file) {
            if (this.f2226a && d(file)) {
                return;
            }
            file.delete();
        }

        private f() {
        }
    }

    public a(File file, int i3, R.a aVar) {
        X.k.g(file);
        this.f2210a = file;
        this.f2211b = A(file, aVar);
        this.f2212c = new File(file, z(i3));
        this.f2213d = aVar;
        D();
        this.f2214e = e0.d.a();
    }

    private static boolean A(File file, R.a aVar) {
        String canonicalPath;
        try {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory == null) {
                return false;
            }
            String string = externalStorageDirectory.toString();
            try {
                canonicalPath = file.getCanonicalPath();
            } catch (IOException e4) {
                e = e4;
                canonicalPath = null;
            }
            try {
                return canonicalPath.contains(string);
            } catch (IOException e5) {
                e = e5;
                aVar.a(a.EnumC0028a.OTHER, f2208f, "failed to read folder to check if external: " + canonicalPath, e);
                return false;
            }
        } catch (Exception e6) {
            aVar.a(a.EnumC0028a.OTHER, f2208f, "failed to get the external storage directory!", e6);
            return false;
        }
    }

    private void B(File file, String str) throws c.a {
        try {
            W.c.a(file);
        } catch (c.a e4) {
            this.f2213d.a(a.EnumC0028a.WRITE_CREATE_DIR, f2208f, str, e4);
            throw e4;
        }
    }

    private boolean C(String str, boolean z3) {
        File fileS = s(str);
        boolean zExists = fileS.exists();
        if (z3 && zExists) {
            fileS.setLastModified(this.f2214e.now());
        }
        return zExists;
    }

    private void D() {
        if (this.f2210a.exists()) {
            if (this.f2212c.exists()) {
                return;
            } else {
                W.a.b(this.f2210a);
            }
        }
        try {
            W.c.a(this.f2212c);
        } catch (c.a unused) {
            this.f2213d.a(a.EnumC0028a.WRITE_CREATE_DIR, f2208f, "version directory could not be created: " + this.f2212c, null);
        }
    }

    private long r(File file) {
        if (!file.exists()) {
            return 0L;
        }
        long length = file.length();
        if (file.delete()) {
            return length;
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String u(String str) {
        if (".cnt".equals(str)) {
            return ".cnt";
        }
        if (".tmp".equals(str)) {
            return ".tmp";
        }
        return null;
    }

    private String v(String str) {
        c cVar = new c(".cnt", str);
        return cVar.c(y(cVar.f2222b));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public c w(File file) {
        c cVarB = c.b(file);
        if (cVarB != null && x(cVarB.f2222b).equals(file.getParentFile())) {
            return cVarB;
        }
        return null;
    }

    private File x(String str) {
        return new File(y(str));
    }

    private String y(String str) {
        return this.f2212c + File.separator + String.valueOf(Math.abs(str.hashCode() % 100));
    }

    static String z(int i3) {
        return String.format(null, "%s.ols%d.%d", "v2", 100, Integer.valueOf(i3));
    }

    @Override // S.f
    public void a() {
        W.a.a(this.f2210a);
    }

    @Override // S.f
    public boolean c() {
        return this.f2211b;
    }

    @Override // S.f
    public void d() {
        W.a.c(this.f2210a, new f());
    }

    @Override // S.f
    public long e(f.a aVar) {
        return r(((b) aVar).b().d());
    }

    @Override // S.f
    public f.b f(String str, Object obj) throws IOException {
        c cVar = new c(".tmp", str);
        File fileX = x(cVar.f2222b);
        if (!fileX.exists()) {
            B(fileX, "insert");
        }
        try {
            return new e(str, cVar.a(fileX));
        } catch (IOException e4) {
            this.f2213d.a(a.EnumC0028a.WRITE_CREATE_TEMPFILE, f2208f, "insert", e4);
            throw e4;
        }
    }

    @Override // S.f
    public boolean g(String str, Object obj) {
        return C(str, true);
    }

    @Override // S.f
    public long h(String str) {
        return r(s(str));
    }

    @Override // S.f
    public boolean i(String str, Object obj) {
        return C(str, false);
    }

    @Override // S.f
    public Q.a j(String str, Object obj) {
        File fileS = s(str);
        if (!fileS.exists()) {
            return null;
        }
        fileS.setLastModified(this.f2214e.now());
        return Q.b.c(fileS);
    }

    File s(String str) {
        return new File(v(str));
    }

    @Override // S.f
    /* JADX INFO: renamed from: t, reason: merged with bridge method [inline-methods] */
    public List b() {
        C0031a c0031a = new C0031a();
        W.a.c(this.f2212c, c0031a);
        return c0031a.d();
    }
}
