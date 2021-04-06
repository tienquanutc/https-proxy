package app;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import okhttp3.*;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Main {

    private static final String PROXY_HOST = "ca10.freevpn.zone";
    private static final int PROXY_PORT = 443;
    private static final String PROXY_USER_NAME = "DictOciafDocyangEzbawuj1";
    private static final String PROXY_PASSWORD = "bavpensacpyubEfketDiwrir";

    private static final String WHAT_IP_ENDPOINT = "https://api.ipify.org/?format=json";

    public static void main(String[] args) throws Exception {
        System.out.println("OKHTTP PROXY");
        proxyWithOkHttp();
        System.out.println("VERTX PROXY");
        proxyWithVertx();
    }

    private static void proxyWithOkHttp() throws IOException {
        Authenticator proxyAuthenticator = (route, response) -> {
            String credential = Credentials.basic(PROXY_USER_NAME, PROXY_PASSWORD);
            return response.request().newBuilder().header("Proxy-Authorization", credential).build();
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT)))
                .proxyAuthenticator(proxyAuthenticator)
                .socketFactory(new DelegatingSocketFactory(SSLSocketFactory.getDefault())) //I don't understand, can't use proxy without this
                .build();

        Request request = new Request.Builder()
                .url(WHAT_IP_ENDPOINT)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String bodyAsString = new String(response.body().bytes());

        System.out.println(bodyAsString);
    }

    private static void proxyWithVertx() throws Exception {
        Vertx vertx = Vertx.vertx();

        ProxyOptions proxyOptions = new ProxyOptions()
                .setUsername(PROXY_USER_NAME)
                .setPassword(PROXY_PASSWORD)
                .setHost(PROXY_HOST)
                .setPort(PROXY_PORT);
        WebClientOptions webClientOptions = new WebClientOptions()
                .setProxyOptions(proxyOptions);
        WebClient webClient = WebClient.create(vertx, webClientOptions);

        HttpResponse<Buffer> httpResponse = webClient.getAbs(WHAT_IP_ENDPOINT).send().toCompletionStage().toCompletableFuture().get();
        String bodyAsString = httpResponse.bodyAsString();

        System.out.println(bodyAsString);
    }
}
