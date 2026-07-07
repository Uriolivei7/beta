package X2;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/* JADX INFO: loaded from: classes.dex */
public final class f extends Handler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final f f2766a = new f();

    private f() {
    }

    @Override // java.util.logging.Handler
    public void publish(LogRecord logRecord) {
        D2.h.f(logRecord, "record");
        e eVar = e.f2765c;
        String loggerName = logRecord.getLoggerName();
        D2.h.e(loggerName, "record.loggerName");
        int iB = g.b(logRecord);
        String message = logRecord.getMessage();
        D2.h.e(message, "record.message");
        eVar.a(loggerName, iB, message, logRecord.getThrown());
    }

    @Override // java.util.logging.Handler
    public void close() {
    }

    @Override // java.util.logging.Handler
    public void flush() {
    }
}
