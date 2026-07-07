package org.chromium.support_lib_boundary;

/* JADX INFO: loaded from: classes.dex */
public interface ServiceWorkerWebSettingsBoundaryInterface {
    boolean getAllowContentAccess();

    boolean getAllowFileAccess();

    boolean getBlockNetworkLoads();

    int getCacheMode();

    void setAllowContentAccess(boolean z3);

    void setAllowFileAccess(boolean z3);

    void setBlockNetworkLoads(boolean z3);

    void setCacheMode(int i3);
}
