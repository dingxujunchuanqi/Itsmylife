package com.sinoautodiagnoseos.entity.Download;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lanye on 17/2/17.
 */
public class Download implements Parcelable {
    private int progress;
    private long currentFileSize;
    private long totalFileSize;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(long currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.progress);
        dest.writeLong(this.currentFileSize);
        dest.writeLong(this.totalFileSize);
    }

    public Download() {
    }

    protected Download(Parcel in) {
        this.progress = in.readInt();
        this.currentFileSize = in.readLong();
        this.totalFileSize = in.readLong();
    }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public com.sinoautodiagnoseos.entity.Download.Download createFromParcel(Parcel source) {
            return new com.sinoautodiagnoseos.entity.Download.Download(source);
        }

        @Override
        public com.sinoautodiagnoseos.entity.Download.Download[] newArray(int size) {
            return new com.sinoautodiagnoseos.entity.Download.Download[size];
        }
    };
}
