import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniButtonComponent } from './ani-button.component';

describe('AniButtonComponent', () => {
  let component: AniButtonComponent;
  let fixture: ComponentFixture<AniButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
