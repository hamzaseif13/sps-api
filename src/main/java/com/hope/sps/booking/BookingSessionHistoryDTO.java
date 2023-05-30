package com.hope.sps.booking;

import com.hope.sps.zone.ZoneDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BookingSessionHistoryDTO {

    private BookingSessionDTO bookingSession;

    private ZoneDTO zone;

}
