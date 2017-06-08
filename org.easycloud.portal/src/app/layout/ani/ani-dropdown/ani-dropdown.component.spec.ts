import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniDropdownComponent } from './ani-dropdown.component';

describe('AniDropdownComponent', () => {
  let component: AniDropdownComponent;
  let fixture: ComponentFixture<AniDropdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniDropdownComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
