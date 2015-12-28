package cn.hexiao.flog.webservices.xmlrcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcServer;

public class FlogXMLRPCServlet extends HttpServlet {

	private static final long serialVersionUID = 8995816293711070922L;
	
private static Log mLogger = LogFactory.getLog(FlogXMLRPCServlet.class);
    
    private transient XmlRpcServer mXmlRpcServer = new XmlRpcServer();
    private BloggerAPIHandler mBloggerHandler = null;
    private MetaWeblogAPIHandler mMetaWeblogHandler = null;
    
    
    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        
        super.init(config);
        try {
            mBloggerHandler = new BloggerAPIHandler();
            mXmlRpcServer.addHandler("blogger", mBloggerHandler);
            
            mMetaWeblogHandler = new MetaWeblogAPIHandler();
            mXmlRpcServer.addHandler("metaWeblog", mMetaWeblogHandler);
        } catch (Exception e) {
            mLogger.error("Initialization of XML-RPC servlet failed", e);
        }
    }
    
    
    protected void service(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        InputStream is = request.getInputStream();
        
        if (mLogger.isDebugEnabled()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            mLogger.debug(sb.toString());
            is = new StringBufferInputStream(sb.toString());
        }
        
        // execute XML-RPC request
        byte[] result = mXmlRpcServer.execute(is);
        
        if (mLogger.isDebugEnabled()) {
            String output = new String(result);
            mLogger.debug(output);
        }
        
        response.setContentType("text/xml");
        response.setContentLength(result.length);
        OutputStream output = response.getOutputStream();
        output.write(result);
        output.flush();
    }

}
