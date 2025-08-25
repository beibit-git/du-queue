package kz.dulaty.queue.feature.ticket.data.mapper;

import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {
    TicketMapper TICKET_MAPPER = Mappers.getMapper(TicketMapper.class);
    @Mapping(target = "windowNumber", source = "manager.windowNumber")
    TicketDto toDto(Ticket ticket);
}
