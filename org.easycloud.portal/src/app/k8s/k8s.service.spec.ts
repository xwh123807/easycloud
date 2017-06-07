import { TestBed, inject } from '@angular/core/testing';

import { K8sService } from './k8s.service';

describe('K8sService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [K8sService]
    });
  });

  it('should be created', inject([K8sService], (service: K8sService) => {
    expect(service).toBeTruthy();
  }));
});
