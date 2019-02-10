package com.onishchenko.oleksii.mystore.dao;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDaoFactoryTest {
    @Test
    void singleton() {
        //When
        ProductDao first = ProductDaoFactory.singleton();
        ProductDao second = ProductDaoFactory.singleton();

        //Then
        assertThat(first).isEqualTo(second);
    }

    @RepeatedTest(value = 10)
    void singletonFromDifferentThreads() throws ExecutionException, InterruptedException {
        //When
        Callable<ProductDao> callable = ProductDaoFactory::singleton;

        //Get ExecutorService from Executors utility class, thread pool size is 10
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //create a list to hold the Future object associated with Callable
        List<Future<ProductDao>> list = new ArrayList<>();

        ProductDao expected = ProductDaoFactory.singleton();

        //When
        for (int i = 0; i < 100; i++) {
            //submit Callable tasks to be executed by thread pool
            Future<ProductDao> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
        }

        //Then
        for (Future<ProductDao> future : list) {
            assertThat(future.get()).isEqualTo(expected);
        }
        //shut down the executor service now
        executor.shutdown();
    }
}