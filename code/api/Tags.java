package api;

import java.util.ArrayList;

public class Tags {

    private ArrayList<String> tags;

    public Tags(){}

    public Tags(ArrayList<String> tags){
        this.tags = tags;

    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
