docker pull dockerhub.ygsoft.com:5000/wordpress:4.8.0-apache
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-proxy-amd64:v1.7.0
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-apiserver-amd64:v1.7.0
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-controller-manager-amd64:v1.7.0
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-scheduler-amd64:v1.7.0
docker pull dockerhub.ygsoft.com:5000/weaveworks/weave-npc:2.0.1
docker pull dockerhub.ygsoft.com:5000/weaveworks/weave-kube:2.0.1
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.4.0
docker pull dockerhub.ygsoft.com:5000/google_containers/k8s-dns-sidecar-amd64:1.14.4
docker pull dockerhub.ygsoft.com:5000/google_containers/k8s-dns-kube-dns-amd64:1.14.4
docker pull dockerhub.ygsoft.com:5000/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.4
docker pull dockerhub.ygsoft.com:5000/mysql:5.7.18
docker pull dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.9.0-beta.8
docker pull dockerhub.ygsoft.com:5000/google_containers/fluentd-elasticsearch:1.23
docker pull dockerhub.ygsoft.com:5000/google_containers/kubernetes-dashboard-amd64:v1.6.1
docker pull dockerhub.ygsoft.com:5000/google_containers/elasticsearch:v2.4.1-2
docker pull dockerhub.ygsoft.com:5000/google_containers/etcd-amd64:3.0.17
docker pull dockerhub.ygsoft.com:5000/google_containers/addon-resizer:1.7
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.0.2
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-influxdb-amd64:v1.1.1
docker pull dockerhub.ygsoft.com:5000/google_containers/kibana:v4.6.1-1
docker pull dockerhub.ygsoft.com:5000/google_containers/pause-amd64:3.0
docker pull dockerhub.ygsoft.com:5000/google_containers/defaultbackend:1.0


docker tag dockerhub.ygsoft.com:5000/wordpress:4.8.0-apache                                   wordpress:4.8.0-apache
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-proxy-amd64:v1.7.0                gcr.io/google_containers/kube-proxy-amd64:v1.7.0
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-apiserver-amd64:v1.7.0            gcr.io/google_containers/kube-apiserver-amd64:v1.7.0
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-controller-manager-amd64:v1.7.0   gcr.io/google_containers/kube-controller-manager-amd64:v1.7.0
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-scheduler-amd64:v1.7.0            gcr.io/google_containers/kube-scheduler-amd64:v1.7.0
docker tag dockerhub.ygsoft.com:5000/weaveworks/weave-npc:2.0.1                               weaveworks/weave-npc:2.0.1
docker tag dockerhub.ygsoft.com:5000/weaveworks/weave-kube:2.0.1                              weaveworks/weave-kube:2.0.1
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.4.0                  gcr.io/google_containers/heapster-amd64:v1.4.0
docker tag dockerhub.ygsoft.com:5000/google_containers/k8s-dns-sidecar-amd64:1.14.4           gcr.io/google_containers/k8s-dns-sidecar-amd64:1.14.4
docker tag dockerhub.ygsoft.com:5000/google_containers/k8s-dns-kube-dns-amd64:1.14.4          gcr.io/google_containers/k8s-dns-kube-dns-amd64:1.14.4
docker tag dockerhub.ygsoft.com:5000/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.4     gcr.io/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.4
docker tag dockerhub.ygsoft.com:5000/mysql:5.7.18                                             mysql:5.7.18
docker tag dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.9.0-beta.8  gcr.io/google_containers/nginx-ingress-controller:0.9.0-beta.8
docker tag dockerhub.ygsoft.com:5000/google_containers/fluentd-elasticsearch:1.23             gcr.io/google_containers/fluentd-elasticsearch:1.23
docker tag dockerhub.ygsoft.com:5000/google_containers/kubernetes-dashboard-amd64:v1.6.1      gcr.io/google_containers/kubernetes-dashboard-amd64:v1.6.1
docker tag dockerhub.ygsoft.com:5000/google_containers/elasticsearch:v2.4.1-2                 gcr.io/google_containers/elasticsearch:v2.4.1-2
docker tag dockerhub.ygsoft.com:5000/google_containers/etcd-amd64:3.0.17                      gcr.io/google_containers/etcd-amd64:3.0.17
docker tag dockerhub.ygsoft.com:5000/google_containers/addon-resizer:1.7                      gcr.io/google_containers/addon-resizer:1.7
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.0.2          gcr.io/google_containers/heapster-grafana-amd64:v4.0.2
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-influxdb-amd64:v1.1.1         gcr.io/google_containers/heapster-influxdb-amd64:v1.1.1
docker tag dockerhub.ygsoft.com:5000/google_containers/kibana:v4.6.1-1                        gcr.io/google_containers/kibana:v4.6.1-1
docker tag dockerhub.ygsoft.com:5000/google_containers/pause-amd64:3.0                        gcr.io/google_containers/pause-amd64:3.0
docker tag dockerhub.ygsoft.com:5000/google_containers/defaultbackend:1.0                     gcr.io/google_containers/defaultbackend:1.0


