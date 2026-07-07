package com.facebook.react.bridge;

import android.os.SystemClock;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ReactMarker {
    private static Queue<ReactMarkerRecord> sNativeReactMarkerQueue = new ConcurrentLinkedQueue();
    private static final List<MarkerListener> sListeners = new CopyOnWriteArrayList();
    private static final List<FabricMarkerListener> sFabricMarkerListeners = new CopyOnWriteArrayList();

    public interface FabricMarkerListener {
        void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, long j3);

        default void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, long j3, int i4) {
            logFabricMarker(reactMarkerConstants, str, i3, j3);
        }
    }

    public interface MarkerListener {
        void logMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3);
    }

    private static class ReactMarkerRecord {
        private final String mMarkerName;
        private final long mMarkerTime;

        public ReactMarkerRecord(String str, long j3) {
            this.mMarkerName = str;
            this.mMarkerTime = j3;
        }

        public String getMarkerName() {
            return this.mMarkerName;
        }

        public long getMarkerTime() {
            return this.mMarkerTime;
        }
    }

    public static void addFabricListener(FabricMarkerListener fabricMarkerListener) {
        List<FabricMarkerListener> list = sFabricMarkerListeners;
        if (list.contains(fabricMarkerListener)) {
            return;
        }
        list.add(fabricMarkerListener);
    }

    public static void addListener(MarkerListener markerListener) {
        List<MarkerListener> list = sListeners;
        if (list.contains(markerListener)) {
            return;
        }
        list.add(markerListener);
    }

    public static void clearFabricMarkerListeners() {
        sFabricMarkerListeners.clear();
    }

    public static void clearMarkerListeners() {
        sListeners.clear();
    }

    public static void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, long j3, int i4) {
        Iterator<FabricMarkerListener> it = sFabricMarkerListeners.iterator();
        while (it.hasNext()) {
            it.next().logFabricMarker(reactMarkerConstants, str, i3, j3, i4);
        }
    }

    public static void logMarker(String str) {
        logMarker(str, (String) null);
    }

    private static native void nativeLogMarker(String str, long j3);

    private static void notifyNativeMarker(ReactMarkerConstants reactMarkerConstants, Long l3) {
        if (!reactMarkerConstants.hasMatchingNameMarker()) {
            return;
        }
        if (l3 == null) {
            l3 = Long.valueOf(SystemClock.uptimeMillis());
        }
        if (!ReactBridge.isInitialized()) {
            sNativeReactMarkerQueue.add(new ReactMarkerRecord(reactMarkerConstants.name(), l3.longValue()));
            return;
        }
        nativeLogMarker(reactMarkerConstants.name(), l3.longValue());
        while (true) {
            ReactMarkerRecord reactMarkerRecordPoll = sNativeReactMarkerQueue.poll();
            if (reactMarkerRecordPoll == null) {
                return;
            } else {
                nativeLogMarker(reactMarkerRecordPoll.getMarkerName(), reactMarkerRecordPoll.getMarkerTime());
            }
        }
    }

    public static void removeFabricListener(FabricMarkerListener fabricMarkerListener) {
        sFabricMarkerListeners.remove(fabricMarkerListener);
    }

    public static void removeListener(MarkerListener markerListener) {
        sListeners.remove(markerListener);
    }

    public static void logMarker(String str, int i3) {
        logMarker(str, (String) null, i3);
    }

    public static void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, long j3) {
        Iterator<FabricMarkerListener> it = sFabricMarkerListeners.iterator();
        while (it.hasNext()) {
            it.next().logFabricMarker(reactMarkerConstants, str, i3, j3, 0);
        }
    }

    public static void logMarker(String str, String str2) {
        logMarker(str, str2, 0);
    }

    public static void logMarker(String str, String str2, int i3) {
        logMarker(ReactMarkerConstants.valueOf(str), str2, i3);
    }

    public static void logFabricMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3) {
        logFabricMarker(reactMarkerConstants, str, i3, SystemClock.uptimeMillis(), 0);
    }

    public static void logMarker(ReactMarkerConstants reactMarkerConstants) {
        logMarker(reactMarkerConstants, (String) null, 0);
    }

    public static void logMarker(ReactMarkerConstants reactMarkerConstants, int i3) {
        logMarker(reactMarkerConstants, (String) null, i3);
    }

    public static void logMarker(ReactMarkerConstants reactMarkerConstants, String str) {
        logMarker(reactMarkerConstants, str, 0);
    }

    public static void logMarker(ReactMarkerConstants reactMarkerConstants, long j3) {
        logMarker(reactMarkerConstants, null, 0, Long.valueOf(j3));
    }

    public static void logMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3) {
        logMarker(reactMarkerConstants, str, i3, null);
    }

    public static void logMarker(ReactMarkerConstants reactMarkerConstants, String str, int i3, Long l3) {
        logFabricMarker(reactMarkerConstants, str, i3);
        Iterator<MarkerListener> it = sListeners.iterator();
        while (it.hasNext()) {
            it.next().logMarker(reactMarkerConstants, str, i3);
        }
        notifyNativeMarker(reactMarkerConstants, l3);
    }
}
