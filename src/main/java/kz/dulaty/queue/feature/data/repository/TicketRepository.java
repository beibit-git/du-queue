package kz.dulaty.queue.feature.data.repository;

import kz.dulaty.queue.feature.data.entity.Department;
import kz.dulaty.queue.feature.data.entity.Ticket;
import kz.dulaty.queue.feature.data.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT get_next_ticket(:departmentId)", nativeQuery = true)
    String getNextTicketNumber(@Param("departmentId") Integer departmentId);
    Ticket findByPhoneNumberAndDepartmentAndTicketStatus(String phoneNumber,
                                                         Department department,
                                                         TicketStatus ticketStatus);
}
