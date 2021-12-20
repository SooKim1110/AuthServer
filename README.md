# Authorization System 
Spring Boot 기반의 로그인, 회원가입, 토큰 인증 시스템
<br></br>

# 운영 환경
<pre>
- Spring Boot  v2.6.1
- Spring Security  v2.6.1
- MySQL  v8.0.27
- Redis  v6.2.6
- Thymeleaf v3.0.12
</pre>
<br></br>

# 아키텍쳐
<img width="800" alt="Screen Shot 2021-12-20 at 5 13 38 PM" src="https://user-images.githubusercontent.com/47516074/146736333-7661c79b-2587-4066-8fd8-0f46b748752c.png">
<br></br>

# 기능 및 동작 방식
### JWT 
토큰은 JWT(비밀키를 사용해 서명한 JSON 형태 데이터) 사용. 사용자 인증 정보로 토큰을 발급하기 때문에 추후 인증이 필요하면 토큰만으로 사용자 인증 가능. 비대칭키 암호화 방식을 사용하여 서버에서는 시그니처를 복호화를 통해 토큰의 유효성 검증.
### Access Token, Refresh Token
사용자는 쿠키(http-only) Access Token과 Refresh Token을 서버로 보냄. Access Token으로 인증하고 만료되었다면 Refresh Token으로 새 Access Token을 발급받음. 
Access Token은 짧은 시간에만 사용하도록 해서 탈취 피해를 줄이고, Refresh Token은 탈취당하면 유효시간 동안 계속 Access Token을 발급받을 수 있고 서버에서 이미 발급한 토큰을 만료시킬 수도 없기 때문에 Redis에 저장하여 유효한 Refresh Token인지 표시.

## 1. 회원 가입
1) 입력받은 사용자 정보를 DB에 저장
 - 패스워드는 Spring Security의 BCryptPasswordEncoder 사용해서 암호화
 - BCrypt를 사용하면 salt와 비밀번호를 함께 암호한 결과를 주기 때문에 따로 salt를 만들어 저장할 필요가 없음
 - 사용자의 default 권한은 ROLE_VERIFY_REQUIRED로 등록
2) 사용자 이메일로 이메일 인증 요청 전송 
- 스프링이 제공하는 MailSender 로 SMTP 사용
- 랜덤한 UUID로 인증 링크를 만든 후 전송, Redis에 "UUID: username"을 저장(유효시간 5분으로 설정)
3) 이메일 인증 완료
- 사용자가 링크에 접속하면 Redis에서 username을 찾고, 해당하는 사용자의 권한을 ROLE_USER로 변경

## 2. 로그인, 로그아웃
로그인
1) 입력받은 로그인 정보가 DB와 일치하는지 확인  
2) 유저 정보를 사용해 Access Token과 Refresh Token 발행 (각각 유효시간 10분, 1주일)
3) Redis에 Refresh Token 저장 
4) Access Token과 Refresh Token를 쿠키에 저장

로그아웃
1) 쿠키의 Access Token과 Refresh Token 삭제
2) Redis에 저장된 Refresh Token 삭제

## 3. 토큰 인증 
Spring Security에 custom JWT 인증 filter를 만들어 UsernamePasswordAuthenticationFilter 전에 등록

case 1. Access Token 유효
- 토큰에서 Authentication 받아서 Security Context에 저장

case 2. Access Token 만료, 유효한 Refresh Token 존재
- Refresh Token 이 Redis에 저장된 정보와 일치하면 Access Token 재발급(쿠키)
- Security Context에 Authentication 설정

case 3. Access Token, Refresh Token 둘 다 유효하지 않음
- AuthenticationEntryPoint에서 인증 확인, AccessDeniedHandler에서 인가 확인하여 에러 반환
- 재로그인 필요

<br></br>

# User DB 테이블
<kbd>
<img width="600" alt="Screen Shot 2021-12-20 at 3 03 00 PM" src="https://user-images.githubusercontent.com/47516074/146719296-e102fe03-9092-4ee6-b616-86c19f20e8ce.png">
</kbd>
<br></br> 

# Screenshot

<kbd>
<img width="800" alt="Screen Shot 2021-12-20 at 12 04 17 PM" src="https://user-images.githubusercontent.com/47516074/146718056-7338aa4c-c9a8-44d8-a2b3-b4b1c4577f58.png">
</kbd>

1. 회원가입 <br></br> 

<kbd>
<img width="800" alt="Screen Shot 2021-12-20 at 12 59 37 PM" src="https://user-images.githubusercontent.com/47516074/146718300-576d8f6e-9a89-461a-8ef7-fc1fa2780e4c.png">
</kbd>

2. 이메일 인증 <br></br> 

<kbd>
<img width="800" alt="Screen Shot 2021-12-20 at 12 03 55 PM" src="https://user-images.githubusercontent.com/47516074/146718077-65df4f27-db99-4dae-8037-d7c984feaf83.png">
</kbd>

3. 로그인  <br></br> 

<img width="800" alt="Screen Shot 2021-12-20 at 2 47 21 PM" src="https://user-images.githubusercontent.com/47516074/146718092-b511eac1-1c89-45c0-8f3c-0754c9aa6fb4.png">
</kbd>

4. 로그인 완료 & 로그인 쿠키 access, refresh 토큰 <br></br> 

<kbd>
<img width="800" alt="Screen Shot 2021-12-20 at 2 52 54 PM" src="https://user-images.githubusercontent.com/47516074/146718435-4edee33e-ed70-4c69-bd37-cc8d9adb50b5.png">
</kbd>

5. 관리자 페이지 (ROLE_ADMIN만 접근 가능) <br></br> 

<kbd>
<img width="800" alt="Screen Shot 2021-12-20 at 2 58 44 PM" src="https://user-images.githubusercontent.com/47516074/146718937-abc5e6f6-422a-4b6d-8b99-2566c6b848ea.png">
</kbd>

6. 로그아웃 <br></br>

# 업데이트 예정
1. MSA 구조로 변경 (완료되면 프론트 react로 바꾸기)
2. 소셜 로그인 
3. BCryptPasswordEncoder 쓰지 않고 PasswordEncoder 직접 구현
4. 리팩토링 (예외처리 등)
5. 기타 기능: 비밀번호 찾기, admin page 업그레이드 등

< 목표 아키텍쳐 >
<img width="800" alt="Screen Shot 2021-12-20 at 5 28 47 PM" src="https://user-images.githubusercontent.com/47516074/146736338-120b428a-9762-48ce-b262-6d26cef42b3d.png">
