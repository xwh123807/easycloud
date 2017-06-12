import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectNamespaceComponent } from './select-namespace.component';

describe('SelectNamespaceComponent', () => {
  let component: SelectNamespaceComponent;
  let fixture: ComponentFixture<SelectNamespaceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectNamespaceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectNamespaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
