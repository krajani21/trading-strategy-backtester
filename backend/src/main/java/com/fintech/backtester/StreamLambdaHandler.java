package com.fintech.backtester;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
// Notice the .v2 in the package name below
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler {
    // 1. Update the generic type to HttpApiV2ProxyRequest
    private static SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            // 2. Use the factory method for HTTP API (v2)
            handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(TradingStrategyBacktesterApplication.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        // This method handles the stream and passes it to the v2-aware handler
        handler.proxyStream(inputStream, outputStream, context);
    }
}