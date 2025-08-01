package kz.dulaty.queue.feature.manager.data.specification;

import jakarta.persistence.criteria.*;
import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.manager.data.dto.ManagerFilter;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Builder
public class ManagerSpecification implements Specification<Manager> {
    private final ManagerFilter filter;

    @Override
    public Predicate toPredicate(Root<Manager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.distinct(true);
        Predicate predicate = cb.conjunction();

        Join<Manager, Department> department = root.join(Manager.Fields.department, JoinType.LEFT);

        String raw = filter != null ? filter.keyword() : null;
        String keyword = raw == null ? "" : raw.trim();

        if (!keyword.isEmpty()) {
            // создаём join с пользователем только когда нужен
            Join<Manager, User> user = root.join(Manager.Fields.user, JoinType.LEFT);

            List<String> tokens = Arrays.stream(keyword.split("\\s+"))
                    .filter(s -> !s.isBlank())
                    .toList();

            List<Predicate> tokenPredicates = new ArrayList<>();
            for (String token : tokens) {
                String like = likePattern(token.toLowerCase(Locale.ROOT));

                List<Predicate> fields = new ArrayList<>();
                fields.add(cb.like(cb.lower(department.get(Department.Fields.fullName)), like, '\\'));
                fields.add(cb.like(cb.lower(department.get(Department.Fields.name)), like, '\\'));
                fields.add(cb.like(cb.lower(user.get(User.Fields.name)), like, '\\'));
                fields.add(cb.like(cb.lower(user.get(User.Fields.surname)), like, '\\'));
                fields.add(cb.like(cb.lower(user.get(User.Fields.patronymic)), like, '\\'));
                fields.add(cb.like(cb.lower(user.get(User.Fields.email)), like, '\\'));
                fields.add(cb.like(cb.lower(user.get(User.Fields.phoneNumber)), like, '\\'));
                fields.add(cb.like(cb.lower(root.get(Manager.Fields.windowNumber)), like, '\\'));

                // токен должен встретиться хотя бы в одном поле
                Predicate tokenMatchesAnyField = cb.or(fields.toArray(Predicate[]::new));
                tokenPredicates.add(tokenMatchesAnyField);
            }

            // все токены должны встретиться (AND по токенам)
            predicate = cb.and(predicate, cb.or(tokenPredicates.toArray(Predicate[]::new)));
        }

        // Фильтры
        if (filter != null && filter.departmentId() != null) {
            predicate = cb.and(predicate, cb.equal(department.get(Department.Fields.id), filter.departmentId()));
        }

        if (filter != null && filter.windowNumber() != null) {
            predicate = cb.and(predicate, cb.equal(root.get(Manager.Fields.windowNumber), filter.windowNumber()));
        }

        return predicate;
    }

    /**
     * Экранируем спецсимволы LIKE и оборачиваем в %...%
     */
    private static String likePattern(String s) {
        String escaped = s
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
