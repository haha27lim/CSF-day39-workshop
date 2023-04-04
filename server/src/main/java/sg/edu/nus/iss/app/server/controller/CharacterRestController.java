package sg.edu.nus.iss.app.server.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.app.server.services.CharacterService;
import sg.edu.nus.iss.app.server.model.Comment;
import sg.edu.nus.iss.app.server.model.MarvelCharacter;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(path="/api/characters", consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CharacterRestController {
    private Logger logger = LoggerFactory.getLogger(CharacterRestController.class);
    
    @Autowired
    private CharacterService charSvc;
    
    // define endpoint for getting characters and return a JSON response
    @GetMapping
    public ResponseEntity<String> getCharacters(
        @RequestParam(required=true) String characterName,
        @RequestParam(required=true) Integer limit,
        @RequestParam(required=true) Integer offset) {

        // initialize JSON array for storing results    
        JsonArray result = null;
        // call service to get characters
        Optional<List<MarvelCharacter>> or = this.charSvc.getCharacters(characterName, limit, offset);
        // get results from optional object
        List<MarvelCharacter> results = or.get(); 
        // initialize JSON array builder
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (MarvelCharacter mc : results)
            arrBuilder.add(mc.toJSON()); // add each character to JSON array
        // build final JSON array    
        result = arrBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString()); // return JSON response with characters array as string
    }

    // define endpoint for getting character details and return a JSON response
    @GetMapping(path="/{charId}")
    public ResponseEntity<String> getCharacterDetails(@PathVariable(required=true) String charId) throws IOException {
        // call service to get character details
        MarvelCharacter c = this.charSvc.getCharacterDetails(charId);
        // initialize JSON object builder
        JsonObjectBuilder ocjBuilder = Json.createObjectBuilder();
        // add character details to JSON object
        ocjBuilder.add("details" , c.toJSON());
        // build final JSON object
        JsonObject result = ocjBuilder.build();
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString()); // return JSON response with character details object as string
    }

    // define endpoint for saving character comments and return a JSON response
    @PostMapping(path="/{charId}")
    public ResponseEntity<String> saveCharacterComment(
        @RequestBody Comment comment, @PathVariable(required=true) String charId) {
        logger.info("save comment > : " + charId);
        // create new comment object
        Comment c= new Comment();
        // set comment text
        c.setComment(comment.getComment());
        // set character ID
        c.setCharId(charId);
        // call service to save comment
        Comment r = this.charSvc.insertComment(c);
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(r.toJSON().toString()); // return JSON response with saved comment object as string
    }

    // define endpoint for getting all comments for a character and return a JSON response
    @GetMapping(path="/comments/{charId}")
    public ResponseEntity<String> getCharComments(@PathVariable(required=true) String charId) {
        logger.info("Get All ...comments");
        // call service to get comments for character
        List<Comment> aa = this.charSvc.getAllComments(charId);
        // initialize JSON array builder
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Comment c : aa)
            arrBuilder.add(c.toJSON()); // add each comment to JSON array
        JsonArray result = arrBuilder.build(); // build final JSON array
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }

}
