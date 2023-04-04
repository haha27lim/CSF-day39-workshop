import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { Character } from '../models/character';
import { Comment } from '../models/comment';


@Injectable({
  providedIn: 'root'
})
export class MarvelCharService {
  // URL of the Marvel character API
  private API_URI: string = "/api/characters";

  // inject HttpClient service
  constructor(private httpClient: HttpClient) {}

  // retrieve characters from Marvel character API
  getCharacters(charName: string, offset: number, limit: number): Promise<any>{
    // create HttpParams object with query parameters
    const params = new HttpParams()
        .set("characterName", charName)
        .set("limit", limit)
        .set("offset", offset);

    // set headers for request
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    // send GET request to API with query parameters and headers
    return lastValueFrom(this.httpClient
        .get<Character[]>(this.API_URI, {params: params, headers: headers}));
  }

  // retrieve details for a specific character from Marvel character API
  getCharactersDetails(charId: string): Promise<any>{
    // set headers for request
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    // send GET request to API with headers
    return lastValueFrom(this.httpClient
        .get<Character[]>(this.API_URI+"/" + charId, {headers: headers}));
  }

  // save a comment for a specific character to the Marvel character API
  saveComment(c:Comment) : Promise<any>{
    // set headers for request
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    const body=JSON.stringify(c);
    console.log("save comment !");
    // send POST request to API with headers and comment object as request body
    return lastValueFrom(this.httpClient.post<Comment>(this.API_URI+"/" + c.id, body, {headers: headers}));
  }

  // retrieve comments for a specific character from Marvel character API
  getCharComments(charId: string): Promise<Comment[]>{
    // set headers for request
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');
    console.log("get all comments !");
    // send GET request to API with headers
    return lastValueFrom(this.httpClient
        .get<Comment[]>(this.API_URI+"/comments/" + charId, {headers: headers}));
  }
}
