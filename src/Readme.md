#Project Manual

Single-threaded Server-Client application which provides the service of Key-Value store with PUT
/GET/DELETE operations.

### Instructions to execute

To execute TCP server:  
Syntax &nbsp;&nbsp; : `java -jar TCPServer.jar <port-number>`
<br>
Example : `java -jar TCPServer.jar 3000`

To execute TCP client:<br>
Syntax &nbsp;&nbsp; : `java -jar TCPClient.jar <ip-address> <port-number>`
<br>
Example : `java -jar TCPClient.jar 127.0.0.1 3000`

To execute UDP server:  
Syntax &nbsp;&nbsp; : `java -jar UDPServer.jar <port-number>`
<br>
Example : `java -jar UDPServer.jar 3000`

To execute UDP client:<br>
Syntax &nbsp;&nbsp; : `java -jar UDPClient.jar <ip-address> <port-number>`
<br>
Example : `java -jar UDPClient.jar 127.0.0.1 3000`

To specify operations: <br>
`PUT/GET/DELETE any of them can be provided.`<br>
`Operations are case-insensitive.`