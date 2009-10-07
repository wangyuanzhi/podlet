package dnspod.podlet.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.script.ScriptException;

import dnspod.podlet.util.Constants;
import dnspod.podlet.util.JsonParser;

/**
 * 
 * @author Wangyuanzhi
 * 
 */
public class DnsPodApi {
	private static URL ABOUT_IP;
	private static URL ABOUT_IP_SECURE;

	private static URL DOMAIN_LIST;
	private static URL DOMAIN_LIST_SECURE;

	private static URL RECORD_LIST;
	private static URL RECORD_LIST_SECURE;

	private static URL RECORD_MODIFY;
	private static URL RECORD_MODIFY_SECURE;

	private static final String SEPARATOR;

	private static Properties PROPERTIES = new Properties();;

	static {
		SEPARATOR = System.getProperty("line.separator");
		try {
			ABOUT_IP = new URL(String.format("http://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_ABOUT_IP));
			ABOUT_IP_SECURE = new URL(String.format("https://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_ABOUT_IP));

			RECORD_LIST = new URL(String.format("http://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_RECORD_LIST));
			RECORD_LIST_SECURE = new URL(String.format("https://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_RECORD_LIST));

			DOMAIN_LIST = new URL(String.format("http://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_DOMAIN_LIST));
			DOMAIN_LIST_SECURE = new URL(String.format("https://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_DOMAIN_LIST));

			RECORD_MODIFY = new URL(String.format("http://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_RECORD_MODIFY));
			RECORD_MODIFY_SECURE = new URL(String.format("https://%s%s",
					Constants.DNSPOD_DOMAIN, Constants.API_RECORD_MODIFY));
		} catch (MalformedURLException e) {
			// Ignore this
			System.err.println("Initial url list failed.");
		}

		InputStream in = DnsPodApi.class.getResourceAsStream("DnsPodApi.xml");
		try {
			PROPERTIES.loadFromXML(in);
		} catch (Exception e) {
			// Ignore this
			System.err.println("Load properties file failed.");
		}
	}

	private static final String DEFAULT_FORMAT = Constants.RESPONSE_FORMAT_JSON;
	private volatile static DnsPodApi instance;

	public static DnsPodApi getInstance() {
		return getInstance(DEFAULT_FORMAT);
	}

	private static DnsPodApi getInstance(String responseFormat) {
		if (instance == null) {
			synchronized (DnsPodApi.class) {
				if (instance == null) {
					instance = new DnsPodApi();
				}
			}
		}

		return instance;
	}

	public String getResponseFormat() {
		return Constants.RESPONSE_FORMAT_JSON;
	}

	public boolean modifyRecord(String email, String password, long domainId,
			long recordId, String subDomain, String recordType,
			String recordLine, String value, String mx, long ttl)
			throws ScriptException, IOException {
		return modifyRecord(email, password, domainId, recordId, subDomain,
				recordType, recordLine, value, mx, ttl, false);
	}

	public boolean modifyRecord(String email, String password, long domainId,
			long recordId, String subDomain, String recordType,
			String recordLine, String value, String mx, long ttl,
			boolean secureConnection) throws ScriptException, IOException {
		String param = String.format(Constants.REQUEST_RECORD_MODIFY, email,
				password, getResponseFormat(), domainId, recordId, subDomain,
				recordType, recordLine, value, mx, ttl);
		URL url = secureConnection ? RECORD_MODIFY_SECURE : RECORD_MODIFY;

		String responseStr = getResponse(url, param, email);

		@SuppressWarnings("unchecked")
		Map<String, Object> response = (Map<String, Object>) JsonParser
				.evalJsStr(responseStr);

		@SuppressWarnings("unchecked")
		Map<String, Object> status = (Map<String, Object>) response
				.get(Constants.RESPONSE_STATUS);

		if (!"1".equals(status.get(Constants.RESPONSE_CODE))) {
			throw new DnsPodException(String.valueOf(status
					.get(Constants.RESPONSE_MESSAGE)));
		}

		return true;
	}

	public List<DomainRecord> getRecordList(String email, String password,
			long domainId) throws IOException, ScriptException {
		return getRecordList(email, password, domainId, false);
	}

	public List<DomainRecord> getRecordList(String email, String password,
			long domainId, boolean secureConnection) throws IOException,
			ScriptException {
		String param = String.format(Constants.REQUEST_RECORD_LIST, email,
				password, getResponseFormat(), domainId);
		URL url = secureConnection ? RECORD_LIST_SECURE : RECORD_LIST;

		String responseStr = getResponse(url, param, email);

		@SuppressWarnings("unchecked")
		Map<String, Object> response = (Map<String, Object>) JsonParser
				.evalJsStr(responseStr);

		@SuppressWarnings("unchecked")
		Map<String, Object> status = (Map<String, Object>) response
				.get(Constants.RESPONSE_STATUS);

		if (!"1".equals(status.get(Constants.RESPONSE_CODE))) {
			throw new DnsPodException(String.valueOf(status
					.get(Constants.RESPONSE_MESSAGE)));
		}

		return getDomainRecords(response);
	}

	// "/API/Domain.List"
	public List<Domain> getDomainList(String email, String password)
			throws MalformedURLException, IOException, ProtocolException,
			ScriptException {
		return getDomainList(email, password, false);
	}

	// {"status":{"code":"1","message":"Get domain list success.","created_at":"2009-10-06 16:58:07"},
	// "domains":{"domain":[{"id":241485,"name":"hotinno.info","status":"1","records":"4"}]}}
	public List<Domain> getDomainList(String email, String password,
			boolean secureConnection) throws MalformedURLException,
			IOException, ProtocolException, ScriptException {
		String param = String.format(Constants.REQUEST_DOMAIN_LIST, email,
				password, getResponseFormat());
		URL url = secureConnection ? DOMAIN_LIST_SECURE : DOMAIN_LIST;

		String responseStr = getResponse(url, param, email);

		@SuppressWarnings("unchecked")
		Map<String, Object> response = (Map<String, Object>) JsonParser
				.evalJsStr(responseStr);

		@SuppressWarnings("unchecked")
		Map<String, Object> status = (Map<String, Object>) response
				.get(Constants.RESPONSE_STATUS);

		if (!"1".equals(status.get(Constants.RESPONSE_CODE))) {
			throw new DnsPodException(String.valueOf(status
					.get(Constants.RESPONSE_MESSAGE)));
		}

		return getDomains(response);
	}

	protected List<Domain> getDomains(Object domains) {
		List<Domain> result = new ArrayList<Domain>();

		@SuppressWarnings("unchecked")
		Map<String, Object> domainMap = (Map<String, Object>) ((Map<String, Object>) domains)
				.get(Constants.RESPONSE_DOMAINS);

		Object[] domainArray = (Object[]) domainMap.get(Constants.RESPONSE_DOMAIN);

		for (Object object : domainArray) {
			result.add(getDomain(object));
		}

		return result;
	}

	protected Domain getDomain(Object obj) {
		@SuppressWarnings("unchecked")
		Map<String, Object> domain = (Map<String, Object>) obj;

		if (domain != null) {
			Domain d = new Domain();

			d.setId(((Double) domain.get(Constants.RESPONSE_ID)).longValue());
			d.setName((String) domain.get(Constants.RESPONSE_NAME));
			d.setStatus((String) domain.get(Constants.RESPONSE_STATUS));
			d.setRecords(Integer.parseInt((String) domain
					.get(Constants.RESPONSE_RECORDS)));

			return d;
		}

		return null;
	}

	protected List<DomainRecord> getDomainRecords(Object domains) {
		List<DomainRecord> result = new ArrayList<DomainRecord>();

		@SuppressWarnings("unchecked")
		Map<String, Object> domainMap = (Map<String, Object>) ((Map<String, Object>) domains)
				.get(Constants.RESPONSE_RECORDS);

		Object[] domainArray = (Object[]) domainMap.get(Constants.RESPONSE_RECORD);

		for (Object object : domainArray) {
			result.add(getDomainRecord(object));
		}

		return result;
	}

	protected DomainRecord getDomainRecord(Object obj) {
		@SuppressWarnings("unchecked")
		Map<String, Object> domain = (Map<String, Object>) obj;

		if (domain != null) {
			DomainRecord dr = new DomainRecord();

			dr.setId(Long.parseLong((String) domain.get(Constants.RESPONSE_ID)));
			dr.setName((String) domain.get(Constants.RESPONSE_NAME));
			dr.setLine((String) domain.get(Constants.RESPONSE_LINE));
			dr.setType((String) domain.get(Constants.RESPONSE_TYPE));
			dr.setTtl(Long.parseLong((String) domain.get(Constants.RESPONSE_TTL)));
			dr.setValue((String) domain.get(Constants.RESPONSE_VALUE));
			dr.setMx((String) domain.get(Constants.RESPONSE_MX));
			dr.setEnabled("1".equals(domain.get(Constants.RESPONSE_ENABLED)));
			dr.setUpdatedOn((String) domain.get(Constants.RESPONSE_UPDATED_ON));

			return dr;
		}

		return null;
	}

	public String aboutIp(String email) throws ProtocolException, IOException {
		return aboutIp(email, false);
	}

	public String aboutIp(String email, boolean secureConnection)
			throws ProtocolException, IOException {
		return getIPAddress(secureConnection ? ABOUT_IP_SECURE : ABOUT_IP,
				email);
	}

	private String getResponse(URL url, String param, String email)
			throws IOException {
		HttpURLConnection conn = null;
		Writer out = null;
		BufferedReader in = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			conn.setDoInput(true);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			String userAgent = String.format("%s/%s (%s)", PROPERTIES
					.get("UserAgent"), PROPERTIES.get("Version"), email);
			conn.setRequestProperty("User-Agent", userAgent);

			if (param != null) {
				conn.setDoOutput(true);

				conn.setRequestProperty("Content-Length", String.valueOf(param
						.length()));

				out = new BufferedWriter(new OutputStreamWriter(conn
						.getOutputStream()));
				out.write(param);
				out.flush();
			} else {
				conn.setRequestProperty("Content-Length", String.valueOf(0));
			}

			InputStream is = conn.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append(SEPARATOR);
			}

			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Ignore this
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// Ignore this
				}
			}

			if (conn != null) {
				conn.disconnect();
			}
		}

	}

	private String getIPAddress(URL url, String email) throws IOException,
			ProtocolException {
		return getResponse(url, null, email).trim();
	}
}
