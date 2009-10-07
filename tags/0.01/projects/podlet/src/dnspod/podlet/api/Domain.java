/**
 * 
 */
package dnspod.podlet.api;

/**
 * @author Wangyuanzhi
 * 
 */
public class Domain {
	private long id;
	private String name;
	private String status;
	private long records;

	public Domain() {
	}

	public Domain(long id, String name, String status, long records) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.records = records;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return String.format("{id=%s, name=%s, status=%s, records=%s}", id,
				name, status, records);
	}
}
