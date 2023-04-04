import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MarvelCharService } from '../services/marvel-char.service';
import { Comment } from '../models/comment';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit, OnDestroy{
  form!: FormGroup;
  queryParams$! :  Subscription; // Define a Subscription instance variable

  // Define some variables for the character ID and name
  charParam!: any;
  charName! : string;
  charId!: string;

  constructor(private activatedRoute: ActivatedRoute,  private formBuilder: FormBuilder,
    private marvelCharSvc: MarvelCharService, private router: Router){}

  ngOnInit(): void {
     // Create and initialize the FormGroup instance variable
    this.form = this.createForm();
    // Subscribe to the activatedRoute query parameters
    this.queryParams$ = this.activatedRoute.queryParams.subscribe(
      (queryParams) => {
        // Split the charParam query parameter and store the parts in the appropriate variables
        this.charParam = queryParams['charParam'].split('|');
        console.log(this.charParam[0]);
        console.log(this.charParam[1]);
        this.charName = this.charParam[0];
        this.charId = this.charParam[1];
      }
    );
    
  }

  saveComment(){
    // Get the comment value from the form
    const commentFormVal = this.form?.value['comment'];
    // Create a new Comment object with the comment value and character ID
    const c = {} as Comment;
    c.comment = commentFormVal;
    c.id = this.charId;

    // Save the comment using the MarvelCharService
    this.marvelCharSvc.saveComment(c);
    // Navigate back to the character details page
    this.router.navigate(['/details', this.charId]); 
  }

  cancel(){
    // Navigate back to the character details page
    this.router.navigate(['/details', this.charId]);
  }

  ngOnDestroy(): void{
    // Unsubscribe from the activatedRoute query parameters when the component is destroyed
    this.queryParams$.unsubscribe();
  }

  // Define a private method to create the FormGroup instance variable
  private createForm(): FormGroup{
    return this.formBuilder.group({
      comment : this.formBuilder.control(''),  
    })
  }
}
