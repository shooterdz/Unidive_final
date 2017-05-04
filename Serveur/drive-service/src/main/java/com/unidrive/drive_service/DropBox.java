package com.unidrive.drive_service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;


@Path("/DropBox")
public class DropBox {
	private static String code_;
	private static ClientDropBox client = new ClientDropBox();
	private final static String clientID = "p8ohrm26izkscws";
	private final static String clientSecret = "ovydv03up9b2zj7";
	
	//This method is called if HTML is request
	@Path("/OAuth")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String dropBoxAuth(@QueryParam("auth") String auth) {
	String url = "https://www.dropbox.com/1/oauth2/authorize?response_type=code&client_id="+clientID+"&redirect_uri=http://localhost:8080/drive-service/rest/DropBox/Response";
	return "L'authentification a ete demander par " + auth + " <a href="+ url+">CLIQUE ICI SALOPE !</a>";
	}
	
	//This method is called if HTML is request
	@Path("/Response")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response dorpBoxDriveResponse(@QueryParam("code") String code ) throws URISyntaxException {
	code_ = code;
	client.getToken(code_); 
	java.net.URI location = new java.net.URI("http://localhost:4200/login");
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
	
	@Path("/Share")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getShare(@QueryParam("rep") String rep) {
	return client.getShare(rep);
	}
	
	@Path("/Download")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDownload(@QueryParam("rep") String rep) {
	return client.getDownload(rep);
	}
	
	@Path("/Move")
	@POST
	public Response moveFile(@QueryParam("old") String old,@QueryParam("new") String nouveau) {
	client.renameFile(old, nouveau);
	return Response.status(200).build();
	}
	
	@Path("/Delete")
	@DELETE
	public Response deleteFile(@QueryParam("rep") String rep){
	client.deleteFile(rep);
	return Response.status(200).build();
	}
	
	@Path("/Rename")
	@POST
	public Response renameFile(@QueryParam("old") String old,@QueryParam("new") String nouveau) {
	client.renameFile(old, nouveau);
	return Response.status(200).build();
	}
	
	@Path("/Upload")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public FileTranslator createFile(@QueryParam("path") String path,String fileContent){
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
	FileTranslator file = client.createFile(path);	
	return file;
	}

}
