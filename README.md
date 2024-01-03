# Pharmacy-Recommendation

## 요구사항 분석
* 약국 찾기 서비스
  * 약국들의 데이터를 가지고있다고 전제하고, 약국의 위치정보를 가지고있다.(서울시 공공데이터 활용)
  * 주소를 입력하여 요청하면 가까운 약국 3곳을 불러온다.
    * 정확한 주소를 입력 받기 위해 Kakao 우편번호 서비스 사용
  * 입력 받은 주소를 위도, 경도로 변환하여 약국 데이터들 중 가까운 약국을 찾는다
    * 지구는 구면이기 때문에 구에서의 최단 거리를 구하는 공식 필요
    * Haversine formula 사용
    * 완전한 구형은 아니므로 오차가 있다.
  * 입력한 주소 정보에서 반경 10km 내에 있는 약국만 추천 한다.
  * 추천한 약국 데이터는 길안내 URL 및 로드뷰 URL로 제공한다.
  * 길안내 URL은 고객에게 제공되기 때문에 가독성을 위해 shorten url로 제공한다.
  * shorten url에 사용되는 key값은 인코딩하여 제공한다.
    * ex) http://localhost:8080/dir/abcd
    * base62를 통한 인코딩

## Tech Stack
* JDK 11 
* Spring Boot 2.6.7 
* Spring Data JPA 
* Gradle 
* Handlebars 
* Lombok 
* Github 
* Docker 
* AWS EC2 
* Redis
* MariaDB 
* Spock 
* Testcontainers

## Result
![search](https://github.com/jsh9057/Pharmacy-Recommendation/assets/31503178/2be9cb39-bb46-4ac6-b04f-9ff4c923a3b1)
![recommendation](https://github.com/jsh9057/Pharmacy-Recommendation/assets/31503178/41a8008e-0614-4a81-95fd-23b2bd4dde08)
![kakao-api](https://github.com/jsh9057/Pharmacy-Recommendation/assets/31503178/fc1b8bc5-ae56-46c8-b253-c6f9715e3f42)
