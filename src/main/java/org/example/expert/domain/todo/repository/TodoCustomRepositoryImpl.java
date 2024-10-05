package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.*;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = queryFactory
                .selectFrom(todo)
                .leftJoin(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(String titleKeyword, String managerNickname, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {

        // 서브 쿼리: 담당자 수
        JPQLQuery<Long> managerCountSubQuery = JPAExpressions
                .select(manager.count())
                .from(manager)
                .where(manager.todo.eq(todo));

        // 서브 쿼리: 댓글 수
        JPQLQuery<Long> commentCountSubQuery = JPAExpressions
                .select(comment.count())
                .from(comment)
                .where(comment.todo.eq(todo));

        // 메인 쿼리
        List<TodoSearchResponse> results = queryFactory
                .select(
                        Projections.constructor(
                                TodoSearchResponse.class,
                                todo.id,
                                todo.title,
                                managerCountSubQuery,
                                commentCountSubQuery
                        )
                )
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(manager.user, user)
                .where(
                        titleContains(titleKeyword),
                        managerNicknameContains(managerNickname),
                        createdAtBetween(startDate, endDate)
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(manager.user, user)
                .where(
                        titleContains(titleKeyword),
                        managerNicknameContains(managerNickname),
                        createdAtBetween(startDate, endDate)
                )
                .fetchOne();

        long totalCounts = (total != null) ? total : 0L;

        return new PageImpl<>(results, pageable, totalCounts);
    }

    private BooleanExpression titleContains(String titleKeyword) {
        return titleKeyword != null ? todo.title.contains(titleKeyword) : null;
    }

    private BooleanExpression managerNicknameContains(String nickname) {
        return nickname != null ? user.nickname.contains(nickname) : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return todo.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return todo.createdAt.goe(startDate);
        } else if (endDate != null) {
            return todo.createdAt.loe(endDate);
        } else {
            return null;
        }
    }
}
