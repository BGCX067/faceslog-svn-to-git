/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */

package cn.hexiao.flog.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Utilities class for building urls.  This class is meant to centralize the
 * logic behind building urls so that logic isn't duplicated throughout the
 * code.
 */
@SuppressWarnings("unchecked")
public final class URLUtilities {
    
    // non-intantiable
    private URLUtilities() {}
    
    
    /**
     * Get root url for a given weblog.  Optionally for a certain locale.
     */
    public static final String getWeblogURL(boolean absolute) {
        StringBuffer url = new StringBuffer();
        
//        if(absolute) {
//            url.append(RollerRuntimeConfig.getAbsoluteContextURL());
//        } else {
//            url.append(RollerRuntimeConfig.getRelativeContextURL());
//        }
//        
//        url.append("/").append(weblog.getHandle()).append("/");
//        
        
        
        return url.toString();
    }
    
    
    /**
     * Get url for a single weblog entry on a given weblog.
     */
    public static final String getWeblogEntryURL(
                                                 String entryAnchor,
                                                 boolean absolute) {
 
        
        StringBuffer url = new StringBuffer();
        
        url.append(getWeblogURL(absolute));
        url.append("entry/").append(encode(entryAnchor));
        
        return url.toString();
    }
    
    
    /**
     * Get url for a single weblog entry comments on a given weblog.
     */
    public static final String getWeblogCommentsURL(
                                                    String entryAnchor,
                                                    boolean absolute) {
        
        return getWeblogEntryURL(entryAnchor, absolute)+"#comments";
    }
    
    
    /**
     * Get url for a single weblog entry comment on a given weblog.
     */
    public static final String getWeblogCommentURL(
                                                   String entryAnchor,
                                                   String timeStamp,
                                                   boolean absolute) {
        
        return getWeblogEntryURL( entryAnchor, absolute)+"#comment-"+timeStamp;
    }
    
    
    /**
     * Get url for a collection of entries on a given weblog.
     */
    @SuppressWarnings("unchecked")
	public static final String getWeblogCollectionURL(
                                                      String category,
                                                      String dateString,
                                                      List tags,
                                                      int pageNum,
                                                      boolean absolute) {
        

        StringBuffer pathinfo = new StringBuffer();
        Map params = new HashMap();
        
        pathinfo.append(getWeblogURL( absolute));
        
        String cat = null;
        if(category != null && "/".equals(category)) {
            cat = null;
        } else if(category != null && category.startsWith("/")) {
            cat = category.substring(1);
        }
        
        if(cat != null && dateString == null) {
            pathinfo.append("category/").append(encode(cat));
            
        } else if(dateString != null && cat == null) {
            pathinfo.append("date/").append(dateString);  
        
        } else if(tags != null && tags.size() > 0) {
            pathinfo.append("tags/").append(getEncodedTagsString(tags));
        } else {
            if(dateString != null) params.put("date", dateString);
            if(cat != null) params.put("cat", encode(cat));
        }

        if(pageNum > 0) {
            params.put("page", Integer.toString(pageNum));
        }
        
        return pathinfo.toString() + getQueryString(params);
    }
    
    
    /**
     * Get url for a custom page on a given weblog.
     */
   
	public static final String getWeblogPageURL(
                                                String pageLink,
                                                String entryAnchor,
                                                String category,
                                                String dateString,
                                                List tags,
                                                int pageNum,
                                                boolean absolute) {
        
   
        
        StringBuffer pathinfo = new StringBuffer();
        Map params = new HashMap();
        
        pathinfo.append(getWeblogURL(absolute));
        
        if(pageLink != null) {
            pathinfo.append("page/").append(pageLink);
            
            // for custom pages we only allow query params
            if(dateString != null) {
                params.put("date", dateString);
            }
            if(category != null) {
                params.put("cat", encode(category));
            }
            if(tags != null && tags.size() > 0) {
                params.put("tags", getEncodedTagsString(tags));
            }
            if(pageNum > 0) {
                params.put("page", Integer.toString(pageNum));
            }
        } else {
            // if there is no page link then this is just a typical collection url
            return getWeblogCollectionURL(category, dateString, tags, pageNum, absolute);
        }
        
        return pathinfo.toString() + getQueryString(params);
    }
    
    
    /**
     * Get url for a feed on a given weblog.
     */
    public static final String getWeblogFeedURL(
                                                String type,
                                                String format,
                                                String category,
                                                List tags,
                                                boolean excerpts,
                                                boolean absolute) {
        

        
        StringBuffer url = new StringBuffer();
        
        url.append(getWeblogURL( absolute));
        url.append("feed/").append(type).append("/").append(format);
        
        Map params = new HashMap();
        if(category != null && category.trim().length() > 0) {
            params.put("cat", encode(category));
        }
        if(tags != null && tags.size() > 0) {
          params.put("tags", getEncodedTagsString(tags));
        }
        if(excerpts) {
            params.put("excerpts", "true");
        }
        
        return url.toString() + getQueryString(params);
    }
    
    
    /**
     * Get url to search endpoint on a given weblog.
     */
    public static final String getWeblogSearchURL(
                                                  String query,
                                                  String category,
                                                  int pageNum,
                                                  boolean absolute) {
        

        
        StringBuffer url = new StringBuffer();
        
        url.append(getWeblogURL( absolute));
        url.append("search");
        
        Map params = new HashMap();
        if(query != null) {
            params.put("q", encode(query));
            
            // other stuff only makes sense if there is a query
            if(category != null) {
                params.put("cat", encode(category));
            }
            if(pageNum > 0) {
                params.put("page", Integer.toString(pageNum));
            }
        }
        
        return url.toString() + getQueryString(params);
    }
    
    
    /**
     * Get url to a resource on a given weblog.
     */
    public static final String getWeblogResourceURL(
                                                    String filePath,
                                                    boolean absolute) {
        

        
        StringBuffer url = new StringBuffer();
        
        url.append(getWeblogURL( absolute));
        url.append("resource/");
        
        if(filePath.startsWith("/")) {
            url.append(filePath.substring(1));
        } else {
            url.append(filePath);
        }
        
        return url.toString();
    }
    
    
    /**
     * Get url to rsd file on a given weblog.
     */
    public static final String getWeblogRsdURL(
                                               boolean absolute) {
        

        
        return getWeblogURL(absolute)+"rsd";
    }
    
    
    /**
     * Get url to JSON tags service url, optionally for a given weblog.
     */
    public static final String getWeblogTagsJsonURL(
                                                    boolean absolute) {
        
        StringBuffer url = new StringBuffer();
        
//        if(absolute) {
//            url.append(RollerRuntimeConfig.getAbsoluteContextURL());
//        } else {
//            url.append(RollerRuntimeConfig.getRelativeContextURL());
//        }
        
        // json tags service base
        url.append("/roller-services/json/tags/");
        
        // is this for a specific weblog or site-wide?

        
        return url.toString();
    }
    
    
    /**
     * Get root url for a given *preview* weblog.  
     * Optionally for a certain locale.
     */
    public static final String getPreviewWeblogURL(String previewTheme,
                                                   
                                                   boolean absolute) {
        

        
        StringBuffer url = new StringBuffer();
        
//        if(absolute) {
//            url.append(RollerRuntimeConfig.getAbsoluteContextURL());
//        } else {
//            url.append(RollerRuntimeConfig.getRelativeContextURL());
//        }
//        
//        url.append("/roller-ui/authoring/preview/").append(weblog.getHandle()).append("/");
        
   
        
        Map params = new HashMap();
        if(previewTheme != null) {
            params.put("theme", encode(previewTheme));
        }
        
        return url.toString() + getQueryString(params);
    }
    
    
    /**
     * Get url for a given *preview* weblog entry.  
     * Optionally for a certain locale.
     */
    public static final String getPreviewWeblogEntryURL(String previewAnchor,
                                                       
                                                        boolean absolute) {
        

        StringBuffer url = new StringBuffer();
        
//        if(absolute) {
//            url.append(RollerRuntimeConfig.getAbsoluteContextURL());
//        } else {
//            url.append(RollerRuntimeConfig.getRelativeContextURL());
//        }
        
        //url.append("/roller-ui/authoring/preview/").append(weblog.getHandle()).append("/");
        
 
        
        Map params = new HashMap();
        if(previewAnchor != null) {
            params.put("previewEntry", encode(previewAnchor));
        }
        
        return url.toString() + getQueryString(params);
    }
    
    
    /**
     * Get a url to a *preview* resource on a given weblog.
     */
    public static final String getPreviewWeblogResourceURL(String previewTheme,
                                                           
                                                           String filePath,
                                                           boolean absolute) {
        

        
        StringBuffer url = new StringBuffer();
        
//        if(absolute) {
//            url.append(RollerRuntimeConfig.getAbsoluteContextURL());
//        } else {
//            url.append(RollerRuntimeConfig.getRelativeContextURL());
//        }
        
       // url.append("/roller-ui/authoring/previewresource/").append(weblog.getHandle()).append("/");
        
        if(filePath.startsWith("/")) {
            url.append(filePath.substring(1));
        } else {
            url.append(filePath);
        }
        
        Map params = new HashMap();
//        if(previewTheme != null && !Theme.CUSTOM.equals(previewTheme)) {
//            params.put("theme", encode(previewTheme));
//        }
        
        return url.toString() + getQueryString(params);
    }
    
    
    /**
     * Compose a map of key=value params into a query string.
     */
    public static final String getQueryString(Map<String, String> params) {
        
        if(params == null) {
            return null;
        }
        
        StringBuffer queryString = new StringBuffer();
        
        for(Iterator keys = params.keySet().iterator(); keys.hasNext();) {
            String key = (String) keys.next();
            String value =   params.get(key);
            
            if (queryString.length() == 0) {
                queryString.append("?");
            } else {
                queryString.append("&");
            }
            
            queryString.append(key);
            queryString.append("=");
            queryString.append(value);
        }
        
        return queryString.toString();
    }
    
    
    /**
     * URL encode a string using UTF-8.
     */
    public static final String encode(String str) {
        String encodedStr = str;
        try {
            encodedStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // ignored
        }
        return encodedStr;
    }
    
    
    /**
     * URL decode a string using UTF-8.
     */
    public static final String decode(String str) {
        String decodedStr = str;
        try {
            decodedStr = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // ignored
        }
        return decodedStr;
    }
    
    
    public static final String getEncodedTagsString(List tags) {
        StringBuffer tagsString = new StringBuffer();
        if(tags != null && tags.size() > 0) {
            String tag = null;
            Iterator tagsIT = tags.iterator();
            
            // do first tag
            tag = (String) tagsIT.next();
            tagsString.append(encode(tag));
            
            // do rest of tags, joining them with a '+'
            while(tagsIT.hasNext()) {
                tag = (String) tagsIT.next();
                tagsString.append("+");
                tagsString.append(encode(tag));
            }
        }
        return tagsString.toString();
    }
}
