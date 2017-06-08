import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AniPaginationComponent } from './ani-pagination.component';

describe('AniPaginationComponent', () => {
  let component: AniPaginationComponent;
  let fixture: ComponentFixture<AniPaginationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AniPaginationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AniPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
