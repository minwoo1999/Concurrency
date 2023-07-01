package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.LockRepository;
import com.example.stock.repository.StockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id,Long quantity){

        try{
            lockRepository.getLock(id.toString());
            stockService.decrease(id,quantity) ;
        }finally {
            lockRepository.relaseLock(id.toString());
        }
    }
}
