package com.demo.vendingmachine.service;

import com.demo.vendingmachine.dao.model.*;
import com.demo.vendingmachine.dao.repository.PaymentTypeRepository;
import com.demo.vendingmachine.dao.repository.TotalSalesRepository;
import com.demo.vendingmachine.dao.repository.TransactionRepository;
import com.demo.vendingmachine.dao.repository.VendingMachineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private PaymentTypeRepository paymentTypeRepository;
    private TransactionRepository transactionRepository;
    private TotalSalesRepository totalSalesRepository;
    private VendingMachineRepository vendingMachineRepository;

    private final double[] COINS = {0.5, 0.25, 0.10, 0.05};

    @Autowired
    public TransactionService(PaymentTypeRepository paymentTypeRepository, TransactionRepository transactionRepository, TotalSalesRepository totalSalesRepository, VendingMachineRepository vendingMachineRepository) {
        this.paymentTypeRepository = paymentTypeRepository;
        this.transactionRepository = transactionRepository;
        this.totalSalesRepository = totalSalesRepository;
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public Iterable<Transaction> findAllTransactions() {
        return this.transactionRepository.findAll();
    }

    public Optional<ReportTotalSalesByMachine> findTotalSalesByVendingMachine(Integer vendingMachineId) {
        return this.totalSalesRepository.findTotalSalesByVendingMachine(vendingMachineId);
    }

    public Double findTotalSales() {
        Iterable<Transaction> transactions = this.transactionRepository.findAll();
        return getTotalProfit(transactions);
    }

    public Iterable<ReportTransaction> findReportTransactionsFromDate(LocalDateTime fromDate) {
        Iterable<Transaction> transactions = this.transactionRepository.findTransactionsFromDate(fromDate);
        ArrayList<ReportTransaction> reportTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            reportTransactions.add(
                    ReportTransaction.builder()
                            .withId(transaction.getId())
                            .withTotal(transaction.getTotal())
                            .withTransactionDate(transaction.getTransactionDate())
                            .withPaymentType(transaction.getPaymentType().getType().name().toLowerCase())
                            .withProductName(transaction.getProduct().getName())
                            .withMachineModel(transaction.getVendingMachine().getModel())
                            .build()
            );
        }

        return reportTransactions;
    }

    public Optional<ReportTransaction> sellProduct(RequestSell requestSell) {
        Optional<VendingMachine> optionalVendingMachine = this.vendingMachineRepository.findById(requestSell.getVendingMachineId());

        if (optionalVendingMachine.isPresent()) {
            VendingMachine vendingMachine = optionalVendingMachine.get();
            Optional<Product> optionalProduct = getProduct(vendingMachine.getProducts(), requestSell.getProductId());

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                Double total = requestSell.getQuantity() * product.getPrice();

                Optional<PaymentType> optionalPaymentType = this.paymentTypeRepository.findById(requestSell.getPaymentMethod().ordinal() + 1);
                if (optionalPaymentType.isPresent()) {
                    PaymentType paymentType = optionalPaymentType.get();

                    if (vendingMachine.getPaymentTypes().contains(paymentType)) {
                        ReportTransaction reportTransaction = ReportTransaction.builder()
                                .withMachineModel(vendingMachine.getModel())
                                .withProductName(product.getName())
                                .withPaymentType(paymentType.getType().name().toLowerCase())
                                .withTotal(total)
                                .withMoneyReceived(getMoneyReceived(requestSell.getRequestCashes()))
                                .withChange(getChange(paymentType, total, requestSell.getRequestCashes()))
                                .withTransactionDate(LocalDateTime.now())
                                .build();

                        saveTransaction(requestSell, vendingMachine, product, paymentType, reportTransaction);

                        return Optional.of(reportTransaction);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void saveTransaction(RequestSell requestSell, VendingMachine vendingMachine, Product product, PaymentType paymentType, ReportTransaction reportTransaction) {
        this.transactionRepository.save(Transaction.builder()
                .withVendingMachine(vendingMachine)
                .withProduct(product)
                .withQuantity(requestSell.getQuantity())
                .withTotal(reportTransaction.getTotal())
                .withPaymentType(paymentType)
                .withTransactionDate(reportTransaction.getTransactionDate())
                .build()
        );
    }

    private Double getMoneyReceived(RequestCash[] requestCashes) {
        Double totalMoneyReceived = 0.0;

        for (RequestCash requestCash : requestCashes) {
            totalMoneyReceived += requestCash.getQuantity() * requestCash.getAmount();
        }

        return totalMoneyReceived;
    }

    private Set<RequestCash> getChange(PaymentType paymentType, Double totalPrice, RequestCash[] requestCashes) {
        Double changeAmount = getMoneyReceived(requestCashes) - totalPrice;
        LOG.info("Change Amount: " + changeAmount);

        return calculateCoinChange(paymentType, changeAmount);
    }

    private Set<RequestCash> calculateCoinChange(PaymentType paymentType, Double changeAmount) {
        Set<RequestCash> changeCoinList = new HashSet<>();

        if (!PaymentMethod.CARD.equals(paymentType.getType().name())) {
            if (changeAmount > 0) {
                for (Double coin : this.COINS) {
                    if (changeAmount >= coin) {
                        double coinQuantity = Math.floor(changeAmount / coin);

                        changeCoinList.add(RequestCash.builder().withQuantity((int) coinQuantity).withAmount(coin).build());

                        LOG.info("Change Amount: " + changeAmount + " : " + coinQuantity + " coins of " + coin);
                        changeAmount -= coinQuantity * coin;
                    }
                }
                return changeCoinList;
            }
        }

        LOG.info("Change don't need it");
        changeCoinList.add(RequestCash.builder().withQuantity(0).withAmount(0.0).build());
        return changeCoinList;
    }

    private Optional<Product> getProduct(Set<Product> products, Integer productId) {
        for (Product product : products) {
            if (productId == product.getId()) {
                return Optional.of(product);
            }
        }

        return Optional.empty();
    }

    private Double getTotalProfit(Iterable<Transaction> transactions) {
        Double totalSales = 0.0;

        for (Transaction transaction : transactions) {
            totalSales += transaction.getTotal();
        }

        return totalSales;
    }
}
