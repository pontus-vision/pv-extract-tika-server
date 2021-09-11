package com.pontusvision.tika.resource;


import javax.ws.rs.WebApplicationException;

/**
 * Simple wrapper exception to be thrown for consistent handling
 * of exceptions that can happen during a parse.
 */
public class TikaServerParseException extends WebApplicationException {

  public TikaServerParseException(String msg) {
    super(msg);
  }

  public TikaServerParseException(Exception e) {
    super(e);
  }
}
