package io.flowing.retail.zeebe.inventory;

import io.camunda.zeebe.spring.client.annotation.JobWorker;

import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;


@Component
public class FetchGoodsAdapter {

  @JobWorker(type = "fetch-goods-z")
  public void fetchGoods(JobClient client, ActivatedJob job) {
    System.out.println("fetch goods");
    client.newCompleteCommand(job.getKey()).send()
      .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job, throwable); });
  }

}
