package org.example.expert.domain.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Log extends Timestamped{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long todoId;
    private Long managerUserId;
    private String message;

    public Log(Long userId, Long todoId, Long managerUserId, String message) {
        this.userId = userId;
        this.todoId = todoId;
        this.managerUserId = managerUserId;
        this.message = message;
    }
}
