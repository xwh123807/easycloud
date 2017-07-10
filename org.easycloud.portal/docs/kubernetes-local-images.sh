docker tag wordpress:4.8.0-apache                                            dockerhub.ygsoft.com:5000/wordpress:4.8.0-apache
docker tag gcr.io/google_containers/kube-proxy-amd64:v1.7.0                  dockerhub.ygsoft.com:5000/google_containers/kube-proxy-amd64:v1.7.0
docker tag gcr.io/google_containers/kube-apiserver-amd64:v1.7.0              dockerhub.ygsoft.com:5000/google_containers/kube-apiserver-amd64:v1.7.0
docker tag gcr.io/google_containers/kube-controller-manager-amd64:v1.7.0     dockerhub.ygsoft.com:5000/google_containers/kube-controller-manager-amd64:v1.7.0
docker tag gcr.io/google_containers/kube-scheduler-amd64:v1.7.0              dockerhub.ygsoft.com:5000/google_containers/kube-scheduler-amd64:v1.7.0
docker tag weaveworks/weave-npc:2.0.1                                        dockerhub.ygsoft.com:5000/weaveworks/weave-npc:2.0.1
docker tag weaveworks/weave-kube:2.0.1                                       dockerhub.ygsoft.com:5000/weaveworks/weave-kube:2.0.1
docker tag gcr.io/google_containers/heapster-amd64:v1.4.0                    dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.4.0
docker tag gcr.io/google_containers/k8s-dns-sidecar-amd64:1.14.4             dockerhub.ygsoft.com:5000/google_containers/k8s-dns-sidecar-amd64:1.14.4
docker tag gcr.io/google_containers/k8s-dns-kube-dns-amd64:1.14.4            dockerhub.ygsoft.com:5000/google_containers/k8s-dns-kube-dns-amd64:1.14.4
docker tag gcr.io/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.4       dockerhub.ygsoft.com:5000/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.4
docker tag mysql:5.7.18                                                      dockerhub.ygsoft.com:5000/mysql:5.7.18
docker tag gcr.io/google_containers/nginx-ingress-controller:0.9.0-beta.8    dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.9.0-beta.8
docker tag gcr.io/google_containers/fluentd-elasticsearch:1.23               dockerhub.ygsoft.com:5000/google_containers/fluentd-elasticsearch:1.23
docker tag gcr.io/google_containers/kubernetes-dashboard-amd64:v1.6.1        dockerhub.ygsoft.com:5000/google_containers/kubernetes-dashboard-amd64:v1.6.1
docker tag gcr.io/google_containers/elasticsearch:v2.4.1-2                   dockerhub.ygsoft.com:5000/google_containers/elasticsearch:v2.4.1-2
docker tag gcr.io/google_containers/etcd-amd64:3.0.17                        dockerhub.ygsoft.com:5000/google_containers/etcd-amd64:3.0.17
docker tag gcr.io/google_containers/addon-resizer:1.7                        dockerhub.ygsoft.com:5000/google_containers/addon-resizer:1.7
docker tag gcr.io/google_containers/heapster-grafana-amd64:v4.0.2            dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.0.2
docker tag gcr.io/google_containers/heapster-influxdb-amd64:v1.1.1           dockerhub.ygsoft.com:5000/google_containers/heapster-influxdb-amd64:v1.1.1
docker tag gcr.io/google_containers/kibana:v4.6.1-1                          dockerhub.ygsoft.com:5000/google_containers/kibana:v4.6.1-1
docker tag gcr.io/google_containers/pause-amd64:3.0                          dockerhub.ygsoft.com:5000/google_containers/pause-amd64:3.0
docker tag gcr.io/google_containers/defaultbackend:1.0                       dockerhub.ygsoft.com:5000/google_containers/defaultbackend:1.0

docker push dockerhub.ygsoft.com:5000/wordpress:4.8.0-apache
docker push dockerhub.ygsoft.com:5000/google_containers/kube-proxy-amd64:v1.7.0
docker push dockerhub.ygsoft.com:5000/google_containers/kube-apiserver-amd64:v1.7.0
docker push dockerhub.ygsoft.com:5000/google_containers/kube-controller-manager-amd64:v1.7.0
docker push dockerhub.ygsoft.com:5000/google_containers/kube-scheduler-amd64:v1.7.0
docker push dockerhub.ygsoft.com:5000/weaveworks/weave-npc:2.0.1
docker push dockerhub.ygsoft.com:5000/weaveworks/weave-kube:2.0.1
docker push dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.4.0
docker push dockerhub.ygsoft.com:5000/google_containers/k8s-dns-sidecar-amd64:1.14.4
docker push dockerhub.ygsoft.com:5000/google_containers/k8s-dns-kube-dns-amd64:1.14.4
docker push dockerhub.ygsoft.com:5000/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.4
docker push dockerhub.ygsoft.com:5000/mysql:5.7.18
docker push dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.9.0-beta.8
docker push dockerhub.ygsoft.com:5000/google_containers/fluentd-elasticsearch:1.23
docker push dockerhub.ygsoft.com:5000/google_containers/kubernetes-dashboard-amd64:v1.6.1
docker push dockerhub.ygsoft.com:5000/google_containers/elasticsearch:v2.4.1-2
docker push dockerhub.ygsoft.com:5000/google_containers/etcd-amd64:3.0.17
docker push dockerhub.ygsoft.com:5000/google_containers/addon-resizer:1.7
docker push dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.0.2
docker push dockerhub.ygsoft.com:5000/google_containers/heapster-influxdb-amd64:v1.1.1
docker push dockerhub.ygsoft.com:5000/google_containers/kibana:v4.6.1-1
docker push dockerhub.ygsoft.com:5000/google_containers/pause-amd64:3.0
docker push dockerhub.ygsoft.com:5000/google_containers/defaultbackend:1.0

