import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniPanelsComponent } from './ani-panels.component';

describe('AniPanelsComponent', () => {
  let component: AniPanelsComponent;
  let fixture: ComponentFixture<AniPanelsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniPanelsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniPanelsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
