/**
 * 
 */
package dnspod.podlet;

import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import dnspod.podlet.listener.IpListener;
import dnspod.podlet.util.Constants;

/**
 * @author Wangyuanzhi
 * 
 */
@SuppressWarnings("serial")
public class Podlet extends HttpServlet {
	private IpListener ipListener;
	private Timer timer;

	@Override
	public void init() throws ServletException {
		super.init();

		boolean result = setup();
		if (!result) {
			System.err
					.println("Required parameter is missing, Podlet will not work.");

			return;
		}

	}

	private boolean setup() {
		boolean result = true;

		String email;
		String password;

		String domain;
		String hostNames;

		boolean secureConnection = false;

		long intervalTime = 60;

		email = getInitParameter(Constants.LOGIN_EMAIL);
		password = getInitParameter(Constants.LOGIN_PASSWORD);

		domain = getInitParameter(Constants.DOMAIN_NAME);
		hostNames = getInitParameter(Constants.HOST_NAMES);

		String itStr = getInitParameter(Constants.INTERVAL_TIME);

		if (itStr != null) {
			try {
				intervalTime = Long.parseLong(itStr);
			} catch (NumberFormatException e) {
				System.err
						.println(String
								.format(
										"interval_time (%s) is not a number, user default value (%s) for it.",
										itStr, intervalTime));
			}
		}

		secureConnection = Boolean
				.parseBoolean(getInitParameter(Constants.SECURE_CONNECTION));

		if (email == null) {
			System.err.println("login_email is missing.");
			result = false;
		}

		if (password == null) {
			System.err.println("login_password is missing.");
			result = false;
		}

		if (domain == null) {
			System.err.println("domain_name is missing.");
			result = false;
		}

		if (hostNames == null) {
			System.err.println("host_names is missing.");
			result = false;
		}

		if (result) {
			timer = new Timer("Podlet", true);

			ipListener = new IpListener(email, password, domain, hostNames,
					secureConnection);

			timer.scheduleAtFixedRate(ipListener, 1000,
					intervalTime * 60 * 1000);
		}

		return result;
	}

	@Override
	public void destroy() {
		super.destroy();

		if (ipListener != null) {
			ipListener.cancel();
		}

		if (timer != null) {
			timer.cancel();
		}
	}
}
