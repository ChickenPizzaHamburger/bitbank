# BitBank Project
## 학원 : 비트교육센터 (https://www.bitacademy.com/)
## D-day : 2024-11-11 ~ 2024-12-13
## Team : 김진우(Leader), 이재하(Deputy Leader), 정원담, 이승민


## 주요 기능
### 입출금
입금 : 사용자의 계좌에 자금을 추가하는 기능이며, 입금 내역을 기록한다.
출금 : 사용자의 계좌에 자금을 삭제하는 기능이며, 출금 내역을 기록한다.
### 송금
사용자간의 계좌에서 자금을 이동시키는 기능이며, 송금 내역을 기록한다.
### 대출
대출 한도와 이자율을 설정하고, 루트 계좌에서 사용자 계좌로 자금을 이동시키는 기능이다.
## 특수 기능
### 출금 로또
사용자의 출금 수수료의 일정 부분을 특정한 기간(1주)에 한 번씩 랜덤 추첨을 통해서 사용자에게 자금을 지급하는 기능이다.
### 출금락 및 락대출
출금락 : 사용자가 설정한 기간만큼 설정한 금액의 출금 및 송금을 제한하는 기능
락대출 : 락을 건 금액에서 이자율을 뺀만큼 루트 계좌에서 사용자에게 자금을 지급하는 기능으로, 락 기간이 끝나면 그 금액은 사용자가 아닌 루트 계좌로 돌아간다.
### 태그 기능
사용자가 거래 내역(송금)에 태그를 추가하여 내역을 보다 쉽게 구분하고 자금 관리를 할 수 있게 하는 기능
### 자동 환율 환전 송금 기능
환율의 저점을 자동으로 분석해서 저점에 해당하는 시점에 환전을 해서 송금하는 기능
### 가상 매매
매수 : 사용자가 주식의 배수(1배, 2배, 3배…)에 해당하는 금액을 회사에 송금하며, 회사는 그 배수에 해당하는 주식의 수를 사용자에게 송금하는 기능
매도 : 사용자가 가지고 있는 주식을 원하는 갯수만큼 회사에 송금하며, 회사는 형성된 가격에 해당하는 금액에 회사에 송금한 주식의 수를 곱하여 송금하는 기능
### 인력 관리
관리자(admin)의 직급을 나눠서, 루트(은행)이 관리자들의 직급과 권한을 관리하는 기능, 루트는 관리자의 직급을 변경할 수 있으며, 직급마다 권한을 정할 수 있다. 승진 날짜가 기록되며, 월급이 다르다. (월급은 루트 계좌에서 각 관리자 계좌로 송금)
### 일반 대출
사용자의 신용등급이 존재하고, 신용등급에 따라서 빌릴 수 있는 금액과 이자율이 차등 적용되는 기능
고객센터 지점 (지도 api)

## 고려 사항
- 보안성
비밀번호 암호화 : 해시 알고리즘(JB*crypt)*을 사용해서 사용자 비밀번호를 안전하게 보호
SQL 인젝션 방지 : PreparedStatement를 이용해서 외부로부터 SQL 공격을 방지
데이터베이스 정보 노출 방지 : 서블릿 풀을 이용해서 루트의 비밀번호 공개 방지
세션 보안 강화 : 세션 타임아웃을 설정하여 세션 하이재킹을 방지

### 데이터베이스 테이블 구조
 * 회원 테이블 - 회원 정보를 저장하며 기본 키로 회원번호를 사용합니다. 
    `userId`, `username`, `password`, `email`, `birthDate`, `gender`, `role`, `createdAt`
 * 통장 테이블 - 사용자 계좌 정보를 관리합니다. 
	`user_id`, `account_num`, `account_password`, `account_type`, `account_amount`, `account_date`
 * 내역 테이블 - 입출금, 송금 등 거래 내역을 기록합니다. CREATE TABLE `transfer` (
	`sender_account`, `receiver_account`, `send_time`, `send_amount`, `tag` 

-----------------------------------
# 공부 목록
### **1. 서블릿 (자바)**
### **2. DB**
### **3. jQuery**
### **4. 부트스트랩**
### **5. try ~ catch문**
### 6. HTTP 구조
### 7. 구글 머티리얼
### 8. 응답 코드
### 9. GPT 활용법
