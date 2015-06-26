# Introduction #

This page describe how to use podlet.

# Details #

Step by step:
  * Register an account on http://www.dnspod.com
  * Configure your DNSPod account
  * Download podlet-mini-`*`.jar from project page
  * Copy it to your WEB-INF\lib
  * Add below content in your web.xml and then enjoy it (be sure you are using jre1.6 or above):
```
	<servlet>
		<servlet-name>Podlet</servlet-name>
		<servlet-class>dnspod.podlet.Podlet</servlet-class>
		<init-param>
			<!-- required -->
			<param-name>login_email</param-name>
			<param-value>dnspodlet@gmail.com</param-value>
		</init-param>
		<init-param>
			<!-- required -->
			<param-name>login_password</param-name>
			<param-value>dnspodlet</param-value>
		</init-param>
		<init-param>
			<!-- required. The domain name which need DDN support. -->
			<param-name>domain_name</param-name>
			<param-value>sample.com</param-value>
		</init-param>
		<init-param>
			<!--
				required. Host names which need DDNS support, separated by comma,
				ignore cases.
			-->
			<param-name>host_names</param-name>
			<param-value>@,www</param-value>
		</init-param>
		<init-param>
			<!--
				Optional, how many minutes once time to check public IP address.
				Default value is 60 minutes.
			-->
			<param-name>interval_time</param-name>
			<param-value>60</param-value>
		</init-param>
		<init-param>
			<!--
				Optional, connect to dnspod.com with HTTPS or not. Default value is
				false.
			-->
			<param-name>secure_connection</param-name>
			<param-value>false</param-value>
		</init-param>

		<load-on-startup>999</load-on-startup>
	</servlet>

```