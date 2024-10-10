package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "managers")
public class Manager {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none"), updatable = false, nullable = false) // 일정 만든 사람 id
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) // 일정 id
    @JoinColumn(name = "todo_id", foreignKey = @ForeignKey(name = "none"), updatable = false, nullable = false)
    private Todo todo;

    public Manager(User user, Todo todo) {
        this.user = user;
        this.todo = todo;
    }
}
