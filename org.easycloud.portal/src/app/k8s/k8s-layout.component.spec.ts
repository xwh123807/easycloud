import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { K8sLayoutComponent } from './k8s-layout.component';

describe('K8sLayoutComponent', () => {
  let component: K8sLayoutComponent;
  let fixture: ComponentFixture<K8sLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ K8sLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(K8sLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
