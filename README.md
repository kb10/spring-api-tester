spring-api-tester
=================

spring-api-tester is an application that scans your spring application 
for API controllers, tries to extract information from them, and 
display them in a simple angular / bootstrap app. 

Quickstart
=================

to use, simply clone a tag from 

	https://github.com/rmalchow/spring-api-tester

build and deploy to a repo of your choice (as of now, this is not in 
any public repo);

then add these dependencies to your WAR module:

		<dependency>
			<groupId>cinefms-apitester</groupId>
			<artifactId>apitester-spring-mvc</artifactId>
			<version>$VERSION</version>
		</dependency>
		<dependency>
			<groupId>cinefms-apitester</groupId>
			<artifactId>apitester-webapp</artifactId>
			<version>$VERSION</version>
			<type>war</type>
		</dependency>
 
 and add the apitester dispatcher servlet config to your web.xml:
 
 
		<servlet>
			<servlet-name>apitester</servlet-name>
			<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
			<init-param>
		        <param-name>contextConfigLocation</param-name>
		        <param-value>WEB-INF/apitester/servlet-config.xml</param-value>
	    	</init-param>
	    	<load-on-startup>1</load-on-startup>
		</servlet>
		<servlet-mapping>
			<servlet-name>apitester</servlet-name>
			<url-pattern>/apitester/api/*</url-pattern>
		</servlet-mapping>

assuming there's no conflict with the rest of your application, the
apitester should now be available on:

		${your appserver}/{$context_path}/apitester
		
 		