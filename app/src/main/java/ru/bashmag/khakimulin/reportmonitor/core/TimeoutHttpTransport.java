package ru.bashmag.khakimulin.reportmonitor.core;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Proxy;

import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 07.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TimeoutHttpTransport extends HttpTransportSE {
    public TimeoutHttpTransport(String url) {
        super(url);
    }

    public TimeoutHttpTransport(Proxy proxy, String url) {
        super(proxy, url);
    }

    public TimeoutHttpTransport(String url, int timeout) {
        super(url, timeout);
    }

    public TimeoutHttpTransport(Proxy proxy, String url, int timeout) {
        super(proxy, url, timeout);
    }

    public TimeoutHttpTransport(String url, int timeout, int contentLength) {
        super(url, timeout, contentLength);
    }

    public TimeoutHttpTransport(Proxy proxy, String url, int timeout, int contentLength) {
        super(proxy, url, timeout, contentLength);
    }

    public void call(String soapAction, SoapEnvelope envelope, int timeout)
            throws HttpResponseException, IOException, XmlPullParserException {
        this.timeout = timeout;
        super.call(soapAction,envelope);
    }

    public void defaultTimeout() {
        this.timeout = Constants.DEFAULT_TIMEOUT;
    }
}
