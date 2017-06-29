import {Component, Input, OnInit, Output} from '@angular/core';

@Component({
    selector: 'app-status',
    templateUrl: './status.component.html',
    styleUrls: ['./status.component.scss']
})
export class StatusComponent implements OnInit {
    @Input() status: any[];

    constructor() {
    }

    ngOnInit() {
    }

}
