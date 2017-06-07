import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { K8sSdiebarComponent } from './k8s-sdiebar.component';

describe('K8sSdiebarComponent', () => {
  let component: K8sSdiebarComponent;
  let fixture: ComponentFixture<K8sSdiebarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ K8sSdiebarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(K8sSdiebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
