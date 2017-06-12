import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlusDeployComponent } from './plus-deploy.component';

describe('PlusDeployComponent', () => {
  let component: PlusDeployComponent;
  let fixture: ComponentFixture<PlusDeployComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlusDeployComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlusDeployComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
