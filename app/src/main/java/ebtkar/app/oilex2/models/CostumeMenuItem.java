package ebtkar.app.oilex2.models;

import android.view.View;

/**
 * Created by Luminance on 6/27/2018.
 */

public abstract class CostumeMenuItem {
    private String id , title;
    private int icon;

    public CostumeMenuItem(String id, String title, int icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

 public   abstract void doAction(View v);

}
