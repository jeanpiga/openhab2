package org.openhab.binding.atsadvanced.webservices.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.0-milestone2
 * 2014-04-23T16:43:57.558+02:00
 * Generated source version: 3.0.0-milestone2
 * 
 */
@WebServiceClient(name = "SyncReply", 
                  wsdlLocation = "http://127.0.0.1:1337/soap11",
                  targetNamespace = "ATSAdvanced.DTO") 
public class SyncReply extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("ATSAdvanced.DTO", "SyncReply");
    public final static QName BasicHttpBindingISyncReply = new QName("ATSAdvanced.DTO", "BasicHttpBinding_ISyncReply");
    static {
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:1337/soap11");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(SyncReply.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://127.0.0.1:1337/soap11");
        }
        WSDL_LOCATION = url;
    }

    public SyncReply(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SyncReply(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SyncReply() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SyncReply(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SyncReply(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SyncReply(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns ISyncReply
     */
    @WebEndpoint(name = "BasicHttpBinding_ISyncReply")
    public ISyncReply getBasicHttpBindingISyncReply() {
        return super.getPort(BasicHttpBindingISyncReply, ISyncReply.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ISyncReply
     */
    @WebEndpoint(name = "BasicHttpBinding_ISyncReply")
    public ISyncReply getBasicHttpBindingISyncReply(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingISyncReply, ISyncReply.class, features);
    }

}
