package com.oliver.weatherapp.data.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "cities")
public class CityEntry implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String address;
    public double latitude;
    public double longitude;

    public CityEntry(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityEntry)) return false;

        CityEntry cityEntry = (CityEntry) o;

        if (id != cityEntry.id) return false;
        if (Double.compare(cityEntry.latitude, latitude) != 0) return false;
        if (Double.compare(cityEntry.longitude, longitude) != 0) return false;
        if (name != null ? !name.equals(cityEntry.name) : cityEntry.name != null) return false;
        return address != null ? address.equals(cityEntry.address) : cityEntry.address == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected CityEntry(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.address = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<CityEntry> CREATOR = new Parcelable.Creator<CityEntry>() {
        @Override
        public CityEntry createFromParcel(Parcel source) {
            return new CityEntry(source);
        }

        @Override
        public CityEntry[] newArray(int size) {
            return new CityEntry[size];
        }
    };

    @Override
    public String toString() {
        return "CityEntry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
