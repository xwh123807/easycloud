###
# swagger-editor - https://github.com/swagger-api/swagger-editor/
#
# Run the swagger-editor service on port 8080
###

FROM    dockerhub.ygsoft.com:5000/ygsoft/angular4:1.0.0

WORKDIR /easycloud
ADD     .    /easycloud

# The default port of the application
EXPOSE  4200

# CMD ["http-server", "--cors", "-p8080", "/easycloud"]

CMD ["npm", "run", "start"]
