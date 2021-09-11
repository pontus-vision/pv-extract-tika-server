package com.pontusvision.tika;

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.util.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
  protected static final Logger logger = LoggerFactory.getLogger(LambdaHandler.class);
  protected static final ResourceConfig jerseyApplication = new ResourceConfig()
      .packages("com.pontusvision.tika", "com.pontusvision.security")
      .register(JacksonFeature.class);
  protected static final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler
      = JerseyLambdaContainerHandler
      .getAwsProxyHandler(jerseyApplication);

  static {
    try {
      System.out.println("IN LAMBDA HANDLER");
      App.init();

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

//    private JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> container =
//        JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);

  public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
    return LambdaHandler.handler.proxy(awsProxyRequest, context);
  }


}
