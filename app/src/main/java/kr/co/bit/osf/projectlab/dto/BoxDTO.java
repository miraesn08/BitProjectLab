package kr.co.bit.osf.projectlab.dto;

public class BoxDTO {
    private long id;
    private String name;
    private int type;           // 0:user box, 1:demo box
    int seq;
    int position;               // for Adapter

    public BoxDTO() { }

    public BoxDTO(String name) {
        this.name = name;
    }

    public BoxDTO(int id, String name, int type, int seq) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.seq = seq;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

        BoxDTO that = (BoxDTO) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + type;
        return result;
    }

    @Override
    public String toString() {
        return "BoxDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", seq=" + seq +
                '}';
    }
}
