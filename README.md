# simplefileserver
本项目主要使用技术spring boot,nginx,servlet,filter,derby,jetty等技术，使用了aes、rsa加密方式。server端提供文件的上传、下载、查询等接口，client端使用resttemplate调用这些接口，返回数据在前端展示。
### nginx 主要配置
```java 
upstream myclient{
	server localhost:9000;
	server localhost:9090;
}
server {
        listen       8000;
        server_name  localhost;

        location / {
		proxy_pass http://myclient;
        }
}  
````
### derby 数据库
```sql 
CREATE TABLE t_file ( 
	uuid char(64) NOT NULL,
	size INTEGER NOT NULL, 
	type varchar(64) NOT NULL, 
	originName varchar(160)  not null, 
	createTime timestamp not null,
	saveAddress varchar(160) not null,
	digitEnvelop varchar(512) not null,
	PRIMARY KEY(uuid )
);
```
