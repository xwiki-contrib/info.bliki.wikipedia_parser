package info.bliki.api;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;

public class TestUser extends User {
    public TestUser(String username, String password, String mediawikiApiUrl) {
        super(username, password, mediawikiApiUrl, "");
        connector = new TestConnector();
    }

    public static class TestConnector extends Connector {
        public TestConnector() {
            super(DEFAULT_HTTPCLIENT_BUILDER
                .setSSLSocketFactory(sslFactory())
                .disableContentCompression());
        }

        private static SSLConnectionSocketFactory sslFactory() {
            try {
                SSLContextBuilder sslBuilder = new SSLContextBuilder();
                sslBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                return new SSLConnectionSocketFactory(sslBuilder.build(), NoopHostnameVerifier.INSTANCE);
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ignored) {
                return null;
            }
        }
    }
}
