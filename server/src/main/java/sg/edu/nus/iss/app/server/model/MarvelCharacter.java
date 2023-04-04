package sg.edu.nus.iss.app.server.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class MarvelCharacter implements Serializable{
    private Integer id;
    private String name;
    private String desc;
    private String photo;
    private List<Comment> comments;
    
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // create a MarvelCharacter object from a JSON object
    public static MarvelCharacter createJson(JsonObject o){
        // Create new MarvelCharacter object
        MarvelCharacter c = new MarvelCharacter();
        // extract thumbnail image path and extension from JSON object
        JsonObject t = o.getJsonObject("thumbnail");
        String path = t.getString("path");
        String ext = t.getString("extension");
        // set MarvelCharacter object properties from JSON object
        c.id = o.getJsonNumber("id").intValue();
        c.name = o.getString("name");
        c.desc = o.getString("description");
        // combine path and extension to form photo URL
        c.photo = path + '.' + ext; 
        return c; // Return the created MarvelCharacter object
    }
    
    // create a list of MarvelCharacter objects from a JSON string
    public static List<MarvelCharacter> create(String json) throws IOException {
        // Create new LinkedList of MarvelCharacter objects
        List<MarvelCharacter> chars = new LinkedList<>();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){ // Convert "json" String to InputStream
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            JsonObject oo = o.getJsonObject("data");
            // if there are results in the data object, map them to MarvelCharacter objects
            if(oo.getJsonArray("results") != null)
                chars = oo.getJsonArray("results").stream()
                    .map(v-> (JsonObject)v)
                    .map(v-> MarvelCharacter.createJson(v))
                    .toList();  // map each JSON object to a MarvelCharacter object and add to list
        }
        return chars; // Return the List of created MarvelCharacter objects
    }

    // create a MarvelCharacter object from a JSON string for caching
    public static MarvelCharacter createForCache(String json) throws IOException {
        // create a new MarvelCharacter object
        MarvelCharacter c = new MarvelCharacter();
        // read JSON string as input stream
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            // set MarvelCharacter object properties from JSON object
            c.setId(o.getJsonNumber("id").intValue());
            c.setName(o.getString("name"));
            c.setDesc(o.getString("description"));
            c.setPhoto(o.getString("photo"));
        }
        return c; // return the MarvelCharacter object
    }

    // convert MarvelCharacter object to JSON object
    public JsonObject toJSON() {
        // build and return a JSON object with MarvelCharacter object properties
        return Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("description", getDesc())
                .add("photo", getPhoto())
                .build();
    }
    
}
