# SW캡스톤디자인 Spring Boot proejct

## 각자 로컬환경에 맞게 변경할것
* application.properties
* Swagger URL: http://localhost:{portnumber}/swagger-ui.html, portnumber는 스프링부트포트번호
## Specification
* 피드추천 알고리즘은 30분마다 실행된다.
* 회원가입 시 그 회원에 대해서 피드 추천 알고리즘이 실행된다.




* 신고기능 추가
    report table 추가
    ```sql
   create table report
   (
   	report_id int auto_increment
   		primary key,
   	user_id int null,
   	reported_user_id int null,
   	post_id int null,
   	review_id int null,
   	comment_id int null,
   	report_reason varchar(30) null,
   	created_at timestamp null
   );    
    ```


