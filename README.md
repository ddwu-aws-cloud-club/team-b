## 프로젝트 소개

수강 신청을 진행하는 모의 프로젝트

https://github.com/ddwu-aws-cloud-club/team-b

> 요구사항 및 시나리오 정의
> 
- 수강신청 서비스는 새학기가 시작될 때 사용자가 몰릴 것이다.
    - 특정 기간에 사용자 트래픽이 과중된다.
    - 트래픽 분산이 필요하다.
- 사용자의 데이터와 서비스의 안정성을 위해 이중화 구성(Multi - AZ)이 필요하다.
    - 사용자 1 ~ n 명의 경우 인스턴스 경량화가 필요하다.
    - 사용자가 100명이 될 경우, 관리형 서비스가 필요할 것이다.
    - 사용자가 1000명이상이 될 경우, 부하를 줄이기 위한 아키텍처 구성이 필요하다. ← 이 부분에 집중

# 아키텍처 설계

- 적은 사용자에 대한 고려
    
    ## 사용자가 100명 이하인 경우
    
    ### [ 고려 사항 ]
    
    - 기본 아키텍처
        - 적절한 인스턴스 선택
        - 인스턴스 경량화 - 데이터베이스 분리, web/was 분리 → 3 Tier 구축
        - 기본 보안 및 모니터링
    - 비용 효율적인 구성
    
    ### **[아키텍처 구성]**
    
    - 단일 퍼블릭 서브넷 → Public과 Private으로 분리함으로써 외부 사용자가 접근하는 네트워크와 데이터가 저장된 네트워크를 나눈다.
    - 인스턴스 경량화를 위해 WEB/WAS 역할을 하는 인스턴스와 DB 역할을 하는 인스턴스를 분리한다.
    - 사용자는 Route53이라고 하는 DNS서비스를 통해 Public IP를 찾아서 AWS VPC 안에 구성된 인스턴스와 연결하여 서비스를 이용한다.
    - 퍼블릭 서브넷에 있는 탄력적 ip 가 사용되는 Instance는 Web과 WAS 역할을 하고 프라이빗 서브넷에 있는 인스턴스는 데이터베이스 역할을 한다.
    - 기본 모니터링
        - CloudWatch를 통한 리소스 메트릭 및 로그 모니터링
        - 자동 대시보드 생성, 임계치 초과 알람(Slack 연결)
    - 2계층 구성
    
    - 인스턴스 경량화 - 3tier 분리 구축
        - public subnet
            - web server
        - private subnet
            - was
            - db
    
    ![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/7ebcda5f-d375-41ff-845c-0587c06c6079)


    
    ## 사용자가 1000명인 경우
    
    여전히 소규모 서비스이기는 하지만 사용자와 트래픽 증가에 대한 대비를 미리 해보자!
    
    → 고 가용성에 집중을 하자 
    

### [ 아키텍처 설계 ]

1. HA(고가용성)을 위한 방안 
- 이중화
- 다중 가용영역 (Multi -AZ)
    
    서비스를 보다 안전하게 구성하려면 1개 이상의 가용 영역을 사용해야 한다. 
    
    단일 인스턴스, 단일 데이터베이스 개수를 늘리는 Scale Out
    
    - ELB를 이용한 수평적 확장
        - 인스턴스 개수가 여러 개일 때 트래픽을 어떻게 처리할까? → 인스턴스 앞 단에서 사용자 트래픽을 받아서 각 인스턴스에게 분배해줘야 한다.
        - ELB를 사용함으로써 트래픽을 받아 부하를 분산시키고 가용성을 높여줄 수 있다.
1. 트래픽 분산
- 트래픽 분산 대상
    1. EC2 인스턴스
    2. 컨테이너
    3. IP 주소
- 다중 가용 영역 지원
- 자동으로 용량 확장
- 오토 스케일 그룹 지원(자동으로 인스턴스를 ELB에 등록하고 제외)
- ELB Service
    - ALB(Application Load Balancer)
        - 고가용성, 자동확장
        - L7기반 로드 밸런서
        - 컨텐츠 기반 라우팅
        - HTTP, HTTPS, HTTP/2 지원
        - 헬스 체크
        - 세션 유지
        - 모니터링/로깅

## Infra

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/9913c160-f44a-49f1-a7c8-c68e259acf10)


최종 서비스 아키텍처

### CICD

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/868542d9-3095-4209-a48f-2dc6d611e3cc)

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/1125e815-49ca-4826-8461-53cf3b970cc3)


### 사용 기술

**WAF (**Web Application Firewall)

Application Load Balancer 앞에 WAF를 통해 웹 서버 자원에 대해 유입되는 HTTP(S) 트래픽을 분석하고 차단하였습니다.

WAF는 Access Control list를 정의하고, 탐지 Rule을 설정함으로써 내부 자원을 보호합니다.

**이중화 구성을 통한 안정성 개선**

- 다중 가용영역 활용
- 로드 밸런서를 통한 서버 이중화
    - ALB를 통해 요청을 적절히 배분하였습니다.
- 데이터베이스 이중화
    - Master-Slave 구조 선택(=Primary-Secondary 구조)
        
        **트래픽이 늘어남**에 따라 자연스럽게 **병목현상**이 생길 수 밖에 없으며, **쓰기**는 **원본 서버에서만 수행**하게 하고 **읽기**는 **원본의 복제 서버**에서 읽어오게 한다면 쓰기의 기능과 읽기의 기능을 향상시켰습니다.
        

## Monitoring

### **아키텍처 구성**

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/76399227-0168-4763-8f06-455b82a9261c)


> prometheus (:9090)
> 

[](http://ec2-3-35-100-16.ap-northeast-2.compute.amazonaws.com:9090/)

> alertmanager (:9093)
> 

[](http://ec2-3-35-100-16.ap-northeast-2.compute.amazonaws.com:9093/)

- 이벤트 브릿지는 비용문제도 그렇고 굳이 한 다리를 거쳐서 진행 할 필요성을 느끼지 못했습니다.
- node exporter 모니터링 중 조건(위험 감지)이 만족되면 알림을 띄우게 됩니다.
- 다음 기본적인 다섯 가지 규칙에 엇나가면 slack 채널에 자동으로 전송됩니다.(현재 개인 채널에서 확인)
    
    <aside>
    💡 **instanceDown**
    - 인스턴스가 다운되었을 때
    **HostOutOfMemory**
    - 메모리가 부족한 경우(10% 미만)
    **HostMemoryUnderMemoryPressure**
    - 메모리 압력이 높아지는 경우
    **HostOutOfDiskSpace**
    - 디스크 공간이 부족한 경우
    **HostHighCpuLoad**
    - CPU 부하가 높아지는 경우
    
    </aside>
    
    ![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/1e69e52e-55f3-4415-8583-c08eda609c88)

    **HostOutOfMemory -** 메시지가 가는지 확인을 위해 우선 100% 미만으로 설정함
    

> grafana (:3000)
> 

- cpu, memory, network, Disk, File System 상태를 대쉬보드를 통해 모니터링할 수 있음
- 현재 Node Exporter로 was의 정보를 가져오며 이를 위해 Node Exporter Full 대쉬보드를 사용하였음.
    
    ![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/dbd73351-888d-4be1-a286-1d438a5bcb89)

    

### **엔드 포인트**

> nodeExporter (:9100) from WAS
> 
- WAS 서버의 메트릭을 수집해서 엔드포인트에 노출

### 부하 테스트

> Apache Bench
> 

<aside>
💡 ab -n 100 -c 10 -p “courseId.txt” -T “application/json” http://api

</aside>

- 15000명이 15000번 요청한다.

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/2780d438-ab5e-4b9c-a28e-ba8ccf304819)


request 별 반응 시간

## Back-End

### Redis 대기열

**💡 레디스 사용 이유**

- 이번 포스팅에선, 레디스에서 제공하는 자료구조 중 하나인 **Sorted Set**을 활용하여 `모든 요청이 DB에 바로 부하가 가지 않고 차례대로 일정 범위만큼씩 처리`하는 구성
- 선착순 이벤트 시 대기하고 있는 인원에 대해 대기열 순번을 표출하기 용이

- **❓ Sorted Set이란?**
    
    ![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/6b1c08dc-cdc5-4a4b-8bcb-6bf474d5f0d4)

    
    - Sorted Sets은 **key 하나에 여러개의 score와 value**로 구성하는 자료구조입니다.
    - Value는 score로 sort되며 중복되지 않습니다.
    - Score가 같으면 value로 sort됩니다.
    - Sorted Sets에서는 집합이라는 의미에서 value를 member라 부릅니다.
    - Sorted Sets은 주로 sort가 필요한 곳에 사용됩니다.
    
    > 간단히 정리하자면, 한 Key에 여러 value와 score를 가지고 있으며 중복되지 않는 value로 score순으로 데이터를 정렬합니다
    > 

**기프티콘 선착 이벤트 구조 - Sorted Set 을 통한 이벤트 구현**

- Sorted Set Key에는 **EVENT** 를 설정합니다.
- Value에는 **사용자명(Pir, David, Foo, John)**을 설정합니다. 해당 예제는 사용자명으로 했지만 사용자명이 중복일 경우, 사용자에 대한 `고유한 값`으로 세팅하면 됩니다.
- Score에는 `참여한 사람들을 순서대로 정렬`하기 위해, 이벤트를 참여한 시간을 **유닉스타임(m/s)** 값으로 넣어줍니다.

### 📌 **어플리케이션 구성 -** 30개 선착순 이벤트(자리가 30개 있는 경우)시 100명이 요청했을 경우

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/a4552989-605c-4db4-9b35-3dfb5e00379d)


**(1)** 100명의 유저가 수강신청을 진행합니다.

**(2)** 100명의 유저는 대기열에 쌓이게 됩니다.

**(3)** 1초마다 동기화 돼어 과목 마감(성공), 실패 로직을 수행합니다.

**(4)** 성공시, 이벤트가 종료되지 않았으면 **100명의 유저중 먼저 들어온 순서대로 `10명씩` 수강 신청을 성공**합니다.

**(5)** 실패시, 다음 대기열로 돌아가면서 **`남은 대기열 순번을 표출`**합니다.

**(6)** 해당 과정을 반복하면서 이벤트는 종료(30개 발급완료)합니다

<aside>
💡 10개씩 발급하는 이유는 DB 부하를 줄이기 위해서 입니다.

</aside>

다중스레드를 발생시켜(50개) 동시성 테스트

### Lock (Application)

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/14aab76e-f1cf-4d68-9d8d-61efca2326fe)

avilableSeets이 40일 때

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/3c986d50-82b2-4a82-9f31-ab90ae637c12)


### 수강 취소 부분 동시성 제어 (by named Lock)

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/e0431964-b253-43cc-b432-3bc10e4742fa)

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/0daeb32a-a790-480b-be8e-a64eb8cf83c0)


### 다중 서버 세션 관리

## Lambda + SES

### 흐름

- Lambda를 사용하여 RDS에 접속합니다.
- Enroll 테이블과(수강신청 기록을 저장하는 테이블) Student 테이블을 Join하여 이메일을 추출합니다.
- SES을 사용하여 해당 이메일로 수강신청 완료 메일을 보냅니다.

### SNS에서 SES로 변경한 이유

- **SNS** : SNS를 통해 이메일을 보내기 위해서는 먼저 이메일 주소가 SNS 주제(Topic)에 구독되어 있어야 하며, 이 구독 프로세스는 수동으로 확인 링크를 클릭해야 완료됩니다. 이 방식은 동적으로 변하는 이메일 주소에 대해 자동화된 방식으로 이메일을 보내는 데는 제한이 있습니다.
- **SES** : 이메일을 프로그래밍 방식으로 보내는 서비스로, 수신자 목록이 동적으로 결정되며, 데이터베이스의 내용에 따라 변화되는 서비스에 SNS보다 적합합니다.

### Lambda 함수 코드

*(이 코드는 SNS 사용을 기준으로 작성해둔 코드입니다)*

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/7e2aa7f2-8163-454d-8f9b-d63b21d0e2e5)

**RDS 접속 성공**

![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/04a64597-674c-47b3-bb4c-a5c824fba2c8)


### SES 생성

- **계정 샌드박스 해제 필요** - 프로덕션 액세스 요청하기
    
    ![image](https://github.com/ddwu-aws-cloud-club/team-b/assets/96467030/5f687f63-ba2b-4e65-bd31-5ef133f69305)

    
    샌드박스 상태에서는 **등록된 사용자**에 한에서만 이메일을 주고 받는 것이 가능합니다. 따라서 계정을 프로덕션 액세스가 가능하도록 AWS측에 요청해야 합니다.
    
- **AWS에 프로덕션 액세스 요청**
    
    1차적으로 요청을 하였으나 더 세부적인 정보가 필요하다는 답변이 왔고, 좀 더 디테일하게 2차적으로 요청을 했습니다. 아직 승인 대기 중입니다…🥺
    

# As Is → To Be

## 기술적 관점

### 캐시 서버: ElastiCache for Redis로 마이그레이션

현재는 WAS 인스턴스 내부에서 Redis를 구현하였으나, 추후 캐시 서버를 위한 ElastiCache를 따로 두어 개선하고자 합니다.

### 3Tier CICD 개선

현재는 Docker+GithubAction을 통해 CICD 를 진행하고 있습니다.

**해당 방식의 문제**

현재 액션에서 사용하는 방식은 Docker Hub 의 public Repository에 도커 이미지를 업로드하고 있기 때문에, public에서의 보안 문제가 발생할 수 있습니다. 이에 Terraform 을 통한 CICD 파이프라인을 개선한다면 보다 3Tier에 적합하도록 개선할 수 있을것으로 보입니다.

[[CICD] Terraform을 통한 AWS 3 Tier 구성 및 CI/CD 파이프라인 배포 #1 - 아키텍처 및 CICD 흐름 소개](https://nyyang.tistory.com/86)

### **Bastion Host 사용 시 이슈 (pain point)**

현재 저희 아키텍처에선느 Bastion host를 사용하여 타 인스턴스에 접근하고 있습니다.

**EC2 접근 방법 변경에 대한 고민**

이에 대해, Bastion host 대신 AWS System Manager Session Manager를 도입하여 보다 안전하게 접근을 관리할 수 있을 것으로 보여, 추후 도입하여 서비스를 개선할 수 있을 것으로 보입니다.

[Amazon EC2 Instance Connect Endpoint 를 활용한 폐쇄된 VPC 환경의 AWS EC2 접근 - LG CNS](https://www.lgcns.com/blog/cns-tech/aws-ambassador/45743/)

**bastion host 사용하는 경우**

- Bastion Host를 사용하여 Private Subnet의 EC2에 접근한다면 keypair 기반의 접속을 동반
- Pain point
    - 지속적인 keypair 관리 이슈
        - 1인 1계정 사용 시 많은 기회비용 발생
    - 폐쇄망에서 사용 불가능
        - VPN 등 다른 Workaround 방식 필요
- 추가 리소스 사용으로 인한 자원 비용 및 관리 에포트 추가 발생
- 일원화 된 접근 제어, 모니터링 불가
    - 추가적인 작업 혹은 솔루션이 필요

**Session Manager 사용하는 경우**

- Bastion Host 대비 장점
    - key pair 없이 IAM을 통한 일원화된 EC2 자원 접근 통제
    - Cloud Trails, SSM Session Logging 을 통한 접근 모니터링 및 감사 가능
- Pain point
    - 폐쇄망일 경우 Endpoint를 추가 구성하여야 하는 번거로움이 존재
