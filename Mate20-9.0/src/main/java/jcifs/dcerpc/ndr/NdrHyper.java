package jcifs.dcerpc.ndr;

public class NdrHyper extends NdrObject {
    public long value;

    public NdrHyper(long value2) {
        this.value = value2;
    }

    public void encode(NdrBuffer dst) throws NdrException {
        dst.enc_ndr_hyper(this.value);
    }

    public void decode(NdrBuffer src) throws NdrException {
        this.value = src.dec_ndr_hyper();
    }
}
