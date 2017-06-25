import {Component, OnInit} from '@angular/core';
import {Template, TemplateService} from '../../services/template.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
    selector: 'app-template-list',
    templateUrl: './template-list.component.html',
    styleUrls: ['./template-list.component.scss']
})
export class TemplateListComponent implements OnInit {
    templates: Template[];

    constructor(private service: TemplateService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    ngOnInit() {
        this.service.getTemplates().subscribe(templates => this.templates = templates);
    }

    view(name: string): void {
        this.router.navigate(['../', name], {relativeTo: this.route});
    }
}
