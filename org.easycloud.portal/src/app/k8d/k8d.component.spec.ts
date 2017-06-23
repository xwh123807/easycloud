import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { K8dComponent } from './k8d.component';

describe('K8dComponent', () => {
  let component: K8dComponent;
  let fixture: ComponentFixture<K8dComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ K8dComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(K8dComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
