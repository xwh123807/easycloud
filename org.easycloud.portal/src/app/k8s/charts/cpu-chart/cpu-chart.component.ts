import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-cpu-chart',
  templateUrl: './cpu-chart.component.html',
  styleUrls: ['./cpu-chart.component.scss']
})
export class CpuChartComponent implements OnInit {
  @Input() metrics: any;

  constructor() { }

  ngOnInit() {
  }

  // lineChart
  //  public lineChartData: Array<any> = [
  //    { data: [18, 48, 77, 9, 100, 27, 40], label: 'Series C' }
  //  ];
  //  public lineChartLabels: Array<any> = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];

  public getChartLabels(): Array<any> {
    let items = [];
    this.metrics.dataPoints.map(item => {
      items.push(item.x);
    });
    return items;
  }

  public getChartDatas(): Array<any> {
    let items = [];
    this.metrics.dataPoints.map(item => {
      items.push(item.y);
    });
    return [{ data: items, label: this.metrics.metricName }];
  }
}
