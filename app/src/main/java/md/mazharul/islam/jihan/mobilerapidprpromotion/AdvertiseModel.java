package md.mazharul.islam.jihan.mobilerapidprpromotion;

/**
 * Created by Arif on 6/4/2017.
 */

public class AdvertiseModel {
    private int id;
    private String address;
    private int duration;
    private boolean isPreviewNow;
    private String boxName;

    public AdvertiseModel(int id, String address, int duration, boolean isPreviewNow, String boxName) {
        this.id = id;
        this.address = address;
        this.duration = duration;
        this.isPreviewNow = isPreviewNow;
        this.boxName = boxName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isPreviewNow() {
        return isPreviewNow;
    }

    public void setPreviewNow(boolean previewNow) {
        isPreviewNow = previewNow;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }
}
