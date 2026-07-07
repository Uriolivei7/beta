package com.reactnativecommunity.asyncstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/* JADX INFO: loaded from: classes.dex */
public class k extends SQLiteOpenHelper {

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static k f8437e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f8438b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private SQLiteDatabase f8439c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f8440d;

    private k(Context context) {
        super(context, "RKStorage", (SQLiteDatabase.CursorFactory) null, 1);
        this.f8440d = j.f8436a.longValue() * 1048576;
        this.f8438b = context;
    }

    private synchronized boolean q() {
        o();
        return this.f8438b.deleteDatabase("RKStorage");
    }

    public static k z(Context context) {
        if (f8437e == null) {
            f8437e = new k(context.getApplicationContext());
        }
        return f8437e;
    }

    synchronized void a() {
        y().delete("catalystLocalStorage", null, null);
    }

    public synchronized void i() {
        try {
            a();
            o();
            Y.a.b("ReactNative", "Cleaned RKStorage");
        } catch (Exception unused) {
            if (!q()) {
                throw new RuntimeException("Clearing and deleting database RKStorage failed");
            }
            Y.a.b("ReactNative", "Deleted Local Database RKStorage");
        }
    }

    public synchronized void o() {
        SQLiteDatabase sQLiteDatabase = this.f8439c;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            this.f8439c.close();
            this.f8439c = null;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE catalystLocalStorage (key TEXT PRIMARY KEY, value TEXT NOT NULL)");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i3, int i4) {
        if (i3 != i4) {
            q();
            onCreate(sQLiteDatabase);
        }
    }

    synchronized boolean v() {
        SQLiteDatabase sQLiteDatabase = this.f8439c;
        if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
            return true;
        }
        SQLiteException e4 = null;
        for (int i3 = 0; i3 < 2; i3++) {
            if (i3 > 0) {
                try {
                    q();
                } catch (SQLiteException e5) {
                    e4 = e5;
                    try {
                        Thread.sleep(30L);
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            this.f8439c = getWritableDatabase();
        }
        SQLiteDatabase sQLiteDatabase2 = this.f8439c;
        if (sQLiteDatabase2 == null) {
            throw e4;
        }
        sQLiteDatabase2.setMaximumSize(this.f8440d);
        return true;
    }

    public synchronized SQLiteDatabase y() {
        v();
        return this.f8439c;
    }
}
