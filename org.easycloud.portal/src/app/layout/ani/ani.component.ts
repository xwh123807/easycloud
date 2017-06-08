import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ani',
  templateUrl: './ani.component.html',
  styleUrls: ['./ani.component.scss']
})
export class AniComponent implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
    this.router.navigate(['/ani/icons']);
  }

}
