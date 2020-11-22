# kakaoPay

## 1. 개발환경
  - eclipse
  - java8
  - mariadb
  - tomcat7
  - maven

## 2. 요구 사항
 
 ● 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
 
    - 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
    
    - 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다.
    
    - 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
    
  ● 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
  
  ● 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.


  -> 개발방향 : 뿌리기 API 요청시 뿌리기 정보 생성이후 받기에 필요한 정보를 마스터와 상세 테이블에 등록이후 받기 API 요청시 요청한 사용자의 정보를 업데이트 하는 프로세서로 구현
  
  
  ## 3. 단위테스트
  
    - 뿌리기 API
    
      http://localhost:8080/createPaySpray.do

      입력부 : Header
      Content-Type: application/json
      X-USER-ID: 0000
      X-ROOM-ID: 9999

      입력부 : Body
      {
        "sprayAmt" : 1000
        ,"cntPeople" : 3
      }

      출력부
      {
        "rslt": "SUCCESS",
        "errCode": null,
        "msg": null,
        "msg2": null,
        "obj": 
        {
          "sprayAmt": 1000,
          "cntPeople": 3,
          "userId": "0000",
          "roomId": "9999",
          "tokenId": "ZAH",
          "idx": 11
        }
      }

      kakaoPay.TBPAYSPRAY
      idx|roomId|userId|tokenId|sprayAmt|cntPeople|createTime         |endTime            |
      ---|------|------|-------|--------|---------|-------------------|-------------------|
       11|9999  |0000  |ZAH    |    1000|        3|2020-11-22 17:59:40|2020-11-22 17:59:40|

      kakaoPay.TBPAYSPRAYDETAIL
      seq|idx|roomId|tokenId|userId|getAmt|getTime|
      ---|---|------|-------|------|------|-------|
       25| 11|9999  |ZAH    |      |   866|       |
       26| 11|9999  |ZAH    |      |    71|       |
       27| 11|9999  |ZAH    |      |    63|       |
       
       
    받기 API
    
    http://localhost:8080/getAmt.do
    
    입력부 - Header
    Content-Type: application/json
    X-USER-ID: 1003
    X-ROOM-ID: 9999

    입력부 : Body
    {
      "tokenId" : "ZAH"
      ,"idx" : 11
    }
    
    출력부
    {
      "rslt": "SUCCESS",
      "errCode": null,
      "msg": null,
      "msg2": null,
      "obj": 
      {
        "createTime": 1606035580000,
        "getAmt": 63,
        "maxAmt": 63,
        "idx": 11,
        "userId": "1003",
        "roomId": "9999",
        "seq": 27,
        "timeFlag": 0
      }
    }

    kakaoPay.TBPAYSPRAYDETAIL
    seq|idx|roomId|tokenId|userId|getAmt|getTime            |
    ---|---|------|-------|------|------|-------------------|
     25| 11|9999  |ZAH    |1001  |   866|2020-11-22 18:03:53|
     26| 11|9999  |ZAH    |1002  |    71|2020-11-22 18:09:59|
     27| 11|9999  |ZAH    |1003  |    63|2020-11-22 18:14:55|


    - 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
    입력부 : Header
    Content-Type: application/json
    X-USER-ID: 1003
    X-ROOM-ID: 9999

    입력부 : Body
    {
      "tokenId" : "ZAH"
      ,"idx" : 11
    }

    출력부
    {
      "rslt": "ERROR",
      "errCode": "E800",
      "msg": "한번만 받기가 가능합니다.",
      "msg2": "",
      "obj": null
    }
    
    - 자신이 뿌리기 한건은 자신이 받을 수 없습니다.

    입력부 : Header
    Content-Type: application/json
    X-USER-ID: 0000
    X-ROOM-ID: 9999

    입력부 : Body
    {
      "tokenId" : "KOF"
      ,"idx" : 12
    }

    출력부
    {
      "rslt": "ERROR",
      "errCode": "E800",
      "msg": "자신이 뿌리기한 건은 자신이 받을 수 없습니다.",
      "msg2": "",
      "obj": null
    }

    - 뿌린 건은 10분간만 유효합니다.
    입력부 : Header
    Content-Type: application/json
    X-USER-ID: 0002
    X-ROOM-ID: 9999

    입력부 : Body
    {
      "tokenId" : "KOF"
      ,"idx" : 12
    }

    출력부
    {
      "rslt": "ERROR",
      "errCode": "E800",
      "msg": "마감되었습니다. 다음 기회에!",
      "msg2": "",
      "obj": null
    } 
    
    조회 API
    
    http://localhost:8080/getPaySprayInfo.do

    입력부 : Header
    Content-Type: application/json
    X-USER-ID: 0000
    X-ROOM-ID: 9999

    입력부 : Body
    {
      "tokenId" : "ZAH"
    }

    출력부
    {
      "rslt": "SUCCESS",
      "errCode": null,
      "msg": null,
      "msg2": null,
      "obj": 
      {
        "endGetAmt": 
        [
          {
            "tokenId": "ZAH",
            "getTime": 1606035833000,
            "getAmt": 866,
            "idx": 11,
            "userId": "1001",
            "seq": 25,
            "roomId": "9999"
          },

          {
            "tokenId": "ZAH",
            "getTime": 1606036199000,
            "getAmt": 71,
            "idx": 11,
            "userId": "1002",
            "seq": 26,
            "roomId": "9999"
          },

          {
            "tokenId": "ZAH",
            "getTime": 1606036495000,
            "getAmt": 63,
            "idx": 11,
            "userId": "1003",
            "seq": 27,
            "roomId": "9999"
          }
        ],

        "sprayAmt": 1000,
        "tokenId": "ZAH",
        "createTime": 1606035580000,
        "idx": 11,
        "userId": "0000",
        "roomId": "9999"
      }
    }
    
    
  ## 4. database info
  
      CREATE TABLE `TBPAYSPRAY` (
      `idx` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '거래순번',
      `roomId` varchar(100) NOT NULL COMMENT '대화방ID',
      `userId` int(11) NOT NULL COMMENT '생성자ID',
      `tokenId` varchar(100) NOT NULL COMMENT '토큰정보',
      `sprayAmt` int(10) unsigned DEFAULT NULL COMMENT '뿌리기금액',
      `cntPeople` int(10) unsigned DEFAULT NULL COMMENT '분배인원',
      `createTime` timestamp NULL DEFAULT current_timestamp() COMMENT '생성시각',
      `endTime` timestamp NULL DEFAULT current_timestamp() COMMENT '종료시각',
      PRIMARY KEY (`idx`,`roomId`,`userId`,`tokenId`)
    ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4


    CREATE TABLE `TBPAYSPRAYDETAIL` (
      `seq` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'seq',
      `idx` bigint(20) unsigned NOT NULL COMMENT '거래순번',
      `roomId` varchar(100) NOT NULL COMMENT '대화방ID',
      `tokenId` varchar(100) NOT NULL COMMENT '토큰정보',
      `userId` int(11) DEFAULT NULL COMMENT '대상자ID',
      `getAmt` int(10) unsigned DEFAULT NULL COMMENT '줍기금액',
      `getTime` datetime DEFAULT NULL COMMENT '줍기시각',
      PRIMARY KEY (`seq`,`idx`,`roomId`,`tokenId`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4

