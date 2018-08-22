package buildnlive.com.buildlive.elements;

import buildnlive.com.buildlive.App;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Plans extends RealmObject {

    @PrimaryKey
    @Index
    private String name;
    private String id;
    private String description;
    private String url;
    @Index
    private String belongsTo;

    public Plans() {
    }

    public Plans(String id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.belongsTo = App.belongsTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belongsTo) {
        this.belongsTo = belongsTo;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
