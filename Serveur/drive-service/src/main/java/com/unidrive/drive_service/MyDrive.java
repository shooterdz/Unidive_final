package com.unidrive.drive_service;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



@Path("/googleDrive")
public class MyDrive {
	private static String code_;
	private final static String clientID = "559854928491-b84f4ejfdhooej6rmqcvqfo4gsiveph1.apps.googleusercontent.com";
	private static ClientGoogle client = new ClientGoogle();
	
	//This method is called if HTML is request
	@Path("/OAuth")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String googleDriveAuth(@QueryParam("auth") String auth) {
	String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/drive&access_type=offline&redirect_uri=http://localhost:8080/drive-service/rest/googleDrive/Response&response_type=code&client_id="+clientID;
	return "L'authentification a ete demander par " + auth + " <a href="+ url+">CLIQUE ICI SALOPE !</a>";
	}
	
	//This method is called if HTML is request
	@Path("/Response")
	@GET
	//@Produces(MediaType.)
	public Response googleDriveResponse(@QueryParam("code") String code) throws URISyntaxException {
	code_ = code;
	client.getToken(code_);  
	java.net.URI location = new java.net.URI("http://localhost:4200/login");
	//ListFileTranslator liste = client.getFiles("root");
	return Response.temporaryRedirect(location).build();
	}
	
	@Path("/Get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ListFileTranslator getChildren(@QueryParam("rep") String rep){
	ListFileTranslator liste = client.getFiles(rep);
	return liste;
	}
	
	@Path("/Info")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Info getInfo() throws JsonParseException, JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException{
	return client.getInfo();
	}
	
	@Path("/Rename")
	@POST
	public Response renameFile(@QueryParam("id") String old,@QueryParam("name") String nouveau) throws JsonParseException, JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException, UnirestException {
		client.renameFile(old, nouveau);
	return Response.status(200).build();
	}
	
	@Path("/Move")
	@POST
	public Response moveFile(@QueryParam("id") String old,@QueryParam("idn") String nouveau) throws JsonParseException, JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException, UnirestException {
		client.moveFile(old, nouveau);
	return Response.status(200).build();
	}
	
	
	@Path("/Delete")
	@DELETE
	public Response deleteFile(@QueryParam("rep") String rep){
	client.deleteFile(rep);
	return Response.status(200).build();
	}
	
	@Path("/Upload")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public FileTranslator createFile(@QueryParam("name") String title,@QueryParam("parent") String parent,String fileContent){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new java.io.File("content.txt")));
			// normalement si le fichier n'existe pas, il est cr�e � la racine du projet
			writer.write(fileContent);
			 
			writer.close();
			}
			catch (IOException e)
			{
			e.printStackTrace();
			}
	FileTranslator file = client.createFile(title,parent);	
	return file;
	}
	
}
