docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.2.0
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-proxy-amd64:v1.6.6
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-apiserver-amd64:v1.6.6
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-controller-manager-amd64:v1.6.6
docker pull dockerhub.ygsoft.com:5000/google_containers/kube-scheduler-amd64:v1.6.6
docker pull dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.9.0-beta.8
docker pull dockerhub.ygsoft.com:5000/google_containers/kubernetes-dashboard-amd64:v1.6.1
docker pull dockerhub.ygsoft.com:5000/google_containers/k8s-dns-sidecar-amd64:1.14.2
docker pull dockerhub.ygsoft.com:5000/google_containers/k8s-dns-kube-dns-amd64:1.14.2
docker pull dockerhub.ygsoft.com:5000/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.2
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.3.0
docker pull dockerhub.ygsoft.com:5000/google_containers/elasticsearch:v2.4.1-2
docker pull dockerhub.ygsoft.com:5000/google_containers/etcd-amd64:3.0.17
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.0.2
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-influxdb-amd64:v1.1.1
docker pull dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.3.0-beta.1
docker pull dockerhub.ygsoft.com:5000/google_containers/kibana:v4.6.1-1
docker pull dockerhub.ygsoft.com:5000/google_containers/fluentd-elasticsearch:1.22
docker pull dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.8.3
docker pull dockerhub.ygsoft.com:5000/google_containers/pause-amd64:3.0
docker pull dockerhub.ygsoft.com:5000/google_containers/defaultbackend:1.0

docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.2.0          gcr.io/google_containers/heapster-grafana-amd64:v4.2.0
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-proxy-amd64:v1.6.6                gcr.io/google_containers/kube-proxy-amd64:v1.6.6
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-apiserver-amd64:v1.6.6            gcr.io/google_containers/kube-apiserver-amd64:v1.6.6
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-controller-manager-amd64:v1.6.6   gcr.io/google_containers/kube-controller-manager-amd64:v1.6.6
docker tag dockerhub.ygsoft.com:5000/google_containers/kube-scheduler-amd64:v1.6.6            gcr.io/google_containers/kube-scheduler-amd64:v1.6.6
docker tag dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.9.0-beta.8  gcr.io/google_containers/nginx-ingress-controller:0.9.0-beta.8
docker tag dockerhub.ygsoft.com:5000/google_containers/kubernetes-dashboard-amd64:v1.6.1      gcr.io/google_containers/kubernetes-dashboard-amd64:v1.6.1
docker tag dockerhub.ygsoft.com:5000/google_containers/k8s-dns-sidecar-amd64:1.14.2           gcr.io/google_containers/k8s-dns-sidecar-amd64:1.14.2
docker tag dockerhub.ygsoft.com:5000/google_containers/k8s-dns-kube-dns-amd64:1.14.2          gcr.io/google_containers/k8s-dns-kube-dns-amd64:1.14.2
docker tag dockerhub.ygsoft.com:5000/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.2     gcr.io/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.2
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.3.0                  gcr.io/google_containers/heapster-amd64:v1.3.0
docker tag dockerhub.ygsoft.com:5000/google_containers/elasticsearch:v2.4.1-2                 gcr.io/google_containers/elasticsearch:v2.4.1-2
docker tag dockerhub.ygsoft.com:5000/google_containers/etcd-amd64:3.0.17                      gcr.io/google_containers/etcd-amd64:3.0.17
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-grafana-amd64:v4.0.2          gcr.io/google_containers/heapster-grafana-amd64:v4.0.2
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-influxdb-amd64:v1.1.1         gcr.io/google_containers/heapster-influxdb-amd64:v1.1.1
docker tag dockerhub.ygsoft.com:5000/google_containers/heapster-amd64:v1.3.0-beta.1           gcr.io/google_containers/heapster-amd64:v1.3.0-beta.1
docker tag dockerhub.ygsoft.com:5000/google_containers/kibana:v4.6.1-1                        gcr.io/google_containers/kibana:v4.6.1-1
docker tag dockerhub.ygsoft.com:5000/google_containers/fluentd-elasticsearch:1.22             gcr.io/google_containers/fluentd-elasticsearch:1.22
docker tag dockerhub.ygsoft.com:5000/google_containers/nginx-ingress-controller:0.8.3         gcr.io/google_containers/nginx-ingress-controller:0.8.3
docker tag dockerhub.ygsoft.com:5000/google_containers/pause-amd64:3.0                        gcr.io/google_containers/pause-amd64:3.0
docker tag dockerhub.ygsoft.com:5000/google_containers/defaultbackend:1.0                     gcr.io/google_containers/defaultbackend:1.0
