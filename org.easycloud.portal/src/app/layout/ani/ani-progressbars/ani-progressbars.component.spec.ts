import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniProgressbarsComponent } from './ani-progressbars.component';

describe('AniProgressbarsComponent', () => {
  let component: AniProgressbarsComponent;
  let fixture: ComponentFixture<AniProgressbarsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniProgressbarsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniProgressbarsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
