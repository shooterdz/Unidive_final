package com.unidrive.drive_service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPatch;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.research.ws.wadl.Request;
import com.unidrive.file_google_drive.ChildList;
import com.unidrive.file_google_drive.File;
import com.unidrive.file_google_drive.Information;
import com.unidrive.file_google_drive.Move;
import com.unidrive.file_google_drive.Parent;
import com.unidrive.file_google_drive.Parents;
import com.unidrive.file_google_drive.Rename;

public class ClientGoogle {
	private static Client client = Client.create();
	private static ObjectMapper mapper = new ObjectMapper();
	private static WebResource webResource;
	private static ClientResponse response;
	private static TrackToken token_ = new TrackToken();
	private final static String clientID = "559854928491-b84f4ejfdhooej6rmqcvqfo4gsiveph1.apps.googleusercontent.com";
	private final static String clientSecret = "s_MJfLgiXsRcQyYGAiyxwUQ_";
	
	
	public ListFileTranslator getFiles(String rep){
		ChildList liste = new ChildList();
		ListFileTranslator listeFT = new ListFileTranslator();
		try {
			webResource = client
			   .resource("https://www.googleapis.com/drive/v2/files/"+rep+"/children?access_token="+token_.getAccess_token());
			
			response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			String output = response.getEntity(String.class);
			//System.out.println(output);
			liste= mapper.readValue(output, ChildList.class);
			
			//System.out.println("Output from Server .... \n");
			if(liste.getItems() != null){
				for(int i = 0; i <liste.getItems().size();i++){
					String currentID = liste.getItems().get(i).getId();
					webResource = client
							   .resource("https://www.googleapis.com/drive/v2/files/"+currentID+"?access_token="+token_.getAccess_token());
					response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
					File current = mapper.readValue(response.getEntity(String.class), File.class);
					FileTranslator currentFT = new FileTranslator();
					
					currentFT.setName(current.getTitle());
					currentFT.setId(current.getId());
					if(current.getMimeType().contains("folder")){
						currentFT.setType("folder");
					}else{
						currentFT.setType("file");
					}
					currentFT.setDrive(new ArrayList<String>());
					currentFT.setDownload("https://www.googleapis.com/drive/v3/files/"+current.getId()+"?alt=media&access_token="+token_.getAccess_token());
					currentFT.setSize(current.getFileSize());
					currentFT.setShare(current.getAlternateLink());
					currentFT.setRights(current.getCapabilities().getCanEdit());
					currentFT.setShared(current.getShared());
					listeFT.add(currentFT);
				}
			}
			
		  } catch (Exception e) {

			e.printStackTrace();

		  }
		return listeFT;
	}
	
	public Info getInfo() throws JsonParseException, JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException{
		webResource = client
				   .resource("https://www.googleapis.com/drive/v2/about?access_token="+token_.getAccess_token());
		response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Information current = mapper.readValue(response.getEntity(String.class), Information.class);
		Info result = new Info();
		result.setUse(current.getQuotaBytesUsed());
		result.setAvailable(current.getQuotaBytesTotal());
		return result;
	}
	
	public void getToken(String code){
		try{
			WebResource webResource = client
					   .resource("https://www.googleapis.com/oauth2/v4/token");

			MultivaluedMap<String,String> formData = new MultivaluedMapImpl();
			formData.add("code", code);
			formData.add("client_id", clientID);
			formData.add("client_secret", clientSecret);
			formData.add("redirect_uri","http://localhost:8080/drive-service/rest/googleDrive/Response" );
			formData.add("grant_type", "authorization_code");
			ClientResponse response = webResource
			    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
			    .post(ClientResponse.class, formData);
			String output = response.getEntity(String.class);
			token_= mapper.readValue(output, TrackToken.class);
			
			
		}catch (Exception e) {

			e.printStackTrace();

		  }
	}
	
	public void deleteFile(String rep){
		try{
			WebResource webResource = client
					   .resource("https://www.googleapis.com/drive/v2/files/"+rep+"?access_token="+token_.getAccess_token());
			
			ClientResponse response = webResource
			    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
			    .delete(ClientResponse.class);
			
		}catch (Exception e) {

			e.printStackTrace();

		}
	}
	
	public void renameFile(String id, String name) throws UnirestException, JsonParseException, JsonMappingException, IOException{
		
		String nouveauRes = "{\n \"title\": \""+name+"\"\n}";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPatch updateRequest = new HttpPatch("https://www.googleapis.com/drive/v2/files/"+id+"?access_token="+token_.getAccess_token());
//		System.out.println(mapper.writeValueAsString(nouveau));  
		updateRequest.setEntity(new StringEntity(nouveauRes, ContentType.APPLICATION_JSON));
		 
		httpclient.execute(updateRequest);
	}
	
	public FileTranslator createFile(String title,String parent){
		FileTranslator currentFT = new FileTranslator();
		try{
		   
			java.io.File fileContent = new java.io.File("content.txt");// Thread.currentThread().getContextClassLoader().getResource("vide.txt");
			java.io.File fileMeta = new java.io.File("file.json");
			//MultiPart multipart = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE);
			FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
					fileContent,
	                MediaType.APPLICATION_OCTET_STREAM_TYPE);
			
	        fileDataBodyPart.setContentDisposition(
	                FormDataContentDisposition.name("file")
	                        .fileName(fileContent.getName()).build());
	        
	        File file =  new File();
		    file.setTitle(title);
		    if(parent!=null){
		    	List<Parents> l = new ArrayList<Parents>();
		    	Parents p= new Parents();
		    	p.setId(parent);
		    	l.add(p);
		    	file.setParents(l);
		    }
		    file.setDescription("importer depuis unidrive");
		    file.setMimeType("text/plain");
		    
		    mapper.writeValue(fileMeta, file);
			
	        MultiPart multipart = new FormDataMultiPart()
	        		.field("metadata", fileMeta, MediaType.APPLICATION_JSON_TYPE)
	        		.bodyPart(fileDataBodyPart);
	        multipart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

	        webResource = client
					   .resource("https://www.googleapis.com/upload/drive/v2/files?access_token="+token_.getAccess_token()+"&upload_Type=multipart");
			response = webResource
			    .type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, multipart);
			String output = response.getEntity(String.class);
			File current = mapper.readValue(output, File.class);
			
			currentFT.setName(current.getTitle());
			currentFT.setId(current.getId());
			if(current.getMimeType().contains("folder")){
				currentFT.setType("folder");
			}else{
				currentFT.setType("file");
			}
			currentFT.setDrive(new ArrayList<String>());
			
			
		}catch (Exception e) {

			e.printStackTrace();

		  }
		return currentFT;
	}

	public void moveFile(String old, String nouveau) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		Move m = new Move();
		Parent p = new Parent();
		p.setId(nouveau);
		List<Parent> l = new ArrayList<Parent>();
		l.add(p);
		m.setParents(l);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPatch updateRequest = new HttpPatch("https://www.googleapis.com/drive/v2/files/"+old+"?access_token="+token_.getAccess_token());
//		System.out.println(mapper.writeValueAsString(nouveau));  
		updateRequest.setEntity(new StringEntity(mapper.writeValueAsString(m), ContentType.APPLICATION_JSON));
		 
		httpclient.execute(updateRequest);
	}
}

