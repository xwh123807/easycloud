import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniAlertsComponent } from './ani-alerts.component';

describe('AniAlertsComponent', () => {
  let component: AniAlertsComponent;
  let fixture: ComponentFixture<AniAlertsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniAlertsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniAlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
