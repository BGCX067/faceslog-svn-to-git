package cn.hexiao.flog.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to localize the modification date header-related logic.
 */
public class ModDateHeaderUtil {
    private static final Log log = LogFactory.getLog(ModDateHeaderUtil.class);

    // Utility class with static methods; inhibit construction
    private ModDateHeaderUtil() {

    }

    /**
     * Sets the HTTP response status to 304 (NOT MODIFIED) if the request contains an
     * If-Modified-Since header that specifies a time that is
     * at or after the time specified by the value of lastModifiedTimeMillis
     * <em>truncated to second granularity</em>.  Returns true if
     * the response status was set, false if not.
     *
     * @param request
     * @param response
     * @param lastModifiedTimeMillis
     * @return true if a response status was sent, false otherwise.
     */
    public static boolean respondIfNotModified(HttpServletRequest request,
                                               HttpServletResponse response,
                                               long lastModifiedTimeMillis) {
        long sinceDate = request.getDateHeader("If-Modified-Since");
        // truncate to seconds
        lastModifiedTimeMillis -= (lastModifiedTimeMillis % 1000);
        log.debug("since date = " + sinceDate);
        log.debug("last mod date (trucated to seconds) = " + lastModifiedTimeMillis);
        if (lastModifiedTimeMillis <= sinceDate) {
            log.debug("NOT MODIFIED " + request.getRequestURL());
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set the Last-Modified header using the given time in milliseconds.  Note that because the
     * header has the granularity of one second, the value will get truncated to the nearest second that does not
     * exceed the provided value.
     * <p/>
     * This will also set the Expires header to a date in the past.  This forces clients to revalidate the cache each
     * time.
     *
     * @param response
     * @param lastModifiedTimeMillis
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedTimeMillis) {
        response.setDateHeader("Last-Modified", lastModifiedTimeMillis);
        // Force clients to revalidate each time
        // See RFC 2616 (HTTP 1.1 spec) secs 14.21, 13.2.1
        response.setDateHeader("Expires", 0);
        // We may also want this (See 13.2.1 and 14.9.4)
        // response.setHeader("Cache-Control","must-revalidate");
    }
}

