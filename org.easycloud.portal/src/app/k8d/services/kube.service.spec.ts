import { TestBed, inject } from '@angular/core/testing';

import { KubeService } from './kube.service';

describe('KubeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [KubeService]
    });
  });

  it('should be created', inject([KubeService], (service: KubeService) => {
    expect(service).toBeTruthy();
  }));
});
