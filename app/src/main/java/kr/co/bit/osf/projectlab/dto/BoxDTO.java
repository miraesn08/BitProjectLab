package kr.co.bit.osf.projectlab.dto;

public class BoxDTO {
    private int id;
    private String name;
    private int type;           // 0:user box, 1:demo box
    int order;
    int position;               // for Adapter

    public BoxDTO(String name) {
        this.name = name;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

        if (id != that.id) return false;
        if (type != that.type) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = id;
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
                ", order=" + order +
                '}';
    }
}
