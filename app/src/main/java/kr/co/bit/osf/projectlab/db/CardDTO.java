package kr.co.bit.osf.projectlab.db;

import android.os.Parcel;
import android.os.Parcelable;

public class CardDTO implements Parcelable {
    private long id;
    private String name;
    private String imagePath;   // image full path
    private int type;           // 0:user card, 1:demo card
    int seq;
    int boxId;                  // Box id

    public CardDTO() { }

    public CardDTO(String name, String imagePath, int boxId) {
        this.name = name;
        this.imagePath = imagePath;
        this.boxId = boxId;
    }

    public CardDTO(String name, String imagePath, int type, int boxId) {
        this(name, imagePath, boxId);
        this.type = type;
    }

    public CardDTO(long id, String name, String imagePath, int type, int boxId) {
        this(name, imagePath, type, boxId);
        this.id = id;
    }

    public CardDTO(long id, String name, String imagePath, int type, int seq, int boxId) {
        this(id, name, imagePath, type, boxId);
        this.seq = seq;
    }

    // Parcelable
    // http://developer.android.com/intl/ko/reference/android/os/Parcelable.html
    // http://www.developerphil.com/parcelable-vs-serializable/
    protected CardDTO(Parcel in) {
        id = in.readLong();
        name = in.readString();
        imagePath = in.readString();
        type = in.readInt();
        seq = in.readInt();
        boxId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(imagePath);
        dest.writeInt(type);
        dest.writeInt(seq);
        dest.writeInt(boxId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardDTO> CREATOR = new Creator<CardDTO>() {
        @Override
        public CardDTO createFromParcel(Parcel in) {
            return new CardDTO(in);
        }

        @Override
        public CardDTO[] newArray(int size) {
            return new CardDTO[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int order) {
        this.seq = order;
    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardDTO cardDTO = (CardDTO) o;

        if (id != cardDTO.id) return false;
        if (type != cardDTO.type) return false;
        if (seq != cardDTO.seq) return false;
        if (boxId != cardDTO.boxId) return false;
        if (!name.equals(cardDTO.name)) return false;
        return imagePath.equals(cardDTO.imagePath);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + imagePath.hashCode();
        result = 31 * result + type;
        result = 31 * result + boxId;
        return result;
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", type=" + type +
                ", seq=" + seq +
                ", boxId=" + boxId +
                '}';
    }
}
