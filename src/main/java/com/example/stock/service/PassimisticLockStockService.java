package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PassimisticLockStockService {

    private StockRepository stockRepository;


    public PassimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void  decrease(Long id,Long quantity){
        Stock stock = stockRepository.findByIdWithPessimistickLock(id);

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);

    }
}
