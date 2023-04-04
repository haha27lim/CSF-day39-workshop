package sg.edu.nus.iss.app.server.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.app.server.model.Comment;
import sg.edu.nus.iss.app.server.model.MarvelCharacter;
import sg.edu.nus.iss.app.server.repository.CharacterRepository;

@Service
public class CharacterService {
    @Autowired
    private MarvelApiService marvelApiSvc;

    @Autowired
    private CharacterRepository charRepo;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
 
    // get a list of Marvel characters
    public Optional<List<MarvelCharacter>> getCharacters(String characterName,
            Integer limit , Integer offset){
        return marvelApiSvc.getCharacters(characterName, limit, offset);
    }

     // get details for a specific Marvel character
    public MarvelCharacter getCharacterDetails(String characterId) 
            throws IOException{
        MarvelCharacter cc = null;
        // check if character details exist in cache
        String detailsJson = (String)redisTemplate.opsForValue().get(characterId);
        System.out.println("" + detailsJson);
        if(detailsJson != null){
            cc = (MarvelCharacter) MarvelCharacter.createForCache(detailsJson);
        }else{
            // get character details from Marvel API
            Optional<MarvelCharacter> c  = marvelApiSvc.getCharacterDetails(characterId);
            cc = c.get();
            cc.setComments(this.getAllComments(characterId));
            if(detailsJson == null){
                // store character details in cache
                redisTemplate.opsForValue().set(characterId,cc.toJSON().toString());
                // set expiration time for cache
                long currentTime = Instant.now().getMillis();
                // add 60 mins to the current time.
                Date afterAdding60Mins = new Date(currentTime + (60 * 60 * 1000));
                redisTemplate.expireAt(characterId,afterAdding60Mins);
            }               
        }       
        return cc;
    }

     // insert a comment for a specific character
    public Comment insertComment(Comment r){
        return charRepo.insertComment(r);
    }

    // get all comments for a specific character
    public List<Comment> getAllComments(String charId){
        return charRepo.getAllComments(charId);
    }
}
