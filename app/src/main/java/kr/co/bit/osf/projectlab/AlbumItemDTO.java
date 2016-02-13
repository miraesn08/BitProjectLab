package kr.co.bit.osf.projectlab;

public class AlbumItemDTO {
    private int id;
    private int type;
    private String text;
    private String imageUrl;
    private int order;
    private int position;   // for Adapter

    public AlbumItemDTO() {
    }

    public AlbumItemDTO(int id, int type, String text, String imageUrl, int order) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.imageUrl = imageUrl;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumItemDTO that = (AlbumItemDTO) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (order != that.order) return false;
        if (!text.equals(that.text)) return false;
        return imageUrl.equals(that.imageUrl);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type;
        result = 31 * result + text.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + order;
        return result;
    }

    @Override
    public String toString() {
        return "AlbumItemDTO{" +
                "id=" + id +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", order=" + order +
                '}';
    }
}
