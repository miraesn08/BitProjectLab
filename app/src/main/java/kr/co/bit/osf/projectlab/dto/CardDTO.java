package kr.co.bit.osf.projectlab.dto;

public class CardDTO {
    private int id;
    private String name;
    private String imagePath;   // image full path
    private int type;           // 0:user card, 1:demo card
    int seq;
    int boxId;                  // CardBox id
    int position;               // for Adapter

    public CardDTO() { }

    public CardDTO(String name, String imagePath, int boxId) {
        this.name = name;
        this.imagePath = imagePath;
        this.boxId = boxId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardDTO that = (CardDTO) o;

        if (type != that.type) return false;
        if (boxId != that.boxId) return false;
        if (!name.equals(that.name)) return false;
        return imagePath.equals(that.imagePath);

    }

    @Override
    public int hashCode() {
        int result = id;
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
                ", position=" + position +
                '}';
    }
}
