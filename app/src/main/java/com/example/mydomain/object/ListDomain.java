package com.example.mydomain.object;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ListDomain extends ArrayList<Parcelable> implements Parcelable {
    private ArrayList<InfoDomain> ds;

    public ArrayList<InfoDomain> getDs() {
        return ds;
    }

    public void setDs(ArrayList<InfoDomain> ds) {
        this.ds = ds;
    }

    public ListDomain(ArrayList<InfoDomain> ds) {
        this.ds = ds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ds);
    }

    protected ListDomain(Parcel in) {
        ds = in.createTypedArrayList(InfoDomain.CREATOR);
    }

    public static final Creator<ListDomain> CREATOR = new Creator<ListDomain>() {
        @Override
        public ListDomain createFromParcel(Parcel in) {
            return new ListDomain(in);
        }

        @Override
        public ListDomain[] newArray(int size) {
            return new ListDomain[size];
        }
    };

    @NonNull
    @Override
    public Stream<Parcelable> parallelStream() {
        return super.parallelStream();
    }
}
