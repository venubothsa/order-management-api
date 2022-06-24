# ORDER MANAGEMENT API

Order management api is an appilcation which will do create, update and retrieve orders. <br>
Here i consider this application as large scale application and follow **CQRS(Command Query Responsibility Segregation) design pattern**.
![ordermanagemetdesign](https://github.com/venubothsa/order-management-api/blob/master/order_management.png)


## Prerequisites
1. JDK 8 and above
2. Intellij or STS
3. [Axon Server](https://developer.axoniq.io/download) 
4. [Apache Kafka](https://kafka.apache.org/downloads) 
5. Mongo DB

## Run Commands

To Run Kafka in local system

```
bin\windows>zookeeper-server-start.bat D:\tools\kafka\config\zookeeper.properties
bin\windows>kafka-server-start.bat D:\tools\kafka\config\server.properties
```
To Run Axon Server 

```
AxonServer-4.5.12>java -jar axonserver.jar
```



