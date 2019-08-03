# iso8583-client-server

java -jar ./target/iso8583-client-server-0.0.1-SNAPSHOT.jar

-Dspring.profiles.active=[server,client,echotest,hsm]

-Diso8583.server.host=localhost

-Diso8583.server.port=9090

-Diso8583.server.header.length=4

-Diso8583.client.header.length=4

-Diso8583.hsm.host=localhost

-Diso8583.hsm.port=7070

-Diso8583.hsm.header.length=2

# Links
http://localhost:8080/h2
