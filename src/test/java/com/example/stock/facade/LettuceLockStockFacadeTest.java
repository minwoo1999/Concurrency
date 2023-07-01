package com.example.stock.facade;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class LettuceLockStockFacadeTest {

    @Autowired
    private LettuceLockStockFacade lettuceLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before(){
        Stock stock = new Stock(1L, 100L);

        stockRepository.save(stock);

    }
    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }



    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount=100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i=0;i<threadCount;i++){
            executorService.submit(()->{
                try {
                    try {
                        lettuceLockStockFacade.decrease(1L,1L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }finally {
                    countDownLatch.countDown();
                }

            });
        }
        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L,stock.getQuantity());



    }

}