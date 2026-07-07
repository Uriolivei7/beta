package R0;

import android.util.SparseArray;
import java.util.LinkedList;

/* JADX INFO: loaded from: classes.dex */
public class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected final SparseArray f1967a = new SparseArray();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    a f1968b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    a f1969c;

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        a f1970a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        int f1971b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        LinkedList f1972c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        a f1973d;

        public String toString() {
            return "LinkedEntry(key: " + this.f1971b + ")";
        }

        private a(a aVar, int i3, LinkedList linkedList, a aVar2) {
            this.f1970a = aVar;
            this.f1971b = i3;
            this.f1972c = linkedList;
            this.f1973d = aVar2;
        }
    }

    private void b(a aVar) {
        if (aVar == null || !aVar.f1972c.isEmpty()) {
            return;
        }
        d(aVar);
        this.f1967a.remove(aVar.f1971b);
    }

    private void c(a aVar) {
        if (this.f1968b == aVar) {
            return;
        }
        d(aVar);
        a aVar2 = this.f1968b;
        if (aVar2 == null) {
            this.f1968b = aVar;
            this.f1969c = aVar;
        } else {
            aVar.f1973d = aVar2;
            aVar2.f1970a = aVar;
            this.f1968b = aVar;
        }
    }

    private synchronized void d(a aVar) {
        try {
            a aVar2 = aVar.f1970a;
            a aVar3 = aVar.f1973d;
            if (aVar2 != null) {
                aVar2.f1973d = aVar3;
            }
            if (aVar3 != null) {
                aVar3.f1970a = aVar2;
            }
            aVar.f1970a = null;
            aVar.f1973d = null;
            if (aVar == this.f1968b) {
                this.f1968b = aVar3;
            }
            if (aVar == this.f1969c) {
                this.f1969c = aVar2;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized Object a(int i3) {
        a aVar = (a) this.f1967a.get(i3);
        if (aVar == null) {
            return null;
        }
        Object objPollFirst = aVar.f1972c.pollFirst();
        c(aVar);
        return objPollFirst;
    }

    public synchronized void e(int i3, Object obj) {
        try {
            a aVar = (a) this.f1967a.get(i3);
            if (aVar == null) {
                aVar = new a(null, i3, new LinkedList(), null);
                this.f1967a.put(i3, aVar);
            }
            aVar.f1972c.addLast(obj);
            c(aVar);
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized Object f() {
        a aVar = this.f1969c;
        if (aVar == null) {
            return null;
        }
        Object objPollLast = aVar.f1972c.pollLast();
        b(aVar);
        return objPollLast;
    }
}
