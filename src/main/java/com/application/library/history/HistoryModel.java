package com.application.library.history;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "history")
@Table(name = "history")
@EntityListeners(AuditingEntityListener.class)
public class HistoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long bookId;
    private String status;

    @CreatedBy
    @Column(name = "who_borrow")
    private String createdBy;

    @Column(name = "Time_borrow", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "Time_return")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
