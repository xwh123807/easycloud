package io.swagger.api;

import io.swagger.model.Body;
import io.swagger.model.Body1;
import java.io.File;
import io.swagger.model.InlineResponse200;
import io.swagger.model.InlineResponse2001;
import io.swagger.model.InlineResponse2002;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T06:26:34.006Z")

@Api(value = "pet", description = "the pet API")
public interface PetApi {

    @ApiOperation(value = "Add a new pet to the store", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 405, message = "Invalid input", response = Void.class) })
    @RequestMapping(value = "/pet",
        produces = { "application/xml", "application/json" }, 
        consumes = { "application/json", "application/xml" },
        method = RequestMethod.POST)
    ResponseEntity<Void> addPet(@ApiParam(value = "Pet object that needs to be added to the store" ,required=true ) @RequestBody Body1 body);


    @ApiOperation(value = "Deletes a pet", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
        @ApiResponse(code = 404, message = "Pet not found", response = Void.class) })
    @RequestMapping(value = "/pet/{petId}",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> deletePet(@ApiParam(value = "Pet id to delete",required=true ) @PathVariable("petId") Long petId,
        @ApiParam(value = ""  ) @RequestHeader(value="api_key", required=false) String apiKey);


    @ApiOperation(value = "Finds Pets by status", notes = "Multiple status values can be provided with comma separated strings", response = InlineResponse200.class, responseContainer = "List", authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InlineResponse200.class),
        @ApiResponse(code = 400, message = "Invalid status value", response = InlineResponse200.class) })
    @RequestMapping(value = "/pet/findByStatus",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse200>> findPetsByStatus( @NotNull @ApiParam(value = "Status values that need to be considered for filter", required = true, allowableValues = "AVAILABLE, PENDING, SOLD") @RequestParam(value = "status", required = true) List<String> status);


    @ApiOperation(value = "Finds Pets by tags", notes = "Muliple tags can be provided with comma separated strings. Use         tag1, tag2, tag3 for testing.", response = InlineResponse200.class, responseContainer = "List", authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InlineResponse200.class),
        @ApiResponse(code = 400, message = "Invalid tag value", response = InlineResponse200.class) })
    @RequestMapping(value = "/pet/findByTags",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<InlineResponse200>> findPetsByTags( @NotNull @ApiParam(value = "Tags to filter by", required = true) @RequestParam(value = "tags", required = true) List<String> tags);


    @ApiOperation(value = "Find pet by ID", notes = "Returns a single pet", response = InlineResponse2001.class, authorizations = {
        @Authorization(value = "api_key")
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InlineResponse2001.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = InlineResponse2001.class),
        @ApiResponse(code = 404, message = "Pet not found", response = InlineResponse2001.class) })
    @RequestMapping(value = "/pet/{petId}",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<InlineResponse2001> getPetById(@ApiParam(value = "ID of pet to return",required=true ) @PathVariable("petId") Long petId);


    @ApiOperation(value = "Update an existing pet", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid ID supplied", response = Void.class),
        @ApiResponse(code = 404, message = "Pet not found", response = Void.class),
        @ApiResponse(code = 405, message = "Validation exception", response = Void.class) })
    @RequestMapping(value = "/pet",
        produces = { "application/xml", "application/json" }, 
        consumes = { "application/json", "application/xml" },
        method = RequestMethod.PUT)
    ResponseEntity<Void> updatePet(@ApiParam(value = "Pet object that needs to be added to the store" ,required=true ) @RequestBody Body body);


    @ApiOperation(value = "Updates a pet in the store with form data", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 405, message = "Invalid input", response = Void.class) })
    @RequestMapping(value = "/pet/{petId}",
        produces = { "application/xml", "application/json" }, 
        consumes = { "application/x-www-form-urlencoded" },
        method = RequestMethod.POST)
    ResponseEntity<Void> updatePetWithForm(@ApiParam(value = "ID of pet that needs to be updated",required=true ) @PathVariable("petId") Long petId,
        @ApiParam(value = "Updated name of the pet" ) @RequestPart(value="name", required=false)  String name,
        @ApiParam(value = "Updated status of the pet" ) @RequestPart(value="status", required=false)  String status);


    @ApiOperation(value = "uploads an image", notes = "", response = InlineResponse2002.class, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets")
            })
    }, tags={ "pet", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = InlineResponse2002.class) })
    @RequestMapping(value = "/pet/{petId}/uploadImage",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ResponseEntity<InlineResponse2002> uploadFile(@ApiParam(value = "ID of pet to update",required=true ) @PathVariable("petId") Long petId,
        @ApiParam(value = "Additional data to pass to server" ) @RequestPart(value="additionalMetadata", required=false)  String additionalMetadata,
        @ApiParam(value = "file detail") @RequestPart("file") MultipartFile file);

}
