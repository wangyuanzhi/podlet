package dnspod.podlet.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.regex.Pattern;

import dnspod.podlet.api.DnsPodApi;
import dnspod.podlet.api.Domain;
import dnspod.podlet.api.DomainRecord;

/**
 * 
 * @author Wangyuanzhi
 *
 */
public class IpListener extends TimerTask {
	private static DnsPodApi api = DnsPodApi.getInstance();
	private static Pattern COMMA = Pattern.compile(",");

	private String lastestIp;

	private String email;
	private String password;

	private String domain;
	private Set<String> hostNameSet = new HashSet<String>();

	private boolean secureConnection = false;

	public IpListener(String email, String password, String domain,
			String hostNames, boolean secureConnection) {
		this.email = email;
		this.password = password;
		this.domain = domain;
		this.secureConnection = secureConnection;

		String[] hostnameArray = COMMA.split(hostNames);
		
		for (String hostname : hostnameArray) {
			hostNameSet.add(hostname.toLowerCase().trim());
		}
	}

	@Override
	public void run() {
		try {
			String ip = api.aboutIp(email, secureConnection);

			if (ip != null && !ip.equals(lastestIp)) {
				List<Domain> domainList = api.getDomainList(email, password,
						secureConnection);

				for (Domain domainObj : domainList) {
					if (!domainObj.getName().equalsIgnoreCase(domain)) {
						continue;
					}

					List<DomainRecord> drList = api.getRecordList(email,
							password, domainObj.getId(), secureConnection);

					for (DomainRecord domainRecord : drList) {
						if (!hostNameSet.contains(domainRecord.getName().toLowerCase().trim())) {
							continue;
						}

						api.modifyRecord(email, password,
										domainObj.getId(),
										domainRecord.getId(),
										domainRecord.getName(),
										domainRecord.getType(),
										domainRecord.getLine(),
										ip,
										domainRecord.getMx(),
										domainRecord.getTtl(),
										secureConnection);
					}
				}

				lastestIp = ip;
			}
		} catch (Exception e) {
			System.err.println("Error occurred while updating IP address. Will try agian in next interval.");
			e.printStackTrace();
		}
	}
}
