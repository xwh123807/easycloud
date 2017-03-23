package org.easycloud.metadata.server.impl;

import java.util.List;

import org.easycloud.metadata.domain.api.PetsApi;
import org.easycloud.metadata.domain.model.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-08T01:55:37.567Z")

@Controller
public class PetsApiController implements PetsApi {



    public ResponseEntity<Void> addPet(@ApiParam(value = "Pet object that needs to be added to the store"  ) @RequestBody Pet body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> deletePet(@ApiParam(value = "" ,required=true ) @RequestHeader(value="api_key", required=true) String apiKey,
        @ApiParam(value = "Pet id to delete",required=true ) @PathVariable("petId") Long petId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<List<Pet>> findPetsByStatus( @ApiParam(value = "Status values that need to be considered for filter") @RequestParam(value = "status", required = false) List<String> status) {
        // do some magic!
        return new ResponseEntity<List<Pet>>(HttpStatus.OK);
    }

    public ResponseEntity<List<Pet>> findPetsByTags( @ApiParam(value = "Tags to filter by") @RequestParam(value = "tags", required = false) List<String> tags) {
        // do some magic!
        return new ResponseEntity<List<Pet>>(HttpStatus.OK);
    }

    public ResponseEntity<Pet> getPetById(@ApiParam(value = "ID of pet that needs to be fetched",required=true ) @PathVariable("petId") Long petId) {
        // do some magic!
        return new ResponseEntity<Pet>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updatePet(@ApiParam(value = "Pet object that needs to be added to the store"  ) @RequestBody Pet body) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updatePetWithForm(@ApiParam(value = "ID of pet that needs to be updated",required=true ) @PathVariable("petId") String petId,
        @ApiParam(value = "Updated name of the pet", required=true ) @RequestPart(value="name", required=true)  String name,
        @ApiParam(value = "Updated status of the pet", required=true ) @RequestPart(value="status", required=true)  String status) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
