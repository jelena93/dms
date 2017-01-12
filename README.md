# dms
Add the following code to tomcat server.xml

<GlobalNamingResources>
<Resource
            name="jdbc/dms"
            auth="Container"
            type="javax.sql.DataSource"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            initialSize="5"
            maxActive="55"
            maxIdle="21"
            minIdle="13"
            timeBetweenEvictionRunsMillis="34000"
            minEvictableIdleTimeMillis="55000"
            validationQuery="SELECT 1"
            validationInterval="34"
            testOnBorrow="true"
            removeAbandoned="true"
            removeAbandonedTimeout="233"
            username="admin"
            password="admin"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://localhost:3306/dms?allowMultiQueries=true"
        />
</GlobalNamingResources>
