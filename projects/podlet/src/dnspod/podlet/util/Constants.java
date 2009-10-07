package dnspod.podlet.util;

/**
 * 
 * @author Wangyuanzhi
 *
 */
public interface Constants {
	String LOGIN_EMAIL		= "login_email";
	String LOGIN_PASSWORD	= "login_password";
	String DOMAIN_NAME		= "domain_name";
	String HOST_NAMES		= "host_names";
	String INTERVAL_TIME	= "interval_time";
	String SECURE_CONNECTION= "secure_connection";
	
	String RESPONSE_FORMAT_JSON	= "json";
	String RESPONSE_FORMAT_XML	= "xml";
	
	String API_ABOUT_IP			= "/About/IP";
	String API_DOMAIN_LIST		= "/API/Domain.List";
	String API_RECORD_LIST		= "/API/Record.List";
	String API_RECORD_MODIFY	= "/API/Record.Modify";
	
	String DNSPOD_DOMAIN = "www.dnspod.com";
	
	String REQUEST_DOMAIN_LIST = "login_email=%s&login_password=%s&format=%s";
	String REQUEST_RECORD_LIST = "login_email=%s&login_password=%s&format=%s&domain_id=%s";
	String REQUEST_RECORD_MODIFY = "login_email=%s&login_password=%s&format=%s&domain_id=%s&record_id=%s&sub_domain=%s&record_type=%s&record_line=%s&value=%s&mx=%s&ttl=%s";

	String RESPONSE_UPDATED_ON = "updated_on";
	String RESPONSE_ENABLED = "enabled";
	String RESPONSE_MX = "mx";
	String RESPONSE_VALUE = "value";
	String RESPONSE_TTL = "ttl";
	String RESPONSE_TYPE = "type";
	String RESPONSE_LINE = "line";
	String RESPONSE_RECORD = "record";
	String RESPONSE_RECORDS = "records";
	String RESPONSE_NAME = "name";
	String RESPONSE_ID = "id";
	String RESPONSE_DOMAIN = "domain";
	String RESPONSE_DOMAINS = "domains";
	String RESPONSE_CODE = "code";
	String RESPONSE_MESSAGE = "message";
	String RESPONSE_STATUS = "status";
}
