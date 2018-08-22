package buildnlive.com.buildlive.elements;

public class Packet {
    String name;
    String extra;
    int type;

    public Packet(String name, String extra, int type) {
        this.name = name;
        this.extra = extra;
        this.type = type;
    }

    public Packet(String name, String extra) {
        this.name = name;
        this.extra = extra;
    }

    public Packet() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
