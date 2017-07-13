package io.swagger.api;

import io.swagger.model.Body;
import io.swagger.model.Body1;
import java.io.File;
import io.swagger.model.InlineResponse200;
import io.swagger.model.InlineResponse2001;
import io.swagger.model.InlineResponse2002;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-06-30T06:26:34.006Z")

@Controller
public class PetApiController implements PetApi {



    public ResponseEntity<Void> addPet(@ApiParam(value = "Pet object that needs to be added to the store" ,required=true ) @RequestBody Body1 body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> deletePet(@ApiParam(value = "Pet id to delete",required=true ) @PathVariable("petId") Long petId,
        @ApiParam(value = ""  ) @RequestHeader(value="api_key", required=false) String apiKey) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse200>> findPetsByStatus( @NotNull @ApiParam(value = "Status values that need to be considered for filter", required = true, allowableValues = "AVAILABLE, PENDING, SOLD") @RequestParam(value = "status", required = true) List<String> status) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse200>> findPetsByTags( @NotNull @ApiParam(value = "Tags to filter by", required = true) @RequestParam(value = "tags", required = true) List<String> tags) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.OK);
    }

    public ResponseEntity<InlineResponse2001> getPetById(@ApiParam(value = "ID of pet to return",required=true ) @PathVariable("petId") Long petId) {
        // do some magic!
        return new ResponseEntity<InlineResponse2001>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updatePet(@ApiParam(value = "Pet object that needs to be added to the store" ,required=true ) @RequestBody Body body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updatePetWithForm(@ApiParam(value = "ID of pet that needs to be updated",required=true ) @PathVariable("petId") Long petId,
        @ApiParam(value = "Updated name of the pet" ) @RequestPart(value="name", required=false)  String name,
        @ApiParam(value = "Updated status of the pet" ) @RequestPart(value="status", required=false)  String status) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<InlineResponse2002> uploadFile(@ApiParam(value = "ID of pet to update",required=true ) @PathVariable("petId") Long petId,
        @ApiParam(value = "Additional data to pass to server" ) @RequestPart(value="additionalMetadata", required=false)  String additionalMetadata,
        @ApiParam(value = "file detail") @RequestPart("file") MultipartFile file) {
        // do some magic!
        return new ResponseEntity<InlineResponse2002>(HttpStatus.OK);
    }

}
