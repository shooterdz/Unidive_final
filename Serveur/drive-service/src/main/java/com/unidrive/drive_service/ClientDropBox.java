package com.unidrive.drive_service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.unidrive.file_dropbox.Entry;
import com.unidrive.file_dropbox.FileDrop;
import com.unidrive.file_dropbox.FileDropBox;
import com.unidrive.file_dropbox.FileDropBoxV2;
import com.unidrive.file_dropbox.Information;
import com.unidrive.file_dropbox.Move;
import com.unidrive.file_dropbox.Share;;


public class ClientDropBox {
	private static Client client = Client.create();
	private static ObjectMapper mapper = new ObjectMapper();
	private static WebResource webResource;
	private static ClientResponse response;
	private static TrackToken token_ = new TrackToken();
	private final static String clientID = "p8ohrm26izkscws";
	private final static String clientSecret = "ovydv03up9b2zj7";
	
	
//	public ListFileTranslator getFiles(String rep){
//		FileDropBox liste = new FileDropBox();
//		ListFileTranslator listeFT = new ListFileTranslator();
//		try {
//			webResource = client
//			   .resource("https://api.dropboxapi.com/1/metadata/auto/"+rep+"?access_token="+token_.getAccess_token());
//			
//			response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//
//			String output = response.getEntity(String.class);
//			System.out.println(output);
//			liste= mapper.readValue(output, FileDropBox.class);
//			
//			//System.out.println("Output from Server .... \n");
//			if(liste.getContents() != null){
//				for(int i = 0; i <liste.getContents().size();i++){
//					FileDropBox current = liste.getContents().get(i);
//					FileTranslator currentFT = new FileTranslator();
//					currentFT.setName(current.getPath().substring(1, current.getPath().length()));
//					String[] tab = currentFT.getName().split("/");
//					currentFT.setName(tab[tab.length-1]);
//					if(current.getIsDir()){
//						currentFT.setType("folder");
//					}else{
//						currentFT.setType("file");
//					}
//					currentFT.setDrive(new ArrayList<String>());
//					currentFT.setSize(current.getSize());
//					listeFT.add(currentFT);
//				}
//			}
//			
//		  } catch (Exception e) {
//
//			e.printStackTrace();
//
//		  }
//		return listeFT;
//	}
	
	public ListFileTranslator getFiles(String rep){
	FileDropBoxV2 liste = new FileDropBoxV2();
	ListFileTranslator listeFT = new ListFileTranslator();
	try {
		webResource = client
		   .resource("https://api.dropboxapi.com/2/files/list_folder");
		
		if(!rep.equals("")){
			rep = "/"+rep;
		}
		JSONObject obj = new JSONObject();
		obj.put("path", rep);
		obj.put("recursive", false);
		obj.put("include_media_info", false);
		obj.put("include_deleted", false);
		obj.put("include_has_explicit_shared_members", false);

		
		ClientResponse response = webResource
				.header("Authorization", "Bearer "+token_.getAccess_token())
				.header("Content-Type", "application/json")
				.entity(obj.toString(), MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
			    .post(ClientResponse.class);

		String output = response.getEntity(String.class);
		//System.out.println(output);
		liste= mapper.readValue(output, FileDropBoxV2.class);
		
		//System.out.println("Output from Server .... \n");
		for(Entry current : liste.getEntries()){
			FileTranslator currentFT = new FileTranslator();
			currentFT.setName(current.getName());
			//System.out.println(current.getName());
			if(current.getTag().equals("file")){
				currentFT.setType("file");
				currentFT.setSize(current.getSize().toString());
			}else{
				currentFT.setType("folder");
			}
			currentFT.setId(current.getId());
			listeFT.add(currentFT);
		}
	  } catch (Exception e) {

		e.printStackTrace();
	  }
	return listeFT;
}
	public void getToken(String code){
		try{
			WebResource webResource = client
					   .resource("https://api.dropboxapi.com/1/oauth2/token");

			MultivaluedMap<String,String> formData = new MultivaluedMapImpl();
			formData.add("code", code);
			formData.add("client_id", clientID);
			formData.add("client_secret", clientSecret);
			formData.add("redirect_uri","http://localhost:8080/drive-service/rest/DropBox/Response" );
			formData.add("grant_type", "authorization_code");
			ClientResponse response = webResource
			    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
			    .post(ClientResponse.class, formData);
			String output = response.getEntity(String.class);
			token_= mapper.readValue(output, TrackToken.class);
			System.out.println(token_);
			
			
		}catch (Exception e) {

			e.printStackTrace();

		  }	
	}
	
	public Info getInfo() throws JsonParseException, JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException{
		Info result = new Info();
		webResource = client
				   .resource("https://api.dropboxapi.com/2/users/get_space_usage");
		ClientResponse response = webResource
				.header("Authorization", "Bearer "+token_.getAccess_token())
			    .post(ClientResponse.class);
		
		Information current = mapper.readValue(response.getEntity(String.class), Information.class);
		result.setUse(current.getUsed());
		result.setAvailable(current.getAllocation().getAllocated());
		return result;
	}
	
	public String getShare(String rep){
		String res ="";
		try{
			webResource = client
					   .resource("https://api.dropboxapi.com/2/sharing/create_shared_link");	
			JSONObject body = new JSONObject();
			body.put("path", "/"+rep);
			body.put("short_url",false);
			response = webResource
					.header("Authorization", "Bearer "+token_.getAccess_token())
					.header("Content-Type", "application/json")
					.entity(body.toString(), MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
				    .post(ClientResponse.class);
			
			String output = response.getEntity(String.class);
			Share share = mapper.readValue(output, Share.class);
			return share.getUrl();			
		}catch (Exception e) {

			e.printStackTrace();

		  }
		return res;
	}
	
	public void deleteFile(String rep){
		try{
			WebResource webResource = client
					   .resource("https://api.dropboxapi.com/1/fileops/delete");
			
			MultivaluedMap<String,String> formData = new MultivaluedMapImpl();
			formData.add("root","auto");
			formData.add("path", rep);
			formData.add("access_token",token_.getAccess_token());
			ClientResponse response = webResource
			    .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
			    .post(ClientResponse.class, formData);
			//String output = response.getEntity(String.class);
			
		}catch (Exception e) {

			e.printStackTrace();

		}
	}
	
	public void renameFile(String oldPath, String newPath){
		try{
			webResource = client
					   .resource("https://api.dropboxapi.com/2/files/move");	
			
			Move current = new Move();
			current.setFromPath("/"+oldPath);
			current.setToPath("/"+newPath);
			current.setAutorename(false);
			current.setAllowSharedFolder(false);
			
			response = webResource
					.header("Authorization", "Bearer "+token_.getAccess_token())
					.header("Content-Type", "application/json")
					.entity(mapper.writeValueAsString(current), MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
				    .post(ClientResponse.class);
			
			String output = response.getEntity(String.class);
			System.out.println(output);
			
		}catch (Exception e) {

			e.printStackTrace();

		  }
		
	}
	public FileTranslator createFile(String path){
		FileTranslator currentFT = new FileTranslator();
		try{
			webResource = client
					   .resource("https://content.dropboxapi.com/2/files/upload");	
			java.io.File file = new java.io.File("content.txt");

			FileDrop current = new FileDrop();
			current.setPath("/"+path);
			current.setAutorename(true);
			current.setMode("add");
			current.setMute(false);

			ClientResponse response = webResource
					.header("Authorization", "Bearer "+token_.getAccess_token())
					.header("Dropbox-API-Arg", mapper.writeValueAsString(current))
				    .type(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				    .entity(file, MediaType.APPLICATION_OCTET_STREAM_TYPE)
				    .post(ClientResponse.class);
			
			String output = response.getEntity(String.class);
			System.out.println(output);

			
			
			//FileDropBox current_ = mapper.readValue(output, FileDropBox.class);
			//currentFT.setName(current_.getPath());
			
			
		}catch (Exception e) {

			e.printStackTrace();

		  }
		return currentFT;
	}
	public String getDownload(String rep) {
		String res ="";
		try{
			webResource = client
					   .resource("https://content.dropboxapi.com/2/files/download");	
			JSONObject body = new JSONObject();
			body.put("path", "/"+rep);
			
			response = webResource
					.header("Authorization", "Bearer "+token_.getAccess_token())
					.entity(body.toString(), MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
				    .post(ClientResponse.class);
			
			String output = response.getEntity(String.class);
			Share share = mapper.readValue(output, Share.class);
			return share.getUrl();			
		}catch (Exception e) {

			e.printStackTrace();

		  }
		return res;
	}
}
