/**
 * 
 */
package dnspod.podlet.api;


/**
 * @author Wangyuanzhi
 * 
 */
public class DomainRecord {
	private long id;
	private String name;
	private String line;
	private String type;
	private long ttl;
	private String value;
	private String mx;
	private boolean enabled;
	private String updatedOn;

	public DomainRecord() {
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMx() {
		return mx;
	}

	public void setMx(String mx) {
		this.mx = mx;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updateOn) {
		this.updatedOn = updateOn;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String
				.format(
						"{id=%s, name=%s, line=%s, type=%s, ttl=%s, value=%s, mx=%s, enabled=%s, updated_on=%s}",
						id, name, line, type, ttl, value, mx, enabled,
						updatedOn);
	}
}
