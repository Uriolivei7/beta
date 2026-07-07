package com.facebook.react.soloader;

import D2.h;
import com.facebook.soloader.l;

/* JADX INFO: loaded from: classes.dex */
public final class OpenSourceMergedSoMapping implements l {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final OpenSourceMergedSoMapping f7227a = new OpenSourceMergedSoMapping();

    private OpenSourceMergedSoMapping() {
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.facebook.soloader.l
    public void a(String str) {
        h.f(str, "libraryName");
        switch (str.hashCode()) {
            case -1793638007:
                if (str.equals("mapbufferjni")) {
                    libmapbufferjni_so();
                    break;
                }
                break;
            case -1624070447:
                if (str.equals("rninstance")) {
                    librninstance_so();
                    break;
                }
                break;
            case -1570429553:
                if (str.equals("reactnativejni")) {
                    libreactnativejni_so();
                    break;
                }
                break;
            case -1454983728:
                if (str.equals("jsctooling")) {
                    libjsctooling_so();
                    break;
                }
                break;
            case -1438915853:
                if (str.equals("reactnativeblob")) {
                    libreactnativeblob_so();
                    break;
                }
                break;
            case -1382694412:
                if (str.equals("react_featureflagsjni")) {
                    libreact_featureflagsjni_so();
                    break;
                }
                break;
            case -1033318826:
                if (str.equals("reactnative")) {
                    libreactnative_so();
                    break;
                }
                break;
            case -616737073:
                if (str.equals("jscinstance")) {
                    libjscinstance_so();
                    break;
                }
                break;
            case -579037304:
                if (str.equals("react_newarchdefaults")) {
                    libreact_newarchdefaults_so();
                    break;
                }
                break;
            case -49345041:
                if (str.equals("turbomodulejsijni")) {
                    libturbomodulejsijni_so();
                    break;
                }
                break;
            case 3714672:
                if (str.equals("yoga")) {
                    libyoga_so();
                    break;
                }
                break;
            case 65536138:
                if (str.equals("hermesinstancejni")) {
                    libhermesinstancejni_so();
                    break;
                }
                break;
            case 86183502:
                if (str.equals("jsijniprofiler")) {
                    libjsijniprofiler_so();
                    break;
                }
                break;
            case 352552524:
                if (str.equals("hermes_executor")) {
                    libhermes_executor_so();
                    break;
                }
                break;
            case 614482404:
                if (str.equals("hermestooling")) {
                    libhermestooling_so();
                    break;
                }
                break;
            case 688235659:
                if (str.equals("react_devsupportjni")) {
                    libreact_devsupportjni_so();
                    break;
                }
                break;
            case 716617324:
                if (str.equals("uimanagerjni")) {
                    libuimanagerjni_so();
                    break;
                }
                break;
            case 871152397:
                if (str.equals("jscexecutor")) {
                    libjscexecutor_so();
                    break;
                }
                break;
            case 1236065886:
                if (str.equals("jscruntime")) {
                    libjscruntime_so();
                    break;
                }
                break;
            case 1590431694:
                if (str.equals("jsinspector")) {
                    libjsinspector_so();
                    break;
                }
                break;
            case 2016911584:
                if (str.equals("fabricjni")) {
                    libfabricjni_so();
                    break;
                }
                break;
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.facebook.soloader.l
    public String b(String str) {
        h.f(str, "input");
        switch (str.hashCode()) {
            case -1793638007:
                if (str.equals("mapbufferjni")) {
                }
                break;
            case -1624070447:
                if (!str.equals("rninstance")) {
                }
                break;
            case -1570429553:
                if (!str.equals("reactnativejni")) {
                }
                break;
            case -1438915853:
                if (!str.equals("reactnativeblob")) {
                }
                break;
            case -1382694412:
                if (!str.equals("react_featureflagsjni")) {
                }
                break;
            case -616737073:
                if (!str.equals("jscinstance")) {
                }
                break;
            case -579037304:
                if (!str.equals("react_newarchdefaults")) {
                }
                break;
            case -49345041:
                if (!str.equals("turbomodulejsijni")) {
                }
                break;
            case 3714672:
                if (!str.equals("yoga")) {
                }
                break;
            case 65536138:
                if (!str.equals("hermesinstancejni")) {
                }
                break;
            case 86183502:
                if (!str.equals("jsijniprofiler")) {
                }
                break;
            case 352552524:
                if (!str.equals("hermes_executor")) {
                }
                break;
            case 688235659:
                if (!str.equals("react_devsupportjni")) {
                }
                break;
            case 716617324:
                if (!str.equals("uimanagerjni")) {
                }
                break;
            case 871152397:
                if (!str.equals("jscexecutor")) {
                }
                break;
            case 1236065886:
                if (!str.equals("jscruntime")) {
                }
                break;
            case 1590431694:
                if (!str.equals("jsinspector")) {
                }
                break;
            case 2016911584:
                if (!str.equals("fabricjni")) {
                }
                break;
        }
        return str;
    }

    public final native int libfabricjni_so();

    public final native int libhermes_executor_so();

    public final native int libhermesinstancejni_so();

    public final native int libhermestooling_so();

    public final native int libjscexecutor_so();

    public final native int libjscinstance_so();

    public final native int libjscruntime_so();

    public final native int libjsctooling_so();

    public final native int libjsijniprofiler_so();

    public final native int libjsinspector_so();

    public final native int libmapbufferjni_so();

    public final native int libreact_devsupportjni_so();

    public final native int libreact_featureflagsjni_so();

    public final native int libreact_newarchdefaults_so();

    public final native int libreactnative_so();

    public final native int libreactnativeblob_so();

    public final native int libreactnativejni_so();

    public final native int librninstance_so();

    public final native int libturbomodulejsijni_so();

    public final native int libuimanagerjni_so();

    public final native int libyoga_so();
}
