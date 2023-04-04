package sg.edu.nus.iss.app.server.model;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Comment {
    private String charId;
    private String comment;

    public String getCharId() {
        return charId;
    }
    public void setCharId(String id) {
        this.charId = id;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    // This method creates a new Comment object from the given Document object.
    public static Comment create(Document d) {
        Comment c = new Comment();
        c.setCharId(d.getObjectId("charId").toString()); // Set the character ID for the comment.
        c.setComment(d.getString("comment")); // Set the comment text.
        return c;
    }

    // This method converts the Comment object to a JSON object.
    public JsonObject toJSON() {
        return Json.createObjectBuilder()
            .add("charId", getCharId()) // Add the character ID property to the JSON object.
            .add("comment", getComment()) // Add the comment text property to the JSON object.
            .build();
    }
    
}
