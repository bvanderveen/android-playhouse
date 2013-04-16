package me.wai.AndroidWai;

import rx.Observable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpClientImpl implements HttpClient {

    String baseUrl;

    public HttpClientImpl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // synchronous, buffered read into a stream, happens on a background thread.
    String readStream(InputStream s) {
        char[] buffer = new char[1024 * 4];
        StringBuilder out = new StringBuilder();

        try {
            Reader in = new InputStreamReader(s, "UTF-8");
            try {
                while (true) {
                    int bytesRead = in.read(buffer, 0, buffer.length);
                    if (bytesRead < 0)
                        break;
                    out.append(buffer, 0, bytesRead);
                }
            } finally {
                in.close();
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return out.toString();
    }

    void setResponseBodyFromStream(HttpResponse response, InputStream stream) {
        if (stream != null) {
            response.body = readStream(stream);
        }
    }

    HttpResponse performSynchronousRequest(HttpRequest request) {

        URL u = null;
        try {
            u = new URL(baseUrl + request.path);
        } catch (MalformedURLException e) {

        }

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) u.openConnection();
        } catch (IOException e) {
            assert false;
        }
        try {
            connection.setRequestMethod(request.method);
        } catch (ProtocolException e) {
            assert false;
        }

        if (request.body != null) {
            connection.setDoOutput(true);
        }

        String body = null;

        HttpResponse result = new HttpResponse();

        try {
            setResponseBodyFromStream(result, connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            setResponseBodyFromStream(result, connection.getErrorStream());

        } catch (Exception e) {
        } finally {
            connection.disconnect();
        }

        return result;
    }

    @Override
    public Observable<HttpResponse> performRequest(final HttpRequest request) {
        return Task.performInBackground(new Task.TaskDelegate<HttpResponse>() {
            public HttpResponse perform() {
                return performSynchronousRequest(request);
            }
        });
    }
}
