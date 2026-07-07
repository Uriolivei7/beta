package n1;

import android.os.SystemClock;
import com.facebook.react.bridge.ReactNoCrashSoftException;
import com.facebook.react.bridge.ReactSoftExceptionLogger;
import com.facebook.react.bridge.RetryableMountingLayerException;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.fabric.FabricUIManager;
import com.facebook.react.fabric.mounting.mountitems.MountItem;
import d2.C0518a;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import r1.C0670b;

/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final d f9852a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final a f9853b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ConcurrentLinkedQueue f9854c = new ConcurrentLinkedQueue();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ConcurrentLinkedQueue f9855d = new ConcurrentLinkedQueue();

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final ConcurrentLinkedQueue f9856e = new ConcurrentLinkedQueue();

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f9857f = false;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private long f9858g = 0;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private long f9859h = 0;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private long f9860i = 0;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f9861j = false;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private final Runnable f9862k = new Runnable() { // from class: n1.b
        @Override // java.lang.Runnable
        public final void run() {
            this.f9851b.p();
        }
    };

    public interface a {
        void a(List list);

        void b(List list);

        void c();
    }

    public c(d dVar, a aVar) {
        this.f9852a = dVar;
        this.f9853b = aVar;
    }

    private void e() {
        boolean zIsIgnorable;
        this.f9858g = 0L;
        this.f9859h = SystemClock.uptimeMillis();
        List<com.facebook.react.fabric.mounting.mountitems.c> listM = m();
        List<MountItem> listK = k();
        if (listK == null && listM == null) {
            return;
        }
        this.f9853b.b(listK);
        if (listM != null) {
            C0518a.c(0L, "MountItemDispatcher::mountViews viewCommandMountItems");
            for (com.facebook.react.fabric.mounting.mountitems.c cVar : listM) {
                if (C0670b.e()) {
                    q(cVar, "dispatchMountItems: Executing viewCommandMountItem");
                }
                try {
                    j(cVar);
                } catch (RetryableMountingLayerException e4) {
                    if (cVar.b() == 0) {
                        cVar.c();
                        d(cVar);
                    } else {
                        ReactSoftExceptionLogger.logSoftException("MountItemDispatcher", new ReactNoCrashSoftException("Caught exception executing ViewCommand: " + cVar.toString(), e4));
                    }
                } catch (Throwable th) {
                    ReactSoftExceptionLogger.logSoftException("MountItemDispatcher", new RuntimeException("Caught exception executing ViewCommand: " + cVar.toString(), th));
                }
            }
            C0518a.i(0L);
        }
        List<MountItem> listL = l();
        if (listL != null) {
            C0518a.c(0L, "MountItemDispatcher::mountViews preMountItems");
            for (MountItem mountItem : listL) {
                if (C0670b.e()) {
                    q(mountItem, "dispatchMountItems: Executing preMountItem");
                }
                j(mountItem);
            }
            C0518a.i(0L);
        }
        if (listK != null) {
            C0518a.c(0L, "MountItemDispatcher::mountViews mountItems to execute");
            long jUptimeMillis = SystemClock.uptimeMillis();
            for (MountItem mountItem2 : listK) {
                if (C0670b.e()) {
                    q(mountItem2, "dispatchMountItems: Executing mountItem");
                }
                try {
                    j(mountItem2);
                } finally {
                    if (zIsIgnorable) {
                    }
                }
            }
            this.f9858g += SystemClock.uptimeMillis() - jUptimeMillis;
            C0518a.i(0L);
        }
        this.f9853b.a(listK);
    }

    /* JADX WARN: Finally extract failed */
    private void h(long j3) {
        MountItem mountItem;
        C0518a.c(0L, "MountItemDispatcher::premountViews");
        this.f9857f = true;
        while (System.nanoTime() <= j3 && (mountItem = (MountItem) this.f9856e.poll()) != null) {
            try {
                if (C0670b.e()) {
                    q(mountItem, "dispatchPreMountItems");
                }
                j(mountItem);
            } catch (Throwable th) {
                this.f9857f = false;
                throw th;
            }
        }
        this.f9857f = false;
        C0518a.i(0L);
    }

    private static List i(ConcurrentLinkedQueue concurrentLinkedQueue) {
        if (concurrentLinkedQueue.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        do {
            Object objPoll = concurrentLinkedQueue.poll();
            if (objPoll != null) {
                arrayList.add(objPoll);
            }
        } while (!concurrentLinkedQueue.isEmpty());
        if (arrayList.size() == 0) {
            return null;
        }
        return arrayList;
    }

    private void j(MountItem mountItem) {
        if (!this.f9852a.l(mountItem.getSurfaceId())) {
            mountItem.execute(this.f9852a);
            return;
        }
        if (C0670b.e()) {
            Y.a.o("MountItemDispatcher", "executeOrEnqueue: Item execution delayed, surface %s is not ready yet", Integer.valueOf(mountItem.getSurfaceId()));
        }
        this.f9852a.f(mountItem.getSurfaceId()).F(mountItem);
    }

    private List k() {
        return i(this.f9855d);
    }

    private List l() {
        return i(this.f9856e);
    }

    private List m() {
        return i(this.f9854c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p() {
        this.f9861j = false;
        if (this.f9856e.isEmpty()) {
            return;
        }
        h(this.f9860i + 8333333);
    }

    private static void q(MountItem mountItem, String str) {
        for (String str2 : mountItem.toString().split("\n")) {
            Y.a.m("MountItemDispatcher", str + ": " + str2);
        }
    }

    public void b(MountItem mountItem) {
        this.f9855d.add(mountItem);
    }

    public void c(MountItem mountItem) {
        if (!this.f9852a.t(mountItem.getSurfaceId())) {
            this.f9856e.add(mountItem);
        } else if (FabricUIManager.IS_DEVELOPMENT_ENVIRONMENT) {
            Y.a.o("MountItemDispatcher", "Not queueing PreAllocateMountItem: surfaceId stopped: [%d] - %s", Integer.valueOf(mountItem.getSurfaceId()), mountItem.toString());
        }
    }

    public void d(com.facebook.react.fabric.mounting.mountitems.c cVar) {
        this.f9854c.add(cVar);
    }

    public void f(Queue queue) {
        while (!queue.isEmpty()) {
            MountItem mountItem = (MountItem) queue.poll();
            try {
                mountItem.execute(this.f9852a);
            } catch (RetryableMountingLayerException e4) {
                if (mountItem instanceof com.facebook.react.fabric.mounting.mountitems.c) {
                    com.facebook.react.fabric.mounting.mountitems.c cVar = (com.facebook.react.fabric.mounting.mountitems.c) mountItem;
                    if (cVar.b() == 0) {
                        cVar.c();
                        d(cVar);
                    }
                } else {
                    q(mountItem, "dispatchExternalMountItems: mounting failed with " + e4.getMessage());
                }
            }
        }
    }

    public void g(long j3) {
        this.f9860i = j3;
        if (this.f9856e.isEmpty()) {
            return;
        }
        if (!C0670b.i()) {
            h(this.f9860i + 8333333);
        } else {
            if (this.f9861j) {
                return;
            }
            this.f9861j = true;
            UiThreadUtil.getUiThreadHandler().post(this.f9862k);
        }
    }

    public long n() {
        return this.f9858g;
    }

    public long o() {
        return this.f9859h;
    }

    public void r() {
        if (this.f9857f) {
            return;
        }
        this.f9857f = true;
        try {
            e();
            this.f9857f = false;
            this.f9853b.c();
        } catch (Throwable th) {
            this.f9857f = false;
            throw th;
        }
    }
}
