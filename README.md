spring-api-tester
=================

--------------------------------------------

this readme is outdated. i am in the process of switching this to work with spring boot. spring boot has a similar functionality, but i think it is slightly less nice. 

--------------------------------------------

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

and to support bootstrap glyphicon components add following in your web.xml as well:

		<url-pattern>*.svg</url-pattern>
		<url-pattern>*.eot</url-pattern>
		<url-pattern>*.woff</url-pattern>
		<url-pattern>*.ttf</url-pattern>

assuming there's no conflict with the rest of your application, the
apitester should now be available on:

		${your appserver}/{$context_path}/apitester
		
 		
