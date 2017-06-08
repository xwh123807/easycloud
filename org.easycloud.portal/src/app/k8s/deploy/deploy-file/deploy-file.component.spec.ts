import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeployFileComponent } from './deploy-file.component';

describe('DeployFileComponent', () => {
  let component: DeployFileComponent;
  let fixture: ComponentFixture<DeployFileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeployFileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeployFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
