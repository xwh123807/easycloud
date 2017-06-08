import { TestBed, inject } from '@angular/core/testing';

import { DockerHubService } from './docker-hub.service';

describe('DockerHubService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DockerHubService]
    });
  });

  it('should be created', inject([DockerHubService], (service: DockerHubService) => {
    expect(service).toBeTruthy();
  }));
});
