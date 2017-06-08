import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniIconsComponent } from './ani-icons.component';

describe('AniIconsComponent', () => {
  let component: AniIconsComponent;
  let fixture: ComponentFixture<AniIconsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniIconsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniIconsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
