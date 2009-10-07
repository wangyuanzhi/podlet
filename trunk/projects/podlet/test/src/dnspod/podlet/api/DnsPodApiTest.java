package dnspod.podlet.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

import javax.script.ScriptException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Wangyuanzhi
 * 
 */
public class DnsPodApiTest {
	private static final String email = "dnspodlet@gmail.com";
	private static final String password = "dnspodlet";
	private static final String domainName = "sample.com";
	private static final String hostname = "forjunit";

	private static final DnsPodApi api = DnsPodApi.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDomainList() throws MalformedURLException,
			ProtocolException, IOException, ScriptException {
		List<Domain> domainList = api.getDomainList(email, password, true);

		System.out.println(domainList);

		String ip = api.aboutIp(email, true);

		for (Domain domain : domainList) {
			if (!domain.getName().equalsIgnoreCase(domainName)) {
				continue;
			}

			List<DomainRecord> drList = api.getRecordList(email, password,
					domain.getId(), true);

			for (DomainRecord domainRecord : drList) {
				if (!domainRecord.getName().equalsIgnoreCase(hostname)) {
					continue;
				}

				boolean result = api.modifyRecord(email, password, domain
						.getId(), domainRecord.getId(), domainRecord.getName(),
						domainRecord.getType(), domainRecord.getLine(), ip,
						domainRecord.getMx(), domainRecord.getTtl(), true);

				Assert.assertTrue(result);
			}
			System.out.println(drList);
		}
	}

	@Test
	public void testAboutIp() throws ProtocolException, IOException {
		String ip = api.aboutIp(email);

		System.out.println(ip);
	}

}
