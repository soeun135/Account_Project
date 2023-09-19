package com.example.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration //이 클래스 자체가 spring application 뜰 때 auto scan이 되는 타입이 되고
@EnableJpaAuditing //jpa auditing이 켜진 상태. db에 값 저장, update 할 때
//@CreatedDate, @LastModifiedDate 어노테이션 붙은 애들을 자동으로 저장.
public class JpaAuditingConfiguration {
}
