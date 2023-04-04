import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from '../models/character';
import { MarvelCharService } from '../services/marvel-char.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy{
  charName =  ""; // stores character name from activated route parameters
  param$! :  Subscription; // stores subscription for activated route parameters
  characters! : Character[]; // stores characters retrieved from MarvelCharService
  currentIndex!: number; // stores current index for retrieving characters from MarvelCharService

  constructor(private activatedRoute: ActivatedRoute, 
    private marvelCharSvc: MarvelCharService, private router: Router){}

  // subscribe to activated route parameters and retrieve characters from MarvelCharService  
  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
      async (params) => {
        // get character name from activated route parameters
        this.charName = params['charName'];
        console.log(this.charName);
        // get characters from MarvelCharService
        const l = await this.marvelCharSvc.getCharacters(this.charName, 0, 20);
        // set current index to 1
        this.currentIndex = 1;
        console.log(l);
        // if no characters are retrieved, navigate to home page
        if (l === undefined || l.length == 0) {
          this.router.navigate(['/'])
        }else{
            this.characters = l; // store characters in class property
        }  
      }
    );
  }

  // retrieve previous page of characters from MarvelCharService
  async previous(){
    console.log(this.currentIndex);
    if(this.currentIndex > 0){
      // increment current index by 20
      this.currentIndex = this.currentIndex + 20;
      const l = await this.marvelCharSvc
            .getCharacters(this.charName, this.currentIndex, 20); // get characters from MarvelCharService
      // store characters in class property
      this.characters = l; 
    }
  }

  // retrieve next page of characters from MarvelCharService
  async next(){
    console.log(this.currentIndex);
    // increment current index by 20
    this.currentIndex = this.currentIndex + 20;
    const l = await this.marvelCharSvc
          .getCharacters(this.charName, this.currentIndex, 20); // get characters from MarvelCharService
    this.characters = l;
  }

  // unsubscribe from activated route parameters subscription to prevent memory leaks
  ngOnDestroy(): void{
    console.log("destroy sub");
    this.param$.unsubscribe();
  }
}
