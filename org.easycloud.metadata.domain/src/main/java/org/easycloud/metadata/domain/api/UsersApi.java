package org.easycloud.metadata.domain.api;

import java.util.List;

import org.easycloud.metadata.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-08T01:55:37.567Z")

@Api(value = "users", description = "the users API")
public interface UsersApi {

    @ApiOperation(value = "Create user", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    @RequestMapping(value = "/users",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> createUser(@ApiParam(value = "Created user object"  ) @RequestBody User body);


    @ApiOperation(value = "Creates list of users with given input array", notes = "", response = Void.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    @RequestMapping(value = "/users/createWithArray",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> createUsersWithArrayInput(@ApiParam(value = "List of user object"  ) @RequestBody List<User> body);


    @ApiOperation(value = "Creates list of users with given input array", notes = "", response = Void.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    @RequestMapping(value = "/users/createWithList",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> createUsersWithListInput(@ApiParam(value = "List of user object"  ) @RequestBody List<User> body);


    @ApiOperation(value = "Delete user", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid username supplied", response = Void.class),
        @ApiResponse(code = 404, message = "User not found", response = Void.class) })
    @RequestMapping(value = "/users/{username}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteUser(@ApiParam(value = "The name that needs to be deleted",required=true ) @PathVariable("username") String username);


    @ApiOperation(value = "Get user by user name", notes = "", response = User.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = User.class),
        @ApiResponse(code = 400, message = "Invalid username supplied", response = User.class),
        @ApiResponse(code = 404, message = "User not found", response = User.class) })
    @RequestMapping(value = "/users/{username}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
    ResponseEntity<User> getUserByName(@ApiParam(value = "The name that needs to be fetched. Use user1 for testing.",required=true ) @PathVariable("username") String username);


    @ApiOperation(value = "Logs user into the system", notes = "", response = String.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = String.class),
        @ApiResponse(code = 400, message = "Invalid username/password supplied", response = String.class) })
    @RequestMapping(value = "/users/login",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
    ResponseEntity<String> loginUser( @ApiParam(value = "The user name for login") @RequestParam(value = "username", required = false) String username,
         @ApiParam(value = "The password for login in clear text") @RequestParam(value = "password", required = false) String password);


    @ApiOperation(value = "Logs out current logged in user session", notes = "", response = Void.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    @RequestMapping(value = "/users/logout",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
    ResponseEntity<Void> logoutUser();


    @ApiOperation(value = "Updated user", notes = "This can only be done by the logged in user.", response = Void.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid user supplied", response = Void.class),
        @ApiResponse(code = 404, message = "User not found", response = Void.class) })
    @RequestMapping(value = "/users/{username}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> updateUser(@ApiParam(value = "name that need to be deleted",required=true ) @PathVariable("username") String username,
        @ApiParam(value = "Updated user object"  ) @RequestBody User body);

}
