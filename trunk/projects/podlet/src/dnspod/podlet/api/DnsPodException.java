/**
 * 
 */
package dnspod.podlet.api;

/**
 * @author Wangyuanzhi
 * 
 */
public class DnsPodException extends RuntimeException {

	private static final long serialVersionUID = 8763740996135155791L;

	public DnsPodException() {
		super();
	}

	/**
	 * @param message
	 */
	public DnsPodException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DnsPodException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DnsPodException(String message, Throwable cause) {
		super(message, cause);
	}

}
