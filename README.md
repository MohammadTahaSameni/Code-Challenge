# Solactive Code Challenge - MohammadTaha Sameni

Requirements 
Java 11, Maven

Compiling
Use the following command to work with project: mvn clean install

Execute
Use the following command to run with project: mvn spring-boot:run

Usage
All services will be availabe on: localhost:8080

Assumptions
•	Since I handle out-of-order messages, I need to sort the messages for a given Asset, which I do using a priority queue which internally uses a heap data structure.
•	I assume there are no more than 30K Assets. we can easy to increase this, since it's just a constant.
•	I assume when a tick's receive I need to recalculate the statistics and I use disruptor event handler that really efficient.

Improvement
there really should be logging, health check services, log aggregation mechanism (ELK), distributed tracing system, Prometheus system monitor etc.

Did I Like It ?
In all honesty, this was challenging and cool assignment. I did my best to do it the best I could.

