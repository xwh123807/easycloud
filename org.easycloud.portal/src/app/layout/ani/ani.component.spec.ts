import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniComponent } from './ani.component';

describe('AniComponent', () => {
  let component: AniComponent;
  let fixture: ComponentFixture<AniComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
