package com.pontusvision.tika;

import com.microsoft.azure.functions.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import com.microsoft.azure.functions.*;

public class AzureFuction {


    @FunctionName("pvhome-extract-text")
    public void queuetriggerandoutputPOJO(
        @QueueTrigger(name = "message", queueName = "pvhome-extract-requests", connection = "AzureWebJobsStorage") String content,
        @QueueOutput(name = "output", queueName = "pvhome-extract-responses", connection = "AzureWebJobsStorage") OutputBinding<JavaTikaResponse> output,
        final ExecutionContext context
    ) throws IOException, SAXException, TikaException {
        // context.getLogger().info("Java Queue trigger POJO function processed a message: " + message.id);

        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        InputStream decodedInputStream = new Base64InputStream(inputStream, true);
        
        InputStream stream = TikaInputStream.get(decodedInputStream);
        parser.parse(stream, handler, metadata, parseContext);
        output.setValue(new JavaTikaResponse(metadata, handler.toString()));
        
    }

    public class JavaTikaResponse {
        private final Metadata metadata;
        private final String content;
    
    
        public JavaTikaResponse(Metadata metadata, String content) {
            this.metadata = metadata;
            this.content = content;
        }
    
        public Metadata getMetadata() {
            return metadata;
        }
    
        public String getContent() {
            return content;
        }
    }
}
