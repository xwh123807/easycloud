package org.easycloud.metadata.server.impl;

import java.util.List;

import org.easycloud.metadata.domain.api.UsersApi;
import org.easycloud.metadata.domain.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-08T01:55:37.567Z")

@Controller
public class UsersApiController implements UsersApi {



    public ResponseEntity<Void> createUser(@ApiParam(value = "Created user object"  ) @RequestBody User body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> createUsersWithArrayInput(@ApiParam(value = "List of user object"  ) @RequestBody List<User> body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> createUsersWithListInput(@ApiParam(value = "List of user object"  ) @RequestBody List<User> body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteUser(@ApiParam(value = "The name that needs to be deleted",required=true ) @PathVariable("username") String username) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByName(@ApiParam(value = "The name that needs to be fetched. Use user1 for testing.",required=true ) @PathVariable("username") String username) {
        // do some magic!
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    public ResponseEntity<String> loginUser( @ApiParam(value = "The user name for login") @RequestParam(value = "username", required = false) String username,
         @ApiParam(value = "The password for login in clear text") @RequestParam(value = "password", required = false) String password) {
        // do some magic!
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    public ResponseEntity<Void> logoutUser() {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updateUser(@ApiParam(value = "name that need to be deleted",required=true ) @PathVariable("username") String username,
        @ApiParam(value = "Updated user object"  ) @RequestBody User body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
