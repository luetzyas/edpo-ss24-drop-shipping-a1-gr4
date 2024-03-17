package io.flowing.retail.payment.resthacks;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.flowing.retail.payment.resthacks.adapter.NotifySemaphorAdapter;

/**
 * Step6: Use Camunda state machine for long-running retry, external task &
 * compensation
 */
@RestController
public class PaymentRestHacksControllerV6 {

  @Autowired
  private HistoryService historyService;

  @Autowired
  private RuntimeService runtimeService;



  @RequestMapping(path = "/payment/v6", method = PUT)
  public String retrievePayment(String retrievePaymentPayload, HttpServletResponse response) throws Exception {
    String traceId = UUID.randomUUID().toString();
    String customerId = "0815"; // get somehow from retrievePaymentPayload
    long amount = 15; // get somehow from retrievePaymentPayload

    Semaphore newSemaphore = NotifySemaphorAdapter.newSemaphore(traceId);
    ProcessInstance pi = chargeCreditCard(traceId, customerId, amount);
    boolean finished = newSemaphore.tryAcquire(500, TimeUnit.MILLISECONDS);
    NotifySemaphorAdapter.removeSemaphore(traceId);

    if (finished) {
      boolean failed = historyService.createHistoricActivityInstanceQuery().processInstanceId(pi.getId()) //
          .activityId("EndEvent_PaymentFailed") //
          .count() > 0;
      if (failed) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "{\"status\":\"failed\", \"traceId\": \"" + traceId + "\"}";
      } else {
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(pi.getId()) //
            .variableName("paymentTransactionId") //
            .singleResult();
        if (historicVariableInstance != null) {
          String paymentTransactionId = (String) historicVariableInstance.getValue();
          return "{\"status\":\"completed\", \"traceId\": \"" + traceId + "\", \"paymentTransactionId\": \"" + paymentTransactionId + "\"}";
        } else {
          return "{\"status\":\"completed\", \"traceId\": \"" + traceId + "\", \"payedByCredit\": \"true\"}";
        }
      }
    } else {
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
      return "{\"status\":\"pending\", \"traceId\": \"" + traceId + "\"}";
    }

  }

  public ProcessInstance chargeCreditCard(String traceId, String customerId, long remainingAmount) {
    return runtimeService //
        .startProcessInstanceByKey("paymentV6", traceId, //
            Variables.putValue("amount", remainingAmount));
  }

}