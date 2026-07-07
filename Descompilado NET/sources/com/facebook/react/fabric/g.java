package com.facebook.react.fabric;

import java.util.PriorityQueue;
import java.util.Queue;
import u2.AbstractC0746a;

/* JADX INFO: loaded from: classes.dex */
public final class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Queue f6830a = new PriorityQueue(11);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Queue f6831b = new PriorityQueue(11, AbstractC0746a.c());

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private double f6832c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f6833d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private long f6834e;

    public final void a(long j3) {
        if (j3 != 0) {
            if (this.f6830a.size() == this.f6831b.size()) {
                this.f6831b.offer(Long.valueOf(j3));
                this.f6830a.offer(this.f6831b.poll());
            } else {
                this.f6830a.offer(Long.valueOf(j3));
                this.f6831b.offer(this.f6830a.poll());
            }
        }
        int i3 = this.f6833d;
        int i4 = i3 + 1;
        this.f6833d = i4;
        if (i4 == 1) {
            this.f6832c = j3;
        } else {
            this.f6832c = (this.f6832c / ((double) (i4 / i3))) + (j3 / ((long) i4));
        }
        long j4 = this.f6834e;
        if (j3 <= j4) {
            j3 = j4;
        }
        this.f6834e = j3;
    }

    public final double b() {
        return this.f6832c;
    }

    public final long c() {
        return this.f6834e;
    }

    public final double d() {
        long jLongValue;
        Long lValueOf;
        if (this.f6830a.size() == 0 && this.f6831b.size() == 0) {
            return 0.0d;
        }
        if (this.f6830a.size() > this.f6831b.size()) {
            lValueOf = (Long) this.f6830a.peek();
        } else {
            Long l3 = (Long) this.f6830a.peek();
            if (l3 != null) {
                jLongValue = l3.longValue();
            } else {
                Object objPeek = this.f6831b.peek();
                D2.h.c(objPeek);
                jLongValue = ((Number) objPeek).longValue();
            }
            lValueOf = Long.valueOf(jLongValue / ((long) 2));
        }
        return lValueOf.longValue();
    }
}
