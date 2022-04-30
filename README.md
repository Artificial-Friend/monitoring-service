# Monitoring Service
## Launching  
Download Maven, go to project folder do ```mvn package```  
Download Docker, go to project do ```docker-compose up```  
Download the Internet.  
<br/>
There are hardcoded users:
```
{
name: "riddler",
email: "riddler",
accessToken: "1337-riddler-1337"
},
{
name: "joker",
email: "joker",
accessToken: "777-joker-777"
}
```


## Gateways
every endpoint uses header "accessToken" for user identification

##### Monitored Endpoint
Body: 
```
{
    "name": "google",
    "url": "https://google.com",
    "monitoredInterval": 10
}
```  

GET http://localhost:8080/monitored-endpoint/all - get all endpoints by user  
POST http://localhost:8080/monitored-endpoint/create - create endpoint for monitoring with body  
POST http://localhost:8080/monitored-endpoint/{endpointId} - edit endpoint by id with body  
DELETE http://localhost:8080/monitored-endpoint/delete/{endpointId} - delete by id  
##### Monitored Result
GET http://localhost:8080/monitoring-result with parameter "url" to get 10 last results for current user by url  
<br />
<br />
<br />
<br />

<details><summary>TODO</summary>
<p>
 
- ~~Docker~~
- Spring Security
- Spring Gateway
- Deploy Cloud
  - Connect with Telegram bot or Firebase notifications
- Optimize scheduler
  - RabbitMQ
- Make Results as separate service
 
</p>
</details>  

details><summary>LTS</summary>
<p>
 
- Management
    - Anroid app
    - UI web page
 
</p>
</details>
