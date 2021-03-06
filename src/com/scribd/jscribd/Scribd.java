package com.scribd.jscribd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.scribd.jscribd.resource.ScribdDocument;

/**
 *
 *
 */
public class Scribd {
	
	private static final int DEFAULT_SEARCH_LIMIT = 10;
	private static final int DEFAULT_SEARCH_OFFSET = 0;
	private static final String DEFAULT_FEATURED_SCOPE = "hot";

	private final Api api;
	
	public Scribd() {
		api = new ScribdApi();
	}
	
	public Scribd(Api api) {
		this.api = api;
	}
	
	public Api getApi() {
		return api;
	}
	
	/**
	 * For more information, see the Scribd API documentation:
	 * http://www.scribd.com/developers/api?method_name=docs.search
	 *
	 * @param scope The search scope.
	 * @param query The search query.
	 * @return A list of documents matching the search parameters.
	 */
	public List<ScribdDocument> findDocuments(String scope, String query) {
		return findDocuments(scope, query, DEFAULT_SEARCH_LIMIT, DEFAULT_SEARCH_OFFSET);
	}

	/**
	 * For more information, see the Scribd API documentation:
	 * http://www.scribd.com/developers/api?method_name=docs.search
	 *
	 * @param scope The search scope.
	 * @param query The search query.
	 * @param limit The maximum number of results to return. Translates to the "num_results" API parameter.
	 * @param offset The search result offset. Translates to the "num_start" API parameter.
	 * @return A list of documents matching the search parameters.
	 */
	public List<ScribdDocument> findDocuments(String scope, String query, int limit, int offset) {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("scope", scope);
		fields.put("query", query);
		fields.put("num_results", limit);
		fields.put("num_start", offset);

		Document xml = api.sendRequest("docs.search", fields);
		
		List<ScribdDocument> list = new ArrayList<ScribdDocument>();
		NodeList results = xml.getElementsByTagName("result");
		for (int i = 0; i < results.getLength(); i++) {
			list.add(new ScribdDocument(api, results.item(i)));
		}
		
		return list;
	}

    /**
     * For more information, see the Scribd API documentation:
     * http://www.scribd.com/developers/api?method_name=docs.featured
     *
     * @return A list of documents matching the search parameters.
     */
    public List<ScribdDocument> getFeaturedDocuments() {
        return getFeaturedDocuments(DEFAULT_FEATURED_SCOPE, DEFAULT_SEARCH_LIMIT, DEFAULT_SEARCH_OFFSET);
    }

    /**
     * For more information, see the Scribd API documentation:
     * http://www.scribd.com/developers/api?method_name=docs.featured
     *
     * @param scope The search scope.
     * @param limit The maximum number of results to return.
     * @param offset The search result offset.
     * @return A list of documents matching the search parameters.
     */
    public List<ScribdDocument> getFeaturedDocuments(String scope, int limit, int offset) {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("scope", scope);
        fields.put("limit", limit);
        fields.put("offset", offset);

        Document xml = api.sendRequest("docs.featured", fields);

        List<ScribdDocument> list = new ArrayList<ScribdDocument>();
        NodeList results = xml.getElementsByTagName("result");
        for (int i = 0; i < results.getLength(); i++) {
            list.add(new ScribdDocument(api, results.item(i)));
        }

        return list;
    }

    /**
     * For more information, see the Scribd API documentation:
     * http://www.scribd.com/developers/api?method_name=docs.getSettings
     *
     * @param doc_id The ID for the document.
     * @return The ScribdDocument corresponding to the ID.
     */
    public ScribdDocument getDocument(int doc_id) {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("doc_id", doc_id);

        Document xml = api.sendRequest("docs.getSettings", fields);
        NodeList results = xml.getElementsByTagName("rsp");

        if (results.getLength() < 1) {
            return null;
        }

        return new ScribdDocument(api, results.item(0));
    }
}
