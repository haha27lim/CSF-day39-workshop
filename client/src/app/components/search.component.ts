import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy{
  form!: FormGroup;
  charName?: string; // stores character name from search form

  constructor(private formBuilder: FormBuilder, private router: Router){}

  // create form group for search form
  ngOnInit(): void {
    this.form = this.createForm();
  }

  ngOnDestroy(): void {
  }

  // navigate to character list page with search query parameter
  search() {
    const charName = this.form?.value['charName']; // get character name from form group
    this.router.navigate(['/list', charName]); // navigate to character list page with search query parameter
  }

  // create form group for search form
  private createForm(): FormGroup{
    return this.formBuilder.group({
      charName : this.formBuilder.control(''),  
    })
  }
}
