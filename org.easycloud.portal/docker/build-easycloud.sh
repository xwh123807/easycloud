cd ..
npm run build
cd docker

docker build -t dockerhub.ygsoft.com:5000/ygsoft/easycloud:1.0.2 -f Dockerfile-easycloud ../

docker push dockerhub.ygsoft.com:5000/ygsoft/easycloud:1.0.2
