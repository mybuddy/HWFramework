package com.huawei.hilink.framework.aidl;

import android.os.RemoteException;
import com.huawei.hilink.framework.aidl.IRequestHandler;

public abstract class RequestHandlerWrapper extends IRequestHandler.Stub {
    public static final int ERRORCODE_BAD_REQUEST = 400;
    public static final int ERRORCODE_FAILED = -1;
    public static final int ERRORCODE_METHOD_NOT_ALLOWED = 405;
    public static final int ERRORCODE_NOT_FOUND = 404;
    public static final int ERRORCODE_OK = 0;

    public abstract void handleRequest(CallRequest callRequest) throws RemoteException;
}
