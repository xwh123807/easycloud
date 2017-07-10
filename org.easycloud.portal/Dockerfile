###
# swagger-editor - https://github.com/swagger-api/swagger-editor/
#
# Run the swagger-editor service on port 8080
###

FROM    dockerhub.ygsoft.com:5000/ygsoft/httpserver:1.0.0

WORKDIR /easycloud
ADD     ./dist    /easycloud

ENV KUBERNETES_PROXY "http://kube.ygcloud.com:8001"

# The default port of the application
EXPOSE  4200

CMD ["http-server", "--cors", "-P http://kube.ygcloud.com:8001", "-p4200", "/easycloud"]

# CMD ["npm", "run", "start"]
