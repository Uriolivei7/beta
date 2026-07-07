package com.reactnativecommunity.asyncstorage;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class h {
    private static void a(FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws Throwable {
        FileChannel fileChannel;
        FileChannel channel = null;
        try {
            FileChannel channel2 = fileInputStream.getChannel();
            try {
                channel = fileOutputStream.getChannel();
                channel2.transferTo(0L, channel2.size(), channel);
                try {
                    channel2.close();
                } finally {
                    if (channel != null) {
                        channel.close();
                    }
                }
            } catch (Throwable th) {
                th = th;
                FileChannel fileChannel2 = channel;
                channel = channel2;
                fileChannel = fileChannel2;
                if (channel != null) {
                    try {
                        channel.close();
                    } finally {
                        if (fileChannel != null) {
                            fileChannel.close();
                        }
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileChannel = null;
        }
    }

    private static ArrayList b(Context context) {
        ArrayList arrayList = new ArrayList();
        try {
            File[] fileArrListFiles = context.getDatabasePath("noop").getParentFile().listFiles();
            if (fileArrListFiles != null) {
                for (File file : fileArrListFiles) {
                    if (file.getName().startsWith("RKStorage-scoped-experience-") && !file.getName().endsWith("-journal")) {
                        arrayList.add(file);
                    }
                }
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        return arrayList;
    }

    private static File c(ArrayList arrayList) {
        File file = null;
        if (arrayList.size() == 0) {
            return null;
        }
        Iterator it = arrayList.iterator();
        long j3 = -1;
        while (it.hasNext()) {
            File file2 = (File) it.next();
            long jE = e(file2);
            if (jE > j3) {
                file = file2;
                j3 = jE;
            }
        }
        return file != null ? file : (File) arrayList.get(0);
    }

    private static long d(File file) {
        try {
            return Files.readAttributes(file.toPath(), d.a(), new LinkOption[0]).creationTime().toMillis();
        } catch (Exception unused) {
            return -1L;
        }
    }

    private static long e(File file) {
        try {
            return Build.VERSION.SDK_INT >= 26 ? d(file) : file.lastModified();
        } catch (Exception e4) {
            e4.printStackTrace();
            return -1L;
        }
    }

    private static boolean f(Context context) {
        return context.getDatabasePath("RKStorage").exists();
    }

    public static void g(Context context) throws Throwable {
        if (f(context)) {
            return;
        }
        ArrayList<File> arrayListB = b(context);
        File fileC = c(arrayListB);
        if (fileC == null) {
            Log.v("AsyncStorageExpoMigration", "No scoped database found");
            return;
        }
        try {
            k.z(context).y();
            a(new FileInputStream(fileC), new FileOutputStream(context.getDatabasePath("RKStorage")));
            Log.v("AsyncStorageExpoMigration", "Migrated most recently modified database " + fileC.getName() + " to RKStorage");
            try {
                for (File file : arrayListB) {
                    if (file.delete()) {
                        Log.v("AsyncStorageExpoMigration", "Deleted scoped database " + file.getName());
                    } else {
                        Log.v("AsyncStorageExpoMigration", "Failed to delete scoped database " + file.getName());
                    }
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
            Log.v("AsyncStorageExpoMigration", "Completed the scoped AsyncStorage migration");
        } catch (Exception e5) {
            Log.v("AsyncStorageExpoMigration", "Failed to migrate scoped database " + fileC.getName());
            e5.printStackTrace();
        }
    }
}
