package com.huawei.fingerprint;

import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import com.huawei.fingerprint.IAuthenticator;
import com.huawei.fingerprint.IAuthenticatorListener;

public class Authenticator {
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    public static final String SERVICE_NAME = "fido_authenticator";
    public FingerprintManager.AuthenticationCallback mAuthenticationCallback;
    /* access modifiers changed from: private */
    public AuthenticatorListener mAuthenticatorListener;
    private IFingerprintServiceReceiver mFpServiceReceiver = new IFingerprintServiceReceiver.Stub() {
        public void onEnrollResult(long deviceId, int fingerId, int groupId, int remaining) {
        }

        public void onAcquired(long deviceId, int acquireInfo, int vendorCode) {
            Authenticator.this.mHandler.obtainMessage(101, acquireInfo, 0, Long.valueOf(deviceId)).sendToTarget();
        }

        public void onAuthenticationSucceeded(long deviceId, Fingerprint fp, int userId) {
            Authenticator.this.mHandler.obtainMessage(Authenticator.MSG_AUTHENTICATION_SUCCEEDED, userId, 0, fp).sendToTarget();
        }

        public void onAuthenticationFailed(long deviceId) {
            Authenticator.this.mHandler.obtainMessage(Authenticator.MSG_AUTHENTICATION_FAILED).sendToTarget();
        }

        public void onError(long deviceId, int error, int vendorCode) {
            Authenticator.this.mHandler.obtainMessage(Authenticator.MSG_ERROR, error, 0, Long.valueOf(deviceId)).sendToTarget();
        }

        public void onRemoved(long deviceId, int fingerId, int groupId, int remaining) {
        }

        public void onEnumerated(long deviceId, int fingerId, int groupId, int remaining) {
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler;
    private IAuthenticatorListener mIAuthenticatorListener = new IAuthenticatorListener.Stub() {
        public void onUserVerificationResult(final int result, final byte[] userid, final byte[] uvt) throws RemoteException {
            Authenticator.this.mHandler.post(new Runnable() {
                public void run() {
                    Log.d("HwFingerprintService", "Authenticator-onUserVerificationResult-run");
                    if (Authenticator.this.mAuthenticatorListener != null) {
                        Log.d("HwFingerprintService", "Authenticator-mAuthenticatorListener != null");
                        Authenticator.this.mAuthenticatorListener.onUserVerificationResult(result, userid, uvt);
                        AuthenticatorListener unused = Authenticator.this.mAuthenticatorListener = null;
                    }
                }
            });
        }
    };
    private IAuthenticator mService;

    public interface AuthenticatorListener {
        void onIsUserIDValidResult(boolean z);

        void onUserVerificationResult(int i, byte[] bArr, byte[] bArr2);
    }

    private class MyHandler extends Handler {
        private MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1);
                    return;
                case Authenticator.MSG_AUTHENTICATION_SUCCEEDED /*102*/:
                    sendAuthenticatedSucceeded((Fingerprint) msg.obj, msg.arg1);
                    return;
                case Authenticator.MSG_AUTHENTICATION_FAILED /*103*/:
                    sendAuthenticatedFailed();
                    return;
                case Authenticator.MSG_ERROR /*104*/:
                    sendErrorResult(((Long) msg.obj).longValue(), msg.arg1);
                    return;
                default:
                    return;
            }
        }

        private void sendErrorResult(long deviceId, int errMsgId) {
            if (Authenticator.this.mAuthenticationCallback != null) {
                Authenticator.this.mAuthenticationCallback.onAuthenticationError(errMsgId, "");
            }
        }

        private void sendAuthenticatedSucceeded(Fingerprint fp, int userId) {
            if (Authenticator.this.mAuthenticationCallback != null) {
                Authenticator.this.mAuthenticationCallback.onAuthenticationSucceeded(new FingerprintManager.AuthenticationResult(null, fp, userId));
            }
        }

        private void sendAuthenticatedFailed() {
            if (Authenticator.this.mAuthenticationCallback != null) {
                Authenticator.this.mAuthenticationCallback.onAuthenticationFailed();
            }
        }

        private void sendAcquiredResult(long deviceId, int acquireInfo) {
            if (Authenticator.this.mAuthenticationCallback != null) {
                Authenticator.this.mAuthenticationCallback.onAuthenticationAcquired(acquireInfo);
            }
        }
    }

    public static Authenticator getAuthenticator() {
        IBinder service = ServiceManager.getService(SERVICE_NAME);
        if (service != null) {
            return new Authenticator(service, new Binder(), null);
        }
        return null;
    }

    public void setAuthenticationCallback(FingerprintManager.AuthenticationCallback callbak) {
        this.mAuthenticationCallback = callbak;
    }

    private Authenticator(IBinder service, IBinder client, Handler handler) {
        this.mService = IAuthenticator.Stub.asInterface(service);
        this.mHandler = handler == null ? new MyHandler(Looper.getMainLooper()) : handler;
    }

    public int verifyUser(FingerprintManager.AuthenticationCallback callbak, byte[] finalChallenge, String aaid, AuthenticatorListener listener) {
        this.mAuthenticationCallback = callbak;
        return verifyUser(finalChallenge, aaid, listener);
    }

    public int verifyUser(byte[] finalChallenge, String aaid, AuthenticatorListener listener) {
        this.mAuthenticatorListener = listener;
        try {
            this.mService.verifyUser(this.mFpServiceReceiver, this.mIAuthenticatorListener, UserHandle.myUserId(), finalChallenge, aaid);
        } catch (RemoteException e) {
            Log.e("HwFingerprintService", "RemoteException-verifyUser");
        }
        return 0;
    }

    public int cancelVerify() {
        try {
            this.mService.cancelVerifyUser(this.mFpServiceReceiver, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e("HwFingerprintService", "RemoteException-cancelVerifyUser");
        }
        return 0;
    }
}
