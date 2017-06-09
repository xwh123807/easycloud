import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StatuComponent } from './statu.component';

describe('StatuComponent', () => {
  let component: StatuComponent;
  let fixture: ComponentFixture<StatuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StatuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
