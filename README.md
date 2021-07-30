## Intro
This is a webapi application for PMPanel now.

It will support SSPanel, V2board in future.

## How to use

- set a website with nginx, add the nginx conf below:
```conf
location /api/ {
  proxy_pass http://127.0.0.1:9090/;
  proxy_set_header Host $host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_http_version 1.1;
  proxy_set_header Connection "";
}
```
- download the webapi.jar and application.properties file in the same directory
- config the application.properties
```properties
# [dev, prod]
spring.profiles.active=dev
spring.datasource.url=jdbc:mysql://localhost:3306/panel?characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=123456

# [pmpanel, sspanel, v2board]
panel=pmpanel
key=123456
```
- execute jar with command below:
```shell
java -jar webapi.jar
```
-  if test successfully, please execute the jar with supervisor

## RESTFUL API for developers

Please see [here](https://github.com/ByteInternetHK/PMPanelWebapi/wiki/REST-API)