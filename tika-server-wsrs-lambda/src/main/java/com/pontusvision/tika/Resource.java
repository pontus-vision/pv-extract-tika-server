package com.pontusvision.tika;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Path("tika")
public class Resource {

  //  @Inject
  //  KeycloakSecurityContext keycloakSecurityContext;

  Gson gson = new Gson();
  GsonBuilder gsonBuilder = new GsonBuilder();
  Map<String, Pattern> compiledPatterns = new HashMap<>();

  public Resource() {

  }

  @GET
  @Path("hello")
  @Produces(MediaType.TEXT_PLAIN)
  public String helloWorld() {
    return "Hello, world!";
  }

}