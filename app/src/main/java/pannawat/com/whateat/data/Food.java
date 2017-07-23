package pannawat.com.whateat.data;

/**
 * Created by Pannawat on 23/07/2017.
 */

public class Food {
    private String name;
    private String image;
    private String location;
    private String type;
    private String cal;

    public Food(String name, String image, String location, String cal, String type) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.cal = cal;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }
}
