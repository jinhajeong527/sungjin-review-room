package com.sungjin.reviewroom.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {
    /*
    VerificationToken Entity의 특징
    1. Reviewer로 링크백해서 돌아가야 한다(단방향 관계)
    2. Reviewer 등록 후에 생성되어야 한다.
    3. 생성 후 24시간이 지나면 만료된다.
    4. 유일하고 랜덤으로 생성된 값을 갖는다.
    2, 3 은 회원가입 로직의 요구사항
    1, 4 는 VerificationToken 엔티티에 구현되어 있다.
    */
    private static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String token;
  
    @OneToOne(targetEntity = Reviewer.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "reviewer_id") // VerificatioToken과 Reviewer 연관관계의 일관성과 data integrity를 보장하기 위함
    private Reviewer reviewer;
    
    private Date expiryDate;

    public VerificationToken(final String token, final Reviewer reviewer) {
        super();
        this.token = token;
        this.reviewer = reviewer;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
   
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
}
