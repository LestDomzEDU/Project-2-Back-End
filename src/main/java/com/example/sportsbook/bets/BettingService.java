// src/main/java/com/example/sportsbook/bets/BettingService.java
public class BettingService {
private final BetRepository bets;


public BettingService(BetRepository bets) {
this.bets = bets;
}


private BigDecimal computePayout(BigDecimal stake, BigDecimal oddsDecimal) {
return stake.multiply(oddsDecimal).setScale(2, RoundingMode.HALF_UP);
}


@Transactional
public Bet placeBet(String bettorRef, PlaceBetRequest req) {
Bet b = new Bet();
b.setBettorRef(bettorRef);
b.setEventId(req.eventId);
b.setSelection(req.selection);
b.setStake(req.stake.setScale(2, RoundingMode.HALF_UP));
b.setOddsDecimal(req.oddsDecimal.setScale(2, RoundingMode.HALF_UP));
b.setPotentialPayout(computePayout(b.getStake(), b.getOddsDecimal()));
b.setStatus(BetStatus.PENDING);
return bets.save(b);
}


public List<Bet> listForBettor(String bettorRef) {
return bets.findByBettorRefOrderByPlacedAtDesc(bettorRef);
}


@Transactional
public int settleForEvent(SetResultRequest req) {
List<Bet> open = bets.findByEventIdAndStatus(req.eventId, BetStatus.PENDING);
int count = 0;
for (Bet b : open) {
if (b.getSelection() == req.winningSelection) {
b.setStatus(BetStatus.WON);
} else {
b.setStatus(BetStatus.LOST);
}
count++;
}
// JPA dirty checking flushes changes
return count;
}
}