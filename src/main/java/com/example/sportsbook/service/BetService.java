package com.example.sportsbook.service;

import com.example.sportsbook.dto.CreateBetRequest;
import com.example.sportsbook.model.Bet;
import com.example.sportsbook.repository.BetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BetService {

    private final BetRepository betRepository;

    public BetService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    public Bet createBet(CreateBetRequest req) {
        Bet bet = new Bet();
        bet.setEventId(req.getEventId());
        bet.setAmount(req.getAmount());
        bet.setSelection(req.getSelection());
        bet.setOdds(req.getOdds());
        bet.setStatus("pending");
        bet.setCreatedAt(LocalDateTime.now());
        return betRepository.save(bet);
    }

    public List<Bet> listBets() {
        return betRepository.findAll();
    }

    public void deleteBet(Long id) {
        betRepository.deleteById(id);
    }
}
