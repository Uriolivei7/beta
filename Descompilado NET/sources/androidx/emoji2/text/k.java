package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import androidx.emoji2.text.f;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import p.g;

/* JADX INFO: loaded from: classes.dex */
public class k extends f.c {

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private static final a f4813k = new a();

    public static class a {
        public Typeface a(Context context, g.b bVar) {
            return p.g.a(context, null, new g.b[]{bVar});
        }

        public g.a b(Context context, p.e eVar) {
            return p.g.b(context, null, eVar);
        }

        public void c(Context context, Uri uri, ContentObserver contentObserver) {
            context.getContentResolver().registerContentObserver(uri, false, contentObserver);
        }

        public void d(Context context, ContentObserver contentObserver) {
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class b implements f.h {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final Context f4814a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final p.e f4815b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private final a f4816c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final Object f4817d = new Object();

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private Handler f4818e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        private Executor f4819f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        private ThreadPoolExecutor f4820g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private c f4821h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        f.i f4822i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        private ContentObserver f4823j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private Runnable f4824k;

        class a extends ContentObserver {
            a(Handler handler) {
                super(handler);
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z3, Uri uri) {
                b.this.d();
            }
        }

        b(Context context, p.e eVar, a aVar) {
            q.g.h(context, "Context cannot be null");
            q.g.h(eVar, "FontRequest cannot be null");
            this.f4814a = context.getApplicationContext();
            this.f4815b = eVar;
            this.f4816c = aVar;
        }

        private void b() {
            synchronized (this.f4817d) {
                try {
                    this.f4822i = null;
                    ContentObserver contentObserver = this.f4823j;
                    if (contentObserver != null) {
                        this.f4816c.d(this.f4814a, contentObserver);
                        this.f4823j = null;
                    }
                    Handler handler = this.f4818e;
                    if (handler != null) {
                        handler.removeCallbacks(this.f4824k);
                    }
                    this.f4818e = null;
                    ThreadPoolExecutor threadPoolExecutor = this.f4820g;
                    if (threadPoolExecutor != null) {
                        threadPoolExecutor.shutdown();
                    }
                    this.f4819f = null;
                    this.f4820g = null;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        private g.b e() {
            try {
                g.a aVarB = this.f4816c.b(this.f4814a, this.f4815b);
                if (aVarB.c() == 0) {
                    g.b[] bVarArrB = aVarB.b();
                    if (bVarArrB == null || bVarArrB.length == 0) {
                        throw new RuntimeException("fetchFonts failed (empty result)");
                    }
                    return bVarArrB[0];
                }
                throw new RuntimeException("fetchFonts failed (" + aVarB.c() + ")");
            } catch (PackageManager.NameNotFoundException e4) {
                throw new RuntimeException("provider not found", e4);
            }
        }

        private void f(Uri uri, long j3) {
            synchronized (this.f4817d) {
                try {
                    Handler handlerD = this.f4818e;
                    if (handlerD == null) {
                        handlerD = androidx.emoji2.text.c.d();
                        this.f4818e = handlerD;
                    }
                    if (this.f4823j == null) {
                        a aVar = new a(handlerD);
                        this.f4823j = aVar;
                        this.f4816c.c(this.f4814a, uri, aVar);
                    }
                    if (this.f4824k == null) {
                        this.f4824k = new Runnable() { // from class: androidx.emoji2.text.m
                            @Override // java.lang.Runnable
                            public final void run() {
                                this.f4827b.d();
                            }
                        };
                    }
                    handlerD.postDelayed(this.f4824k, j3);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // androidx.emoji2.text.f.h
        public void a(f.i iVar) {
            q.g.h(iVar, "LoaderCallback cannot be null");
            synchronized (this.f4817d) {
                this.f4822i = iVar;
            }
            d();
        }

        void c() {
            synchronized (this.f4817d) {
                try {
                    if (this.f4822i == null) {
                        return;
                    }
                    try {
                        g.b bVarE = e();
                        int iB = bVarE.b();
                        if (iB == 2) {
                            synchronized (this.f4817d) {
                                try {
                                    c cVar = this.f4821h;
                                    if (cVar != null) {
                                        long jA = cVar.a();
                                        if (jA >= 0) {
                                            f(bVarE.d(), jA);
                                            return;
                                        }
                                    }
                                } finally {
                                }
                            }
                        }
                        if (iB != 0) {
                            throw new RuntimeException("fetchFonts result is not OK. (" + iB + ")");
                        }
                        try {
                            androidx.core.os.h.a("EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface");
                            Typeface typefaceA = this.f4816c.a(this.f4814a, bVarE);
                            ByteBuffer byteBufferF = androidx.core.graphics.k.f(this.f4814a, null, bVarE.d());
                            if (byteBufferF == null || typefaceA == null) {
                                throw new RuntimeException("Unable to open file.");
                            }
                            o oVarB = o.b(typefaceA, byteBufferF);
                            androidx.core.os.h.b();
                            synchronized (this.f4817d) {
                                try {
                                    f.i iVar = this.f4822i;
                                    if (iVar != null) {
                                        iVar.b(oVarB);
                                    }
                                } finally {
                                }
                            }
                            b();
                        } catch (Throwable th) {
                            androidx.core.os.h.b();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        synchronized (this.f4817d) {
                            try {
                                f.i iVar2 = this.f4822i;
                                if (iVar2 != null) {
                                    iVar2.a(th2);
                                }
                                b();
                            } finally {
                            }
                        }
                    }
                } finally {
                }
            }
        }

        void d() {
            synchronized (this.f4817d) {
                try {
                    if (this.f4822i == null) {
                        return;
                    }
                    if (this.f4819f == null) {
                        ThreadPoolExecutor threadPoolExecutorB = androidx.emoji2.text.c.b("emojiCompat");
                        this.f4820g = threadPoolExecutorB;
                        this.f4819f = threadPoolExecutorB;
                    }
                    this.f4819f.execute(new Runnable() { // from class: androidx.emoji2.text.l
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f4826b.c();
                        }
                    });
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void g(Executor executor) {
            synchronized (this.f4817d) {
                this.f4819f = executor;
            }
        }
    }

    public static abstract class c {
        public abstract long a();
    }

    public k(Context context, p.e eVar) {
        super(new b(context, eVar, f4813k));
    }

    public k c(Executor executor) {
        ((b) a()).g(executor);
        return this;
    }

    public k(Context context, p.e eVar, a aVar) {
        super(new b(context, eVar, aVar));
    }
}
