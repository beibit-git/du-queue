package kz.dulaty.queue.feature.ticket.data.repository;

import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.ticket.data.entity.Ticket;
import kz.dulaty.queue.feature.ticket.data.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT get_next_ticket(:departmentId)", nativeQuery = true)
    String getNextTicketNumber(@Param("departmentId") Integer departmentId);

    Ticket findByPhoneNumberAndDepartmentAndTicketStatus(String phoneNumber,
                                                         Department department,
                                                         TicketStatus ticketStatus);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE ticket_counters SET current_ticket_number = 1")
    void cleanCounters();

    // 1) Закрыть текущий CALLED у менеджера
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE tickets " +
            "      SET ticket_status = 'DONE', " +
            "          last_modified_date = now() " +
            "      WHERE manager_id = :managerId " +
            "        AND ticket_status = 'CALLED'", nativeQuery = true)
    int finishCurrentForManager(@Param("managerId") Long managerId);

    // 2) Выбрать следующий WAITING (с блокировкой), вернуть только id
    @Query(value = "SELECT id " +
            "      FROM tickets " +
            "      WHERE department_id = :departmentId " +
            "        AND ticket_status = 'WAITING' " +
            "      ORDER BY created_date ASC " +
            "      FOR UPDATE SKIP LOCKED " +
            "      LIMIT 1", nativeQuery = true)
    Optional<Long> selectNextWaitingIdForUpdate(@Param("departmentId") Long departmentId);

    // 3) Назначить его менеджеру и пометить CALLED
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE tickets " +
            "      SET ticket_status = 'CALLED', " +
            "          manager_id = :managerId, " +
            "          last_modified_date = now() " +
            "      WHERE id = :ticketId", nativeQuery = true)
    int claimById(@Param("ticketId") Long ticketId, @Param("managerId") Long managerId);

    // 4) Удобный метод получить сразу номер талона
    @Query("select t.ticketNumber from Ticket t where t.id = :id")
    Optional<String> findTicketNumberById(@Param("id") Long id);

    // 5) Все активные талоны (WAITING + CALLED) — для публичной доски
    @Query(value = "SELECT * FROM tickets WHERE ticket_status IN ('WAITING', 'CALLED') ORDER BY created_date ASC", nativeQuery = true)
    List<Ticket> findAllActiveTickets();

    // 6) Текущий вызванный талон менеджера (нужен для события TICKET_DONE)
    @Query(value = "SELECT * FROM tickets WHERE manager_id = :managerId AND ticket_status = 'CALLED' LIMIT 1", nativeQuery = true)
    Optional<Ticket> findCurrentCalledForManager(@Param("managerId") Long managerId);

    @Query(value = "select * from tickets t  " +
            "where t.manager_id = :managerId " +
            "and (t.ticket_status = 'CALLED' or t.ticket_status = 'DONE') " +
            "order by t.last_modified_date desc  " +
            "limit 5", nativeQuery = true)
    List<Ticket> getLast5TicketByManagerId(@Param("managerId") Long managerId);
}
