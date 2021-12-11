<h1 align="center">Welcome to Wupitch-Server 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
  <a href="https://github.com/kefranabg/readme-md-generator/graphs/commit-activity" target="_blank">
    <img alt="Maintenance" src="https://img.shields.io/badge/Maintained%3F-yes-green.svg" />
  </a>
  <a href="https://github.com/wupitch/wupitch-server/blob/main/LICENSE" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/github/license/wupitch/wupitch-server" />
  </a>
</p>

> wupitch Back-End Server Project

<div align=center>
  <img width="200" alt="스크린샷 2021-12-05 오후 9 41 41" src="https://user-images.githubusercontent.com/54254402/144746981-d11ae8d8-01e2-4d20-b43f-c3cd08a97186.png">
</div>
<h2 align=center>운동하고 싶은 모든 여자들을 위한 여성 스포츠 활동 매칭 플랫폼, 우피치</h2>

<div align=center>
<img width="905" alt="스크린샷 2021-12-05 오후 9 52 15" src="https://user-images.githubusercontent.com/54254402/144747317-5af71998-a855-474b-9d20-79a565411b59.png">
</div>


## API Docs

### 💎 [wupitch Server Postman API DOCs](https://documenter.getpostman.com/view/12462798/UVC3kTUu)
### ✨ [wpitch Server Swagger](https://prod.wupitch.site/swagger-ui/)

## Access App Store

### 🏬 [Google App Store](https://play.google.com/store/apps/details?id=wupitch.android)
### 🍎 [Apple App Store](https://apps.apple.com/app/id1590732671)

## 기술스택

<p>
  <img src="https://img.shields.io/badge/-SpringBoot-blue"/>&nbsp
  <img src="https://img.shields.io/badge/-JPA-red"/>&nbsp
  <img src="https://img.shields.io/badge/-MySQL-yellow"/>&nbsp
  <img src="https://img.shields.io/badge/-JWT-blue"/>&nbsp
  <img src="https://img.shields.io/badge/-AWS-orange"/>&nbsp
  <img src="https://img.shields.io/badge/-Nginx-red"/>&nbsp
  <img src="https://img.shields.io/badge/-Swagger-black"/>&nbsp
  <img src="https://img.shields.io/badge/-SpringSecurity-green"/>&nbsp
  <img src="https://img.shields.io/badge/-Querydsl-violet"/>&nbsp
</p>

## 개발환경

- backend
  - java11
  - gradle
  - spring-boot 2.5.5

## 시스템 구성도

![drawio](https://user-images.githubusercontent.com/54254402/136225436-8693e719-f8cc-4670-bbbd-33e6e68096cc.png)


## Usage

```sh
$ ./gradlew clean build
```

## ERD
<div align="center">
<img width="612" alt="스크린샷 2021-12-05 오후 10 05 35" src="https://user-images.githubusercontent.com/54254402/144747726-98935071-23ea-4ddb-8a23-11a257b4d96e.png">
</div>


## 개발일지

- 백엔드 프로젝트 생성 (21/10/13) - `commit` : [c033fbf](https://github.com/wupitch/wupitch-server/commit/c033fbff57dc87889d410d6e4fe5bf517a35c4c3)
- Account Log 기능 추가 (21/10/20) - `commit` : [f57305c](https://github.com/wupitch/wupitch-server/commit/f57305cbe3bce489dae7d5385e73e476c48e838e)
- 카카오 OAuth 기능 추가 (21/10/21) - `commit` : [f2b96f7](https://github.com/wupitch/wupitch-server/commit/f2b96f752ae8df305e06b7c2ccf4a0482f85c429)
- Account 로그인, 인증, 인증된 유저 확인 API 추가 (21/10/22) - `commit` : [c0cf3b0](https://github.com/wupitch/wupitch-server/commit/c0cf3b04fd860c40fd528bd458593a900f5d2f81)
- 닉네임 Validation API 추가 (21/10/31) - `commit` : [23d35fe](https://github.com/wupitch/wupitch-server/commit/23d35fee29ba98227484123b07ba58901cf2ce5c)
- Account 정보 변경 Patch API 완성 (21/11/02) - `commit` : [f91915b](https://github.com/wupitch/wupitch-server/commit/f91915b24b5d0001862d8a42ac5c3743ef7d5773)
- 전체 지역 조회 API 완성 (21/11/02) - `commit` : [a964c4e](https://github.com/wupitch/wupitch-server/commit/a964c4e41e769211760866d0ec787ef2e3a7dc6d)
- 스포츠 전체 조회 API 완성 (21/11/02) - `commit` : [5d7d116](https://github.com/wupitch/wupitch-server/commit/5d7d11675cef86a7a49e320cabd8725b085f905c)
- Crew 조건 조회 API 완성 (21/11/04) - `commit` : [ac6de1f](https://github.com/wupitch/wupitch-server/commit/ac6de1f4dbebf007ed6e9fdc201b0051caff4c21)
- Crew 생성 API 완성 (21/11/09) - `commit` : [ae89f21](https://github.com/wupitch/wupitch-server/commit/ae89f21365224b96599428e0cbfddc9136277cff)
- 자체 회원가입 (이메일, 비밀번호) 구현 (21/11/11) - `commit` : [d79d3c7](https://github.com/wupitch/wupitch-server/commit/d79d3c74095fd28ea27fa07403b79984fd743d9d)
- 이메일 Validation API 완성 (21/11/15) - `commit` : [77c1e0c](https://github.com/wupitch/wupitch-server/commit/77c1e0cffac10f4884469184a8329bb0e1f8a2b0)
- 프로필 사진 첨부 API 생성 (21/11/21) - `commit` : [177a554](https://github.com/wupitch/wupitch-server/commit/177a554fab0b0f08a57d231f74221b91ea8afd7a)
- 크루 이미지 생성 API 추가 (21/11/24) - `commit` : [430d1bc](https://github.com/wupitch/wupitch-server/commit/430d1bc0f96d6fe4f773dc37eac6303ecf0725bc)
- 번개 생성 API 추가 (21/11/24) - `commit` : [1b96cfc](https://github.com/wupitch/wupitch-server/commit/1b96cfcfb8b2f5c05e662203a52ed023bcee0ca4)
- 번개 조회 API 추가 (21/11/25) - `commit` : [3b6d7cf](https://github.com/wupitch/wupitch-server/commit/3b6d7cf8f7288f4576881528647357dcdcf410fc)
- 회원 탈퇴 API 추가 (21/11/25) - `commit` : [148489e](https://github.com/wupitch/wupitch-server/commit/148489e998858b061fe3d59bf700ad0b9cea4cc5)
- 유저 패스워드 변경 API 추가 (21/11/26) - `commit` : [7a85cd0](https://github.com/wupitch/wupitch-server/commit/7a85cd0986fe41546e70131ac404a6aac8ff4b72)
- 크루 세부 조회 API 추가 (21/11/27) - `commit` : [33e54ee](https://github.com/wupitch/wupitch-server/commit/33e54ee08816154f6a9d25e4f2fcd72b47f90ed0)
- 번개 세부 조회 API 추가 (21/11/27) - `commit` : [279ad15](https://github.com/wupitch/wupitch-server/commit/279ad15d7dc8c0c3a6459d30e9f5f8d768a68e04)
- 인증된 회원의 폰 넘버 조회 API, 지역 조회 API 생성 (21/11/28) - `commit` : [9a8459d](https://github.com/wupitch/wupitch-server/commit/9a8459d725eddee829fea0a1f21fa2aee9a57f0d)
- 크루, 번개 핀업 토글 API 생성 (21/11/28) - `commit` : [79cf214](https://github.com/wupitch/wupitch-server/commit/79cf2144291670e576ff0f5d54bba25a75908168), [79cf214](https://github.com/wupitch/wupitch-server/commit/79cf2144291670e576ff0f5d54bba25a75908168)
- 크루 타이틀로 크루 조회 API 생성 (21/12/02) - `commit` : [089748a](https://github.com/wupitch/wupitch-server/commit/089748a705a8e0b1aea47e249243a778ec20739e) 
- 번개 타이틀로 번개 조회 API 생성 (21/12/03) - `commit` : [58423a6](https://github.com/wupitch/wupitch-server/commit/58423a6547b01b82c703bec17693811cc0dcac51) 
- 크루 게스트 참여 요청 정보 조회 API 생성 (21/12/03) - `commit` : [4a44257](https://github.com/wupitch/wupitch-server/commit/4a44257416d683e272d4af5cd4cf6b32c9ad20e3) 


## Author

👤 **vividswan**

* Website: vividswan.github.io
* Github: [@vividswan](https://github.com/vividswan)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/vividswan/K.Cook-Server/issues). 

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2021 [vividswan](https://github.com/vividswan).<br />
This project is [MIT](https://github.com/wupitch/wupitch-server/blob/main/LICENSE) licensed.

***
_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_
