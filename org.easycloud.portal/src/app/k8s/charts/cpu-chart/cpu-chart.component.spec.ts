import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CpuChartComponent } from './cpu-chart.component';

describe('CpuChartComponent', () => {
  let component: CpuChartComponent;
  let fixture: ComponentFixture<CpuChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CpuChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CpuChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
