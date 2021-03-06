package android.location;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGeoFencer extends IInterface {

    public static abstract class Stub extends Binder implements IGeoFencer {
        private static final String DESCRIPTOR = "android.location.IGeoFencer";
        static final int TRANSACTION_clearGeoFence = 2;
        static final int TRANSACTION_clearGeoFenceUser = 3;
        static final int TRANSACTION_setGeoFence = 1;

        private static class Proxy implements IGeoFencer {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public boolean setGeoFence(IBinder who, GeoFenceParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    boolean _result = true;
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearGeoFence(IBinder who, PendingIntent fence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    if (fence != null) {
                        _data.writeInt(1);
                        fence.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearGeoFenceUser(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGeoFencer asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGeoFencer)) {
                return new Proxy(obj);
            }
            return (IGeoFencer) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: android.location.GeoFenceParams} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.app.PendingIntent} */
        /* JADX WARNING: type inference failed for: r1v1 */
        /* JADX WARNING: type inference failed for: r1v12 */
        /* JADX WARNING: type inference failed for: r1v13 */
        /* JADX WARNING: Multi-variable type inference failed */
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                ? _arg1 = 0;
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = GeoFenceParams.CREATOR.createFromParcel(data);
                        }
                        boolean _result = setGeoFence(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        IBinder _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        }
                        clearGeoFence(_arg02, _arg1);
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        clearGeoFenceUser(data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                reply.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    void clearGeoFence(IBinder iBinder, PendingIntent pendingIntent) throws RemoteException;

    void clearGeoFenceUser(int i) throws RemoteException;

    boolean setGeoFence(IBinder iBinder, GeoFenceParams geoFenceParams) throws RemoteException;
}
