package com.facebook.react.bridge.queue;

import D2.h;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.Pair;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.queue.MessageQueueThreadImpl;
import com.facebook.react.bridge.queue.MessageQueueThreadSpec;
import com.facebook.react.common.futures.SimpleSettableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import kotlin.jvm.internal.DefaultConstructorMarker;
import r2.C0685h;

/* JADX INFO: loaded from: classes.dex */
public final class MessageQueueThreadImpl implements MessageQueueThread {
    public static final Companion Companion = new Companion(null);
    private final String assertionErrorMessage;
    private final MessageQueueThreadHandler handler;
    private volatile boolean isFinished;
    private final Looper looper;
    private final String name;
    private final MessageQueueThreadPerfStats stats;

    public static final class Companion {

        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[MessageQueueThreadSpec.ThreadType.values().length];
                try {
                    iArr[MessageQueueThreadSpec.ThreadType.MAIN_UI.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[MessageQueueThreadSpec.ThreadType.NEW_BACKGROUND.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void assignToPerfStats(MessageQueueThreadPerfStats messageQueueThreadPerfStats, long j3, long j4) {
            if (messageQueueThreadPerfStats != null) {
                messageQueueThreadPerfStats.wallTime = j3;
                messageQueueThreadPerfStats.cpuTime = j4;
            }
        }

        private final MessageQueueThreadImpl createForMainThread(String str, QueueThreadExceptionHandler queueThreadExceptionHandler) {
            Looper mainLooper = Looper.getMainLooper();
            h.e(mainLooper, "getMainLooper(...)");
            return new MessageQueueThreadImpl(str, mainLooper, queueThreadExceptionHandler, null, 8, null);
        }

        private final MessageQueueThreadImpl startNewBackgroundThread(String str, long j3, QueueThreadExceptionHandler queueThreadExceptionHandler) {
            Looper looper;
            final SimpleSettableFuture simpleSettableFuture = new SimpleSettableFuture();
            new Thread(null, new Runnable() { // from class: com.facebook.react.bridge.queue.c
                @Override // java.lang.Runnable
                public final void run() {
                    MessageQueueThreadImpl.Companion.startNewBackgroundThread$lambda$1(simpleSettableFuture);
                }
            }, "mqt_" + str, j3).start();
            Pair pair = (Pair) simpleSettableFuture.b();
            if (pair == null || (looper = (Looper) pair.first) == null) {
                throw new RuntimeException("Looper not found for thread");
            }
            return new MessageQueueThreadImpl(str, looper, queueThreadExceptionHandler, (MessageQueueThreadPerfStats) pair.second, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void startNewBackgroundThread$lambda$1(SimpleSettableFuture simpleSettableFuture) {
            Process.setThreadPriority(-4);
            Looper.prepare();
            MessageQueueThreadPerfStats messageQueueThreadPerfStats = new MessageQueueThreadPerfStats();
            MessageQueueThreadImpl.Companion.assignToPerfStats(messageQueueThreadPerfStats, SystemClock.uptimeMillis(), SystemClock.currentThreadTimeMillis());
            simpleSettableFuture.c(new Pair(Looper.myLooper(), messageQueueThreadPerfStats));
            Looper.loop();
        }

        public final MessageQueueThreadImpl create(MessageQueueThreadSpec messageQueueThreadSpec, QueueThreadExceptionHandler queueThreadExceptionHandler) {
            h.f(messageQueueThreadSpec, "spec");
            h.f(queueThreadExceptionHandler, "exceptionHandler");
            int i3 = WhenMappings.$EnumSwitchMapping$0[messageQueueThreadSpec.getThreadType().ordinal()];
            if (i3 == 1) {
                return createForMainThread(messageQueueThreadSpec.getName(), queueThreadExceptionHandler);
            }
            if (i3 == 2) {
                return startNewBackgroundThread(messageQueueThreadSpec.getName(), messageQueueThreadSpec.getStackSize(), queueThreadExceptionHandler);
            }
            throw new C0685h();
        }

        private Companion() {
        }
    }

    public /* synthetic */ MessageQueueThreadImpl(String str, Looper looper, QueueThreadExceptionHandler queueThreadExceptionHandler, MessageQueueThreadPerfStats messageQueueThreadPerfStats, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, looper, queueThreadExceptionHandler, messageQueueThreadPerfStats);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void callOnQueue$lambda$0(SimpleSettableFuture simpleSettableFuture, Callable callable) {
        try {
            simpleSettableFuture.c(callable.call());
        } catch (Exception e4) {
            simpleSettableFuture.d(e4);
        }
    }

    public static final MessageQueueThreadImpl create(MessageQueueThreadSpec messageQueueThreadSpec, QueueThreadExceptionHandler queueThreadExceptionHandler) {
        return Companion.create(messageQueueThreadSpec, queueThreadExceptionHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void resetPerfStats$lambda$1(MessageQueueThreadImpl messageQueueThreadImpl) {
        Companion.assignToPerfStats(messageQueueThreadImpl.stats, SystemClock.uptimeMillis(), SystemClock.currentThreadTimeMillis());
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public void assertIsOnThread() {
        SoftAssertions.assertCondition(isOnThread(), this.assertionErrorMessage);
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public <T> Future<T> callOnQueue(final Callable<T> callable) {
        h.f(callable, "callable");
        final SimpleSettableFuture simpleSettableFuture = new SimpleSettableFuture();
        runOnQueue(new Runnable() { // from class: com.facebook.react.bridge.queue.a
            @Override // java.lang.Runnable
            public final void run() {
                MessageQueueThreadImpl.callOnQueue$lambda$0(simpleSettableFuture, callable);
            }
        });
        return simpleSettableFuture;
    }

    public final Looper getLooper() {
        return this.looper;
    }

    public final String getName() {
        return this.name;
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public MessageQueueThreadPerfStats getPerfStats() {
        return this.stats;
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public boolean isIdle() {
        return this.looper.getQueue().isIdle();
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public boolean isOnThread() {
        return this.looper.getThread() == Thread.currentThread();
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public void quitSynchronous() {
        this.isFinished = true;
        this.looper.quit();
        if (this.looper.getThread() != Thread.currentThread()) {
            try {
                this.looper.getThread().join();
            } catch (InterruptedException unused) {
                throw new RuntimeException("Got interrupted waiting to join thread " + this.name);
            }
        }
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public void resetPerfStats() {
        Companion.assignToPerfStats(this.stats, -1L, -1L);
        runOnQueue(new Runnable() { // from class: com.facebook.react.bridge.queue.b
            @Override // java.lang.Runnable
            public final void run() {
                MessageQueueThreadImpl.resetPerfStats$lambda$1(this.f6510b);
            }
        });
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public boolean runOnQueue(Runnable runnable) {
        h.f(runnable, "runnable");
        if (!this.isFinished) {
            this.handler.post(runnable);
            return true;
        }
        Y.a.I("ReactNative", "Tried to enqueue runnable on already finished thread: '" + this.name + "... dropping Runnable.");
        return false;
    }

    private MessageQueueThreadImpl(String str, Looper looper, QueueThreadExceptionHandler queueThreadExceptionHandler, MessageQueueThreadPerfStats messageQueueThreadPerfStats) {
        this.name = str;
        this.looper = looper;
        this.stats = messageQueueThreadPerfStats;
        this.handler = new MessageQueueThreadHandler(looper, queueThreadExceptionHandler);
        this.assertionErrorMessage = "Expected to be called from the '" + str + "' thread!";
    }

    @Override // com.facebook.react.bridge.queue.MessageQueueThread
    public void assertIsOnThread(String str) {
        h.f(str, "message");
        boolean zIsOnThread = isOnThread();
        String str2 = this.assertionErrorMessage + " " + str;
        h.e(str2, "toString(...)");
        SoftAssertions.assertCondition(zIsOnThread, str2);
    }

    /* synthetic */ MessageQueueThreadImpl(String str, Looper looper, QueueThreadExceptionHandler queueThreadExceptionHandler, MessageQueueThreadPerfStats messageQueueThreadPerfStats, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, looper, queueThreadExceptionHandler, (i3 & 8) != 0 ? null : messageQueueThreadPerfStats);
    }
}
