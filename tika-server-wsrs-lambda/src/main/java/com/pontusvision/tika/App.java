package com.pontusvision.tika;

//import com.netflix.astyanax.connectionpool.exceptions.ThrottledException;

import com.pontusvision.tika.server.core.PVTikaServerProcess;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.DigestingParser;
import org.apache.tika.parser.digestutils.BouncyCastleDigester;
import org.apache.tika.parser.digestutils.CommonsDigester;
import org.apache.tika.server.core.*;
import com.pontusvision.tika.resource.TikaResource;
import org.apache.tika.utils.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jhades.JHades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App // implements RequestStreamHandler
{
  private static final Logger logger = LoggerFactory.getLogger(App.class);
  //    public static JanusGraphManagement graphMgmt;
  public static Server jettyServer;

  public static void main(String[] args) throws TikaException, IOException, SAXException {
    App.mainHandler(args);
  }

  public static void init() throws Exception {
    TikaConfig tika = new TikaConfig();
    TikaServerConfig tikaServerConfig = TikaServerConfig.load();
    DigestingParser.Digester digester = null;
    if (!StringUtils.isBlank(tikaServerConfig.getDigest())) {
      try {
        digester = new CommonsDigester(tikaServerConfig.getDigestMarkLimit(),
            tikaServerConfig.getDigest());
      } catch (IllegalArgumentException commonsException) {
        try {
          digester = new BouncyCastleDigester(tikaServerConfig.getDigestMarkLimit(),
              tikaServerConfig.getDigest());
        } catch (IllegalArgumentException bcException) {
          throw new IllegalArgumentException(
              "Tried both CommonsDigester (" + commonsException.getMessage() +
                  ") and BouncyCastleDigester (" + bcException.getMessage() + ")",
              bcException);
        }
      }
    }
    InputStreamFactory inputStreamFactory = new DefaultInputStreamFactory();
    ServerStatus  serverStatus = new ServerStatus(tikaServerConfig.getId(), 0, true);

    TikaResource.init(tika, tikaServerConfig, digester, inputStreamFactory, serverStatus);

    List<ResourceProvider> resourceProviders = new ArrayList<>();
    List<Object> providers = new ArrayList<>();
    PVTikaServerProcess.loadAllProviders(tikaServerConfig,
        serverStatus,
        resourceProviders,
        providers);


  }

  public static Server createJettyServer() throws TikaException, IOException, SAXException {
    String portStr = System.getenv("GRAPHDB_REST_PORT");
    int port = 3001;
    if (portStr != null) {
      port = Integer.parseInt(portStr);
    }
    Server server = new Server(port);
    ResourceConfig config = new ResourceConfig();

    config.packages("com.pontusvision.tika", "com.pontusvision.security", "com.pontusvision.tika.resource");
    ServletHolder servlet = new ServletHolder(new ServletContainer(config));

    ServletContextHandler context = new ServletContextHandler(server, "/*");
    context.addServlet(servlet, "/*");

    return server;
  }

  public static void mainHandler(String[] args) throws TikaException, IOException, SAXException {
    new JHades().overlappingJarsReport();

    Server server = createJettyServer();


    try {

      server.start();

      //      final Settings settings;
      try {
        App.init();
      } catch (Exception ex) {
        logger.error("Error Initialising Tika [{}]", ex.getMessage());
        ex.printStackTrace();
        return;
      }

      server.join();
      //      c.join();
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      server.destroy();
    }
  }

  //  @Override public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
  //      throws IOException
  //  {
  //    handler.proxyStream(inputStream, outputStream, context);
  //  }
}
