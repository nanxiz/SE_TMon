package com.trafficmon;

import java.math.BigDecimal;
import java.util.List;

public class OldRuleCalculator extends GeneralCalculator {

    public static final BigDecimal CHARGE_RATE_POUNDS_PER_MINUTE = new BigDecimal(0.05);

    @Override
    public BigDecimal calculateCharge(List<ZoneBoundaryCrossing> crossings) {

        BigDecimal charge = new BigDecimal(0);
        ZoneBoundaryCrossing lastEvent = crossings.get(0);
        int timeIntheZone = 0;

        for (ZoneBoundaryCrossing crossing : crossings.subList(1, crossings.size())) {

            if (crossing instanceof ExitEvent) {
                charge = charge.add(
                        new BigDecimal(minutesBetween(lastEvent.timestamp(), crossing.timestamp()))
                                .multiply(CHARGE_RATE_POUNDS_PER_MINUTE));
            }

            lastEvent = crossing;
        }

        return charge;
    }
}
