# Article REST API

# Installation

### 1 - Clone the repo
```
git clone [https://github.com/github_username/repo_name.git](https://github.com/GuerraXY/article-rest-api.git)
```
### 2 - Do a mvn clean install on the project
```
mvn clean install
```
### 3 - Run the Main app (RestApiApplication)


# Running
## The app handles three different types of Requests:
### <ins>POST Request</ins>
URL - **localhost:8080/article**

Payload example:
```
{
"url": "http://www.rte.ie/news/politics/2018/1004/1001034-cso/",
"socialScore": 20,
"countryCode": "ie"
}
```
### <ins>GET Request</ins>
URL - **localhost:8080/report/{countrycode}**

Response example:
```
[
{
"domain": "rte.ie",
"urls": 2,
"agg_social_score": "80"
},
{
"domain": "irishtimes.com",
"urls": 1,
"agg_social_score": "50"
}
]
```
### <ins>DELETE Request</ins>
URL - **localhost:8080/article?url={url}**

Response example (Deleted article):
```
{
"url": "http://www.rte.ie/news/politics/2018/1004/1001034-cso/",
"socialScore": 20,
"countryCode": "ie"
}
```
