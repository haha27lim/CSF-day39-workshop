package sg.edu.nus.iss.app.server.services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.edu.nus.iss.app.server.model.MarvelCharacter;

@Service
public class MarvelApiService {

    @Value("${workshop39.marvel.api.url}")
    private String marvelapiUrl;

    @Value("${workshop39.marvel.api.key}")
    private String marvelPublicApiKey;

    @Value("${workshop39.marvel.priv.key}")
    private String marvelPrivateApiKey;

    // method to generate a hash for Marvel API authentication
    private String[] getMarvelApiHash(){
        String [] result = new String[2];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long tsVal = timestamp.getTime();
        String hashVal = tsVal + marvelPrivateApiKey + marvelPublicApiKey;
        result[0] = tsVal+"";
        result[1] = DigestUtils.md5Hex(hashVal);
        return result;
    }

    // method to get a list of Marvel characters
    public Optional<List<MarvelCharacter>> getCharacters(String characterName,
            Integer limit , Integer offset){
        ResponseEntity<String> resp = null;
        List<MarvelCharacter> c = null;
        System.out.println(marvelPublicApiKey); // print the public key for debugging purposes
        // get a hash for authentication
        String[] r = getMarvelApiHash();
        // print the timestamp and hash values for debugging purposes
        System.out.println(r[0]);
        System.out.println(r[1]);
        
        // build the Marvel API URL with the required parameters
        String marvelCharApiUrl = UriComponentsBuilder
                                    .fromUriString(marvelapiUrl + "characters")
                                    .queryParam("ts", r[0].trim())
                                    .queryParam("apikey", marvelPublicApiKey.trim())
                                    .queryParam("hash", r[1])
                                    .queryParam("nameStartsWith", 
                                        characterName.replaceAll(" ", "+"))
                                    .queryParam("limit", limit)
                                    .queryParam("offset", offset)
                                    .toUriString();
        // print the API URL for debugging purposes                            
        System.out.println(marvelCharApiUrl);
        // use RestTemplate to make a GET request to the Marvel API
        RestTemplate template = new RestTemplate();
        resp = template.getForEntity(marvelCharApiUrl,String.class);
        // print the response for debugging purposes
        System.out.println(resp);
        try {
            // create a list of MarvelCharacter objects from the JSON response
            c = MarvelCharacter.create(resp.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // print the list of MarvelCharacter objects for debugging purposes
        System.out.println(c);
        if(c != null)
            return Optional.of(c);                        
        return Optional.empty();
    }

    // method to get details of a specific Marvel character
    public Optional<MarvelCharacter> getCharacterDetails(String characterId) 
            throws IOException{
        ResponseEntity<String> resp = null;
        MarvelCharacter c = null;
        // print the public key for debugging purposes
        System.out.println(marvelPublicApiKey);
        // get a hash for authentication
        String[] r = getMarvelApiHash();
         // print the timestamp and hash values for debugging purposes
        System.out.println(r[0]);
        System.out.println(r[1]);
        
        // build the Marvel API URL with the required parameters
        String marvelCharApiUrl = UriComponentsBuilder
                                    .fromUriString(marvelapiUrl + "characters/" + characterId)
                                    .queryParam("ts", r[0].trim())
                                    .queryParam("apikey", marvelPublicApiKey.trim())
                                    .queryParam("hash", r[1])
                                    .toUriString();
        System.out.println(marvelCharApiUrl);
        // create a new RestTemplate and send a GET request to the Marvel API URL
        RestTemplate template = new RestTemplate();
        resp = template.getForEntity(marvelCharApiUrl,String.class);
        // print the response for debugging purposes
        System.out.println(resp);
        // create a new MarvelCharacter object from the response body
        List<MarvelCharacter> cArr = MarvelCharacter.create(resp.getBody());
        c = cArr.get(0);
        // print the character object for debugging purposes
        System.out.println(c);
        // If the character is found, return it as an Optional        
        if(c != null)
            return Optional.of(c);   
        // If the character is not found, return an empty Optional                     
        return Optional.empty();
    }
    
}
