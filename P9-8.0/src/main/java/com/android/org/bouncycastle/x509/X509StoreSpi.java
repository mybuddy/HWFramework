package com.android.org.bouncycastle.x509;

import com.android.org.bouncycastle.util.Selector;
import java.util.Collection;

public abstract class X509StoreSpi {
    public abstract Collection engineGetMatches(Selector selector);

    public abstract void engineInit(X509StoreParameters x509StoreParameters);
}
