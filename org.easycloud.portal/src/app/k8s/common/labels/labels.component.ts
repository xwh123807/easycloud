import { Component, OnInit, Input, Output } from '@angular/core';

@Component({
  selector: 'app-labels',
  templateUrl: './labels.component.html',
  styleUrls: ['./labels.component.scss']
})
export class LabelsComponent implements OnInit {
  /**
   * {k:v, k: v}
   */
  @Input() labels: any;

  @Output() values = [];

  constructor() { }

  ngOnInit() {
    if (this.labels) {
      const props: Array<string> = Object.getOwnPropertyNames(this.labels);
      props.map(name => {
        this.values.push(name + ': ' + this.labels[name]);
      });
    }
  }

}
