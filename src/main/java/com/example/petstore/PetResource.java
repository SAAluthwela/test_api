package com.example.petstore;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.json.JSONException;
import org.json.JSONObject;



@Path("/v1/pets")
@Produces("application/json")
public class PetResource {

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "All Pets", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))) })
	@GET
	public Response getPets() {

		return Response.ok(PetList.getInstance().getArrayList()).build();
	}

	@APIResponses(value = {
			@APIResponse(responseCode = "200", description = "Pet for id", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(ref = "Pet"))),
			@APIResponse(responseCode = "404", description = "No Pet found for the id.") })
	@GET
	@Path("{petId}")
	public Response getPet(@PathParam("petId") int petId) {
		if (petId < 0) {
			return Response.status(Status.NOT_FOUND).build();
		}
		Pet pet = new Pet();
		pet.setPetId(petId);
		pet.setPetAge(3);
		pet.setPetName("Buula");
		pet.setPetType("Dog");

		return Response.ok(pet).build();
		
	}

	@Path("/add")
	@Produces("application/json")
	@POST
	public Response addPet(String request) throws JSONException {
		JSONObject jsonObject = new JSONObject(request);
		System.out.println(request);
		if (jsonObject.has("Pet_name") && jsonObject.has("Pet_type") && jsonObject.has("Pet_age")) {
			Pet petData = new Pet();
			petData.setPetName(jsonObject.getString("Pet_name"));
			petData.setPetAge(Integer.parseInt(jsonObject.getString("Pet_age")));
			petData.setPetType(jsonObject.getString("Pet_type"));

			petData.setPetId(PetList.getInstance().getArrayList().get(PetList.getInstance().getArrayList().size() - 1).getPetId() + 1);

			List<Pet> petList = new ArrayList<Pet>();
			List<Pet> temp = new ArrayList<Pet>();

			petList.add(petData);
			temp.addAll(PetList.getInstance().getArrayList());
			temp.addAll(petList);
			PetList.getInstance().setArrayList(temp);

			return Response.ok(petList).build();
		} else {
			return Response.ok("fail").build();
		}

	}
	@DELETE
	@Produces("application/json")
	@Path("/delete/{petId}")
	public Response deletePet(@PathParam("petId") int petId){
			boolean petFound = false;
			for (int i = 0; i < PetList.getInstance().getArrayList().size(); i++) {
				if (petId == PetList.getInstance().getArrayList().get(i).getPetId()) {
					PetList.getInstance().getArrayList().remove(i);
					petFound = true;
				}
			}
			if (petFound) {
				return Response.ok("{\n" + "\"successful\":true\n" + "}").build();
			} else {
				return Response.ok("{\n" + "\"successful\":false\n" + "}").build();
			}

		}
	@PUT
	@Produces("application/json")
	@Path("/edit/{petId}")
	public Response editPet(@PathParam("petId") int petId,String petData) throws JSONException {
		JSONObject jsonObject = new JSONObject(petData);
		boolean updated = false;
		int id = 0;
		if(jsonObject.has("Pet_name")){
			for (int i=0;i<PetList.getInstance().getArrayList().size();i++){
				if(petId == PetList.getInstance().getArrayList().get(i).getPetId()){
					PetList.getInstance().getArrayList().get(i).setPetName(jsonObject.getString("Pet_name"));
					updated=true;
					id = i;
				}
			}
		}
		if(jsonObject.has("Pet_age")){
			for (int i=0;i<PetList.getInstance().getArrayList().size();i++){
				if(petId == PetList.getInstance().getArrayList().get(i).getPetId()){
					PetList.getInstance().getArrayList().get(i).setPetAge(Integer.parseInt(jsonObject.getString("age")));
					updated=true;
					id = i;
				}
			}
			
		}
		if(jsonObject.has("Pet_type")){
			for (int i=0;i<PetList.getInstance().getArrayList().size();i++){
				if(petId == PetList.getInstance().getArrayList().get(i).getPetId()){
					PetList.getInstance().getArrayList().get(i).setPetType(jsonObject.getString("Pet_type"));
					updated=true;
					id = i;
				}
			}
		}
		if(updated){
			return Response.ok(PetList.getInstance().getArrayList().get(id)).build();
		}else{
			return Response.ok("{\n" + "\"success\":false\n" + "}").build();
		}

	}


}
