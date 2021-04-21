# Code Challenge - MohammadTaha Sameni

**Requirements**<br/>
Java 11, Maven

**Compiling**<br/>
Use the following command to work with project:<br/>
***mvn clean install***

**Execute**<br/>
Use the following command to run with project:<br/>
***mvn spring-boot:run***

**Usage**<br/>
All services will be availabe on: localhost:8080

**Assumptions**<br/>
•	Since I handle out-of-order messages, I need to sort the messages for a given Asset, which I do using a priority queue which internally uses a heap data structure.<br/>
•	I assume there are no more than 30K Assets. we can easy to increase this, since it's just a constant.<br/>
•	I assume when a tick's receive I need to recalculate the statistics and I use disruptor event handler that really efficient.<br/>

**Improvement**<br/>
there really should be logging, health check services, log aggregation mechanism (ELK), distributed tracing system, Prometheus system monitor etc.

**Did I Like It?**<br/>
In all honesty, this was challenging and cool assignment. I did my best to do it the best I could.

