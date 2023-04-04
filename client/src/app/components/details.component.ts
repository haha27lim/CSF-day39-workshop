import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from '../models/character';
import { MarvelCharService } from '../services/marvel-char.service';
import { Comment } from '../models/comment';
import { WebShareService } from '../webshare/web-share.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit, OnDestroy{

  charId =  ""; // initialize to an empty string
  param$! :  Subscription;
  character! : Character;
  comments!: Comment[];

  constructor(private activatedRoute: ActivatedRoute, 
    private marvelCharSvc: MarvelCharService, private router: Router
      , private webshareSvc: WebShareService ){}

  // subscribe to activated route parameters and retrieve character details and comments
  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
       async (params) => {
        // get character id from activated route parameters
        this.charId = params['charId'];
        console.log(this.charId);
        // get character details from MarvelCharService
        const l = await this.marvelCharSvc.getCharactersDetails(this.charId);
        // store character details in class property
        this.character = l.details;
        // get comments from MarvelCharService
        const ll = await this.marvelCharSvc.getCharComments(this.charId);
        console.log(ll);
        // store comments in class property
        this.comments = ll;        
      }
    );
  }

  // navigate to comment page with query parameters containing character name and id
  addComent(){
    const queryParams: Params = { charParam: this.character['name'] + '|' + this.character.id };
    this.router.navigate(['/comment'], {queryParams : queryParams})
  }

  // share current page using WebShareService
  share(){
    this.webshareSvc.share({
      title: 'Marvel App',
      text: 'Hey check out my Share',
      url: 'https://developers.google.com/web'
    });
  }

  // unsubscribe from the subscription when the component is destroyed
  ngOnDestroy(): void{
    this.param$.unsubscribe();
  }

}
