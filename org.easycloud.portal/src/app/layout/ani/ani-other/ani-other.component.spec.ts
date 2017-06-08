import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniOtherComponent } from './ani-other.component';

describe('AniOtherComponent', () => {
  let component: AniOtherComponent;
  let fixture: ComponentFixture<AniOtherComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniOtherComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniOtherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
