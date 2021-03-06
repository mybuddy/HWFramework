package android.media;

class AudioHandle {
    private final int mId;

    AudioHandle(int id) {
        this.mId = id;
    }

    /* access modifiers changed from: package-private */
    public int id() {
        return this.mId;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof AudioHandle)) {
            return false;
        }
        if (this.mId == ((AudioHandle) o).id()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.mId;
    }

    public String toString() {
        return Integer.toString(this.mId);
    }
}
